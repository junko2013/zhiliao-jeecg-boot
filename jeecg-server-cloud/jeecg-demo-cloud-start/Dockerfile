FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER service@songguo.info

RUN ln -sf /usr/share/zoneinfo/UTC /etc/localtime

RUN mkdir -p /jeecg-demo-cloud

WORKDIR /jeecg-demo-cloud

EXPOSE 7002

ADD ./target/jeecg-demo-cloud-start-3.6.3.jar ./

CMD sleep 1;java -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar jeecg-demo-cloud-start-3.6.3.jar
