package org.zephyrsoft.wab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.zephyrsoft.wab.util.ApplicationContextProvider;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.LogLevel;
import com.avaje.ebean.config.AutofetchConfig;
import com.avaje.ebean.config.AutofetchMode;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;

@SpringBootApplication
@ServletComponentScan
public class SpringBootStarter {
	
	@Value("${wab.db.username}")
	private String databaseUsername;
	@Value("${wab.db.password}")
	private String databasePassword;
	@Value("${wab.db.url}")
	private String databaseUrl;
	@Value("${wab.db.driver}")
	private String databaseDriver;
	@Value("${wab.db.heartbeat}")
	private String databaseHeartbeat;
	
	@Bean
	public ApplicationContextProvider ApplicationContextProvider() {
		return new ApplicationContextProvider();
	}
	
	@Bean
	public EbeanServer ebeanServer() {
		DataSourceConfig dsConfig = new DataSourceConfig();
		dsConfig.setUsername(databaseUsername);
		dsConfig.setPassword(databasePassword);
		dsConfig.setUrl(databaseUrl);
		dsConfig.setDriver(databaseDriver);
		dsConfig.setHeartbeatSql(databaseHeartbeat == null || databaseHeartbeat.trim().isEmpty()
			? null
			: databaseHeartbeat);
		dsConfig.setMinConnections(1);
		dsConfig.setMaxConnections(20);
		
		ServerConfig config = new ServerConfig();
		config.setDataSourceConfig(dsConfig);
		config.setName("default");
		
		config.setDdlGenerate(false);
		config.setDdlRun(false);
		config.setDebugSql(false);
		config.setDebugLazyLoad(false);
		
		AutofetchConfig autofetchConfig = new AutofetchConfig();
		autofetchConfig.setQueryTuning(true);
		autofetchConfig.setProfiling(true);
		autofetchConfig.setMode(AutofetchMode.DEFAULT_ON);
		autofetchConfig.setProfilingMin(1);
		autofetchConfig.setProfilingBase(10);
		autofetchConfig.setProfilingRate(0.05);
		config.setAutofetchConfig(autofetchConfig);
		
		config.setLoggingLevel(LogLevel.NONE);
		config.setLoggingToJavaLogger(true);
		
		return EbeanServerFactory.create(config);
	}
	
	public static void main(final String[] args) {
		SpringApplication.run(SpringBootStarter.class, args);
	}
	
}
