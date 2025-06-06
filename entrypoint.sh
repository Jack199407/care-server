#!/bin/sh
set -e

# Default values (can be overridden via -e at runtime)
APP_NAME="app.jar"
APP_PORT=${APP_PORT:-8080}
JAVA_OPTS=${JAVA_OPTS:--Xms512m -Xmx1024m}
CONFIG_DIR=${CONFIG_DIR:-/app/config}
LOG_DIR=${LOG_DIR:-/app/logs}

# Ensure log and config directories exist
mkdir -p "$LOG_DIR" "$CONFIG_DIR"

echo "Starting application with:"
echo "JAVA_OPTS: $JAVA_OPTS"
echo "PORT: $APP_PORT"
echo "CONFIG_DIR: $CONFIG_DIR"
echo "LOG_DIR: $LOG_DIR"

# Start application
exec java $JAVA_OPTS \
  -jar "$APP_NAME" \
  --server.port="$APP_PORT" \
  --spring.config.additional-location="$CONFIG_DIR/" \
  --logging.file.path="$LOG_DIR"
