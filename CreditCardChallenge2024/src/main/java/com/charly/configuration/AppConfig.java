package com.charly.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import com.charly.dto.CreditCardDTO;
import com.charly.util.TarjetaFactory;

@Configuration
public class AppConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public <T extends CreditCardDTO> TarjetaFactory<T> tarjetaFactory() {
        return new TarjetaFactory<>(applicationContext);
    }
}
