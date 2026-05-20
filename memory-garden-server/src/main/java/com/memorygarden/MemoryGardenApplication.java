package com.memorygarden;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 记忆花园系统启动类
 *
 * @author jLU
 * @date 2026-05-20
 */
@SpringBootApplication
@MapperScan("com.memorygarden.mapper")
@EnableScheduling
public class MemoryGardenApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoryGardenApplication.class, args);
    }
}
