#!/bin/bash

java -Xmx768m \
    -Dspring.profiles.active=prod \
    -Ddb="${DB_NAME}" \
    -DdbInstanceName="${DB_INSTANCE_NAME}" \
    -DmysqlUsername="${MYSQL_USER}" \
    -DmysqlPassword="${MYSQL_PASS}" \
    -jar coolname-0.0.1-SNAPSHOT.jar