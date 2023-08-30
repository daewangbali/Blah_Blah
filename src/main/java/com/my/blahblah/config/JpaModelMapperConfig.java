package com.my.blahblah.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaModelMapperConfig {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper(); 
		modelMapper.getConfiguration()
			.setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
			.setMatchingStrategy(MatchingStrategies.LOOSE)
			.setFieldMatchingEnabled(true);
		return modelMapper;
	}
}
