package com.djxlzk.dsasystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.djxlzk.dsasystem.mapper")
public class DsaSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DsaSystemApplication.class, args);
    }

}
