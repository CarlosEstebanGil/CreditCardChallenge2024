package com.charly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.charly.entity.CreditCardEntity;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<CreditCardEntity, Long> {
    Optional<CreditCardEntity> findByNumero(String numero);
}
