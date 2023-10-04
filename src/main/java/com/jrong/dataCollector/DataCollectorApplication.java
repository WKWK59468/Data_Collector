package com.jrong.dataCollector;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.jrong.dataCollector.dao")
@EnableScheduling
@SpringBootApplication
public class DataCollectorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataCollectorApplication.class, args);
	}
}
