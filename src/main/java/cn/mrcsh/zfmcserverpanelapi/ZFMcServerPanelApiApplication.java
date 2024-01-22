package cn.mrcsh.zfmcserverpanelapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@MapperScan("cn.mrcsh.zfmcserverpanelapi.mapper")
public class ZFMcServerPanelApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZFMcServerPanelApiApplication.class, args);
    }

}
