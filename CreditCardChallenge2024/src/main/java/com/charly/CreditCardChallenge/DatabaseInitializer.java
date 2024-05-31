package com.charly.CreditCardChallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.charly.repository.BrandRepository;
import com.charly.entity.BrandEntity;

@Component
@Order(1) // Se ejecuta primero
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Verifico si la tabla está vacía y, de ser así, limpio y restablezco el autoincremento
        if (brandRepository.count() == 0) {

            jdbcTemplate.execute("DELETE FROM brand_entity");
            jdbcTemplate.execute("ALTER TABLE brand_entity AUTO_INCREMENT = 1");

            // Inserto nuestras marcas para que sirempre existan en el sistema ( es configuracion de mi sistema ) 
            brandRepository.save(new BrandEntity(1L, "Visa", true));
            brandRepository.save(new BrandEntity(2L, "Nara", false));
            brandRepository.save(new BrandEntity(3L, "Amex", true));
        } else {
            // Agrego las marcas individualmente si no existen
            if (!brandRepository.existsById(1L)) {
                brandRepository.save(new BrandEntity(1L, "Visa", true));
            }
            if (!brandRepository.existsById(2L)) {
                brandRepository.save(new BrandEntity(2L, "Nara", false));
            }
            if (!brandRepository.existsById(3L)) {
                brandRepository.save(new BrandEntity(3L, "Amex", true));
            }
        }
    }
}

