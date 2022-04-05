package com.zensar.olx.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * @author Support
 *	This Configuration File is needed for customizing CORS Configuration
 *  CORS - Cross Origin Resource Sharing
 */

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry
			.addMapping("/api/**")
			.allowedMethods("*")
			.allowedOrigins("http://localhost:4200");
	}
}
