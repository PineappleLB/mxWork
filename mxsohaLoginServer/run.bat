@echo off 
set MY_CLASSPATH=. 
set MY_CLASSPATH=%MY_CLASSPATH%;library\commons-beanutils-1.7.0.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\commons-collections-3.2.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\commons-lang-2.4.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\commons-logging-1.1.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\ezmorph-1.0.4.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\json-lib-2.2.3-jdk13.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\log4j-1.2.17.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\mybatis-3.2.7.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\mysql-connector-java-5.1.44-bin.jar
set MY_CLASSPATH=%MY_CLASSPATH%;library\netty-all-4.0.54.Final.jar
set MY_CLASS=client.TimeClient
cd bin
java -cp "%MY_CLASSPATH%" %MY_CLASS%