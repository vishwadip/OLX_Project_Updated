package com.zensar.configuration;

import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.resolver.DefaultAddressResolverGroup;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public HttpClientCustomizer httpClientResolverCustomizer() {
		return new HttpClientCustomizer() {
			
			@Override
			public HttpClient customize(HttpClient httpClient) {
				// TODO Auto-generated method stub
				return httpClient.resolver(DefaultAddressResolverGroup.INSTANCE);
			}
		};
	}
}
