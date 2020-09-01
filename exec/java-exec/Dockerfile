FROM registry.cn-hangzhou.aliyuncs.com/palan/apache-drill:1.16.0

RUN rm -rf /apache-drill-1.16.0/jars/3rdparty/mysql-connector-java-5.1.28.jar
COPY 3rdparty  /apache-drill-1.16.0/jars/3rdparty
COPY target/drill-java-exec-1.16.0.jar /apache-drill-1.16.0/jars/drill-java-exec-1.16.0.jar

