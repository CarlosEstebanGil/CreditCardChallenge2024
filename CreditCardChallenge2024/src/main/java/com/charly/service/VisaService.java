package com.charly.service;

import com.charly.dto.BrandDTO;
import com.charly.dto.VisaCreditCardDTO;
import com.charly.entity.BrandEntity;
import com.charly.entity.CreditCardEntity;
import com.charly.repository.BrandRepository;
import com.charly.repository.TarjetaRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisaService implements BrandService {

    private final TarjetaRepository creditCardRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public VisaService(TarjetaRepository creditCardRepository, BrandRepository brandRepository) {
        this.creditCardRepository = creditCardRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public BrandDTO getBrandByName(String name) {
        Optional<BrandEntity> brandOpt = brandRepository.findByName(name);
        if (brandOpt.isPresent()) {
            BrandEntity brandEntity = brandOpt.get();
            BrandDTO brandDTO = new BrandDTO();
            brandDTO.setId(brandEntity.getId()); 
            brandDTO.setName(brandEntity.getName());
            brandDTO.setInternacional(brandEntity.isInternacional());
            return brandDTO;
        } else {
            return null; // Manejo de error según sea necesario
        }
    }

    public VisaCreditCardDTO getVisaCreditCardDTO(Long id) {
        Optional<CreditCardEntity> creditCardOpt = creditCardRepository.findById(id);
        if (creditCardOpt.isPresent()) {
            CreditCardEntity creditCard = creditCardOpt.get();
            VisaCreditCardDTO dto = new VisaCreditCardDTO();
            dto.setCardHolder(creditCard.getCardHolder());
            dto.setNumero(creditCard.getNumero());
            dto.setFechaVencimiento(creditCard.getFechaVencimiento());

            // Utilizo getBrandByName para obtener la marca
            BrandDTO brandDTO = getBrandByName("Visa");
            dto.setBrand(brandDTO);

            return dto;
        } else {
            return null; // Manejo de error según sea necesario
        }
    }
    
    @Override
    public Double getTasa() {
    	ITasaStrategy tasaStrategy = new TasaVisaStrategy();
    	return tasaStrategy.calcular();
    }
    // Esto lo podria refactorizar hacia una abstractclass intermedia a todos los service de tarjetas 
    //	pero x ahora repito el code en cada tarj xa simplificar

    @Transactional
    public Long saveBrand(BrandEntity brandEntity) {
        // Guardo la marca en la base de datos y devuelve el ID generado
        BrandEntity savedBrand = brandRepository.save(brandEntity);
        return savedBrand.getId();
    }

    @Override
    public BrandEntity findBrandEntityByName(String name) {
        return brandRepository.findByName(name).orElse(null);
    }
    
}
