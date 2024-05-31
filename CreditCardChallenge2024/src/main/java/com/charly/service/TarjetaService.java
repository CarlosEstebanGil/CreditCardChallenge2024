package com.charly.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charly.dto.CreditCardDTO;
import com.charly.entity.BrandEntity;
import com.charly.entity.CreditCardEntity;
import com.charly.model.Tarjeta;
import com.charly.repository.TarjetaRepository;

import lombok.Data;

@Data
@Service
public class TarjetaService {

	@Autowired
    private TarjetaRepository creditCardRepository;

	private final Map<String, ITasaStrategy> estrategiaTasaMap;
	
	@Autowired
	public TarjetaService(TarjetaRepository tarjetaRepository,Map<String, ITasaStrategy> estrategiaTasaMap) { //) {
	        this.creditCardRepository = tarjetaRepository;
	        this.estrategiaTasaMap = estrategiaTasaMap;
	}
    public CreditCardEntity createCreditCard(String cardHolder, String number, LocalDate expirationDate, BrandEntity brand) {
    	  CreditCardEntity creditCard = new CreditCardEntity();
          creditCard.setCardHolder(cardHolder);
          creditCard.setNumero(number);
          creditCard.setFechaVencimiento(expirationDate);
          creditCard.setBrand(brand);
          return creditCardRepository.save(creditCard);
    }

    public Long saveCreditCard(CreditCardEntity creditCardEntity) {
        CreditCardEntity savedEntity = creditCardRepository.save(creditCardEntity);
        return savedEntity.getId();
    }
    
    public Long saveCreditCardFromDTO(CreditCardDTO creditCardDTO,BrandService brandService) { //) {

    	// Creo la entidad CreditCardEntity
        CreditCardEntity creditCardEntity = new CreditCardEntity();
        creditCardEntity.setCardHolder(creditCardDTO.getCardHolder());
        creditCardEntity.setNumero(creditCardDTO.getNumero());
        creditCardEntity.setFechaVencimiento(creditCardDTO.getFechaVencimiento());
        
        // convierto BrandDTO a BrandEntity
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName(creditCardDTO.getBrand().getName());
        
        // guardo la entidad en la base de datos
        CreditCardEntity savedEntity = creditCardRepository.save(creditCardEntity);

        // devuelvo el ID generado automáticamente
        return savedEntity.getId();
    }

    
    public double getTasaRate(BrandService brandService) {
    	return brandService.getTasa();
    }
    
    public double getTasaRate(Tarjeta tarjeta) {

    	return -1; //TODO: x ahora no lo necesito para lo que solicita el doc del challenge pero queda como pendiente para cerrar todo.
    	//return brandService.getTasa();
    }
    
    public boolean esTarjetaValidaParaOperar(CreditCardDTO tarjeta) {
        LocalDate fechaVencimiento = tarjeta.getFechaVencimiento();
        LocalDate fechaActual = LocalDate.now();
        
        return fechaVencimiento.isAfter(fechaActual);
    }
    
    //new: robustez
    
    public CreditCardEntity findByNumero(String numero) {
        Optional<CreditCardEntity> optionalCreditCard = creditCardRepository.findByNumero(numero);
        return optionalCreditCard.orElse(null); // Retorno null si no se encuentra la tarjeta
    }
}
