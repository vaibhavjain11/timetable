package com.brillio.timetable.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.brillio.timetable")
@EnableAutoConfiguration
public class TimetableApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(TimetableApplication.class, args);
	}

}
