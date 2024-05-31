package com.charly.configuration;

import com.charly.dto.AmexCreditCardDTO;
import com.charly.dto.CreditCardDTO;
import com.charly.dto.NaraCreditCardDTO;
import com.charly.dto.VisaCreditCardDTO;
import com.charly.service.AmexService;
import com.charly.service.BrandService;
import com.charly.service.NaraService;
import com.charly.service.VisaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TarjetaConfig {

    @Bean
    public Map<Class<? extends CreditCardDTO>, Class<? extends BrandService>> serviceMap() {
        Map<Class<? extends CreditCardDTO>, Class<? extends BrandService>> serviceMap = new HashMap<>();
        serviceMap.put(VisaCreditCardDTO.class, VisaService.class);
        serviceMap.put(AmexCreditCardDTO.class, AmexService.class);
        serviceMap.put(NaraCreditCardDTO.class, NaraService.class);
        return serviceMap;
    }

    @Bean
    public Map<String, Class<? extends BrandService>> brandServiceMap() {
        Map<String, Class<? extends BrandService>> brandServiceMap = new HashMap<>();
        brandServiceMap.put("Visa", VisaService.class);
        brandServiceMap.put("Amex", AmexService.class);
        brandServiceMap.put("Nara", NaraService.class);
        return brandServiceMap;
    }

}

