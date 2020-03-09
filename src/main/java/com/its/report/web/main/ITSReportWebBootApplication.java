package com.its.report.web.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author itshare
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.its.report.*")
public class ITSReportWebBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(ITSReportWebBootApplication.class, "");
	}
}
