package org.jeecg;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 知了IM 消息队列处理启动类
 */
@Slf4j
@SpringBootApplication(exclude={MongoAutoConfiguration.class})
@EnableFeignClients(basePackages = {"org.jeecg"})
@EnableScheduling
public class ZhiLiaoMQApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ZhiLiaoMQApplication.class);
    }

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(ZhiLiaoMQApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = oConvertUtils.getString(env.getProperty("server.servlet.context-path"));
        log.info("\n----------------------------------------------------------\n" +
                "Application ZhiLiaoIM MQ is running! Access URLs:\n" +
                "Local: \thttp://localhost:" + port + path + "/doc.html\n" +
                "External: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");

    }

}