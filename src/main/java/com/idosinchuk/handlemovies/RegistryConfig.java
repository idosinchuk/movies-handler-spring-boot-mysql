package com.idosinchuk.handlemovies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Micrometer registry configuration
 */
@Configuration
public class RegistryConfig {

	@Bean
	TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}

//	@Bean
//	MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
//		return registry -> registry.config().commonTags("app.name", "movies-handlemovies");
//	}

}