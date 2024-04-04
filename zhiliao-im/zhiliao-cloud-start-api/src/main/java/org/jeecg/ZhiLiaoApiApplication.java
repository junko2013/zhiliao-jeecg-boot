package org.jeecg;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.base.BaseMap;
import org.jeecg.common.constant.GlobalConstants;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 知了IM 业务api启动类
 */
@Slf4j
@SpringBootApplication(exclude={MongoAutoConfiguration.class})
@EnableScheduling
@EnableFeignClients(basePackages = {"org.jeecg"})
public class ZhiLiaoApiApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ZhiLiaoApiApplication.class);
    }

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(ZhiLiaoApiApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = oConvertUtils.getString(env.getProperty("server.servlet.context-path"));
        log.info("\n----------------------------------------------------------\n" +
                "Application ZhiLiaoIM app is running! Access URLs:\n" +
                "Local: \thttp://localhost:" + port + path + "/doc.html\n" +
                "External: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");

    }

    /**
     * 启动的时候，触发下gateway网关刷新
     *
     * 解决： 先启动gateway后启动服务，Swagger接口文档访问不通的问题
     * @param args
     */
    @Override
    public void run(String... args) {
        BaseMap params = new BaseMap();
        params.put(GlobalConstants.HANDLER_NAME, GlobalConstants.LODER_ROUDER_HANDLER);
        //刷新网关
        redisTemplate.convertAndSend(GlobalConstants.REDIS_TOPIC_NAME, params);
    }

}