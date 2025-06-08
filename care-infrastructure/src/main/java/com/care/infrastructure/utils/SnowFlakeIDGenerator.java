package com.care.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author ke
 * @version version
 * @title SnowFlakeIDGenerator
 * @desc Twitter Snowflake algorithm
 * @date June 7, 2025
 */
@Slf4j
public class SnowFlakeIDGenerator {
    private static final String HOST_NAME = "HOSTNAME";
    private static final String DATACENTER_ID_ENV = "DATACENTER";

    /**
     * Initial timestamp: 2018-12-01 17:16:36
     */
    private final static long START_STMP = 1543655796000L;
    /**
     * Number of bits for sequence number
     */
    private final static long SEQUENCE_BIT = 7;
    /**
     * Number of bits for machine identifier
     * Changed from 7 to 9 bits, ID expanded from 16 to 17 digits
     */
    private final static long MACHINE_BIT = 10;
    /**
     * Number of bits for data center identifier
     * Not used since our company rarely uses this bit, given all to machine_id
     */
    private final static long DATACENTER_BIT = 0;
    /**
     * Maximum value for each part
     */
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    /**
     * Left shift for each part
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private static final SnowFlakeIDGenerator DEFAULT_GENERATOR = new SnowFlakeIDGenerator(
            Optional.ofNullable(System.getenv(DATACENTER_ID_ENV)).map(Integer::parseInt)
                    .orElse((int) (System.currentTimeMillis() % (1 << DATACENTER_BIT))),
            Optional.ofNullable(getMachineId()).orElse((int) (System.currentTimeMillis() % (1 << MACHINE_BIT)))
    );

    /**
     * Data center ID
     */
    private final long datacenterId;
    /**
     * Machine ID
     */
    private final long machineId;
    /**
     * Sequence number
     */
    private long sequence = 0L;
    /**
     * Last timestamp
     */
    private long lastStmp = -1L;

    public SnowFlakeIDGenerator(final long datacenterId, final long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    public static long nextNumber() {
        return DEFAULT_GENERATOR.nextId();
    }

    public static String nextStringNumber() {
        return String.valueOf(DEFAULT_GENERATOR.nextId());
    }

    public static long nextNumber(byte prefix) {
        return DEFAULT_GENERATOR.nextId(prefix);
    }

    private static Integer getMachineId() {
        String hostname = System.getenv(HOST_NAME);
        if (isEmpty(hostname)) {
            hostname = getMachineIdByInetAddress();
            if (isEmpty(hostname)) {
                return null;
            }
        }
        log.info("SnowFlakeIDGenerator get hostName:{}", hostname);
        // Match the trailing number in hostname as machine ID
        Pattern compile = compile("\\d+$");
        Matcher matcher = compile.matcher(hostname);
        return matcher.find() ? Integer.parseInt(matcher.group()) : null;
    }

    private static String getMachineIdByInetAddress() {
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }

    private static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * Generate the next ID
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }
        if (currStmp == lastStmp) {
            // If in the same millisecond, increment the sequence
            sequence = sequence + 1 & MAX_SEQUENCE;
            // If the sequence number reaches the max value
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            // Reset the sequence number for a new millisecond
            sequence = 0L;
        }
        lastStmp = currStmp;
        // Timestamp + Data center ID + Machine ID + Sequence
        return currStmp - START_STMP << TIMESTMP_LEFT | datacenterId << DATACENTER_LEFT | machineId << MACHINE_LEFT
                | sequence;
    }

    /**
     * Generate the next ID with a prefix
     */
    public synchronized long nextId(byte prefix) {
        assert prefix < 10 && prefix > 0;
        long id = nextId();
        return Long.parseLong(prefix + "" + id);
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }
}
