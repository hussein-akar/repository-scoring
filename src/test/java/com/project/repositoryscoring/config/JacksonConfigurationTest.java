package com.project.repositoryscoring.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = JacksonConfiguration.class)
class JacksonConfigurationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldHaveObjectMapperRegisteredAsSpringBeanWhenApplicationStarts() {
        ObjectMapper actual = applicationContext.getBean(ObjectMapper.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getRegisteredModuleIds()).contains("jackson-datatype-jsr310");
        assertThat(actual.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).isFalse();
    }
}