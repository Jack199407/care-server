package com.care.infrastructure.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:jdbc-mysql.properties")
@MapperScan(
        basePackages = "com.care.infrastructure.repository.mapper.care",
        sqlSessionFactoryRef = "careSqlSessionFactory"
)
public class DataSourceConfiguration {

    @Value("${jdbc.driverClassName}")
    private String driverClassName;

    @Value("${jdbc.care.url}")
    private String jdbcUrl;

    @Value("${jdbc.care.username}")
    private String username;

    @Value("${jdbc.care.password}")
    private String password;

    @Value("${jdbc.care.testQuery:select 1}")
    private String testQuery;

    @Value("${jdbc.care.connectionTimeout:30000}")
    private int connectionTimeout;

    @Value("${jdbc.care.idleTimeout:600000}")
    private int idleTimeout;

    @Value("${jdbc.care.maxPoolSize:10}")
    private int maxPoolSize;

    @Value("${jdbc.care.minimumIdle:1}")
    private int minimumIdle;

    /**
     * create datasource
     */
    @Bean(name = "careDataSource")
    public DataSource careDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectionTestQuery(testQuery);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minimumIdle);
        return new HikariDataSource(config);
    }

    /**
     * MyBatis SqlSessionFactory
     */
    @Bean(name = "careSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("careDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/care/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "careTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("careDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }


    @Bean(name = "careTransactionTemplate")
    public TransactionTemplate transactionTemplate(@Qualifier("careTransactionManager") PlatformTransactionManager tm) {
        return new TransactionTemplate(tm);
    }


    @Bean(name = "careTransactionPropagationTemplate")
    public TransactionTemplate propagationTemplate(@Qualifier("careTransactionManager") PlatformTransactionManager tm) {
        TransactionTemplate template = new TransactionTemplate(tm);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return template;
    }
}
