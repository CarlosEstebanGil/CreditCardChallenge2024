package com.charly.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charly.dto.BrandDTO;
import com.charly.dto.NaraCreditCardDTO;
import com.charly.entity.BrandEntity;
import com.charly.entity.CreditCardEntity;
import com.charly.repository.BrandRepository;
import com.charly.repository.TarjetaRepository;

import jakarta.transaction.Transactional;

@Service
public class NaraService implements BrandService{

	private final TarjetaRepository creditCardRepository;
	private final BrandRepository brandRepository;

	@Autowired
	public NaraService(TarjetaRepository creditCardRepository, BrandRepository brandRepository) {
	    this.creditCardRepository = creditCardRepository;
	    this.brandRepository = brandRepository;
	}	
	
	//TODO este code se puede sacar a 1 sola impl xq es comun a todas las tarjetas x ahora y x siempre (de ult lo overridean) sacar a abstract
	@Override
	public BrandDTO getBrandByName(String name) {
        Optional<BrandEntity> brandOpt = brandRepository.findByName(name);
        if (brandOpt.isPresent()) {
            BrandEntity brandEntity = brandOpt.get();
            BrandDTO brandDTO = new BrandDTO();
            brandDTO.setId(brandEntity.getId()); // new.. me faltaba eso !!
            brandDTO.setName(brandEntity.getName());
            brandDTO.setInternacional(brandEntity.isInternacional());
            return brandDTO;
        } else {
            return null; // Manejo de error según sea necesario
        }
      }

    public NaraCreditCardDTO getNaraCreditCardDTO(Long id) {
        Optional<CreditCardEntity> creditCardOpt = creditCardRepository.findById(id);
        if (creditCardOpt.isPresent()) {
            CreditCardEntity creditCard = creditCardOpt.get();
            NaraCreditCardDTO dto = new NaraCreditCardDTO();
            dto.setCardHolder(creditCard.getCardHolder());
            dto.setNumero(creditCard.getNumero());
            dto.setFechaVencimiento(creditCard.getFechaVencimiento());


            BrandDTO brandDTO = getBrandByName("Nara");
            dto.setBrand(brandDTO);

            return dto;
        } else {
            return null; // Manejo de error según sea necesario
        }
    }
	@Override
	public Double getTasa() {
	 	ITasaStrategy tasaStrategy = new TasaNaraStrategy();
    	return tasaStrategy.calcular();
	}
	
    @Transactional
    public Long saveBrand(BrandEntity brandEntity) {
        // Guardo la marca en la base de datos y devuelvo el ID generado
        BrandEntity savedBrand = brandRepository.save(brandEntity);
        return savedBrand.getId();
    }

    @Override
    public BrandEntity findBrandEntityByName(String name) {
        return brandRepository.findByName(name).orElse(null);
    }

}
