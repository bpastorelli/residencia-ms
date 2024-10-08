package br.com.residencia.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableAsync
@Configuration
public class AsyncConfiguration {
	
	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		
    	log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Thread-");
        executor.initialize();
		
		return executor;
		
	}
	
    @Bean(name = "asyncKafka")
    public Executor taskExecutor() {
    	log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Thread-");
        executor.initialize();
        return executor;
    }

}
