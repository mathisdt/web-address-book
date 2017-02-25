package org.zephyrsoft.wab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.zephyrsoft.wab.util.ApplicationContextProvider;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;

@SpringBootApplication
@ServletComponentScan
public class SpringBootStarter {
	
	@Value("${wab.databaseName}")
	private String databaseName;
	
	@Bean
	public ApplicationContextProvider ApplicationContextProvider() {
		return new ApplicationContextProvider();
	}
	
	@Bean
	public EbeanServer ebeanServer() {
		return EbeanServerFactory.create(databaseName);
	}
	
	public static void main(final String[] args) {
		SpringApplication.run(SpringBootStarter.class, args);
	}
	
}
