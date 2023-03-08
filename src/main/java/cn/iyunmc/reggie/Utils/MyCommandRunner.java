package cn.iyunmc.reggie.Utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyCommandRunner implements CommandLineRunner {

    @Value("${spring.web.loginurl}")
    private String loginUrl;
    @Override
    public void run(String... args) throws Exception {
        log.info("开始自动加载指定的页面...");
        try {
            Runtime.getRuntime().exec("cmd /c start " + loginUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
