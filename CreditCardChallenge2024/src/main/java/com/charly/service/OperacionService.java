package com.charly.service;


import com.charly.dto.OperacionDTO;
import com.charly.dto.TasaOperacionDTO;
import com.charly.dto.VisaCreditCardDTO;
import com.charly.entity.CreditCardEntity;
import com.charly.entity.OperacionEntity;
import com.charly.repository.OperacionRepository;
import com.charly.repository.TarjetaRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OperacionService {

    private final OperacionRepository operacionRepository;
    private final TarjetaRepository creditCardRepository;
    private final Map<String, ITasaStrategy> estrategiaTasaMap;
    
    private final TarjetaService tarjetaService;

    @Autowired
    private GenericApplicationContext context;
     
    @Autowired
    public Map<String, Class<? extends BrandService>>  brandServiceMap;

    @Autowired
    public OperacionService(OperacionRepository operacionRepository, TarjetaRepository creditCardRepository, 
    							TarjetaService tarjetaService, Map<String, ITasaStrategy> estrategiaTasaMap) {
        this.operacionRepository = operacionRepository;
        this.creditCardRepository = creditCardRepository;
        this.estrategiaTasaMap = estrategiaTasaMap;
        
        this.tarjetaService=tarjetaService;
    }

    public OperacionDTO saveOperacion(OperacionDTO operacionDTO) {
        Optional<CreditCardEntity> creditCard = creditCardRepository.findByNumero(operacionDTO.getNumeroTarjeta());

        if (creditCard.isPresent()) {
            OperacionEntity operacionEntity = new OperacionEntity();
            operacionEntity.setCreditCard(creditCard.get());
            operacionEntity.setImporte(operacionDTO.getImporte());
            operacionEntity = operacionRepository.save(operacionEntity);

            operacionDTO.setId(operacionEntity.getId());
            return operacionDTO;
        } else {
            throw new RuntimeException("Credit card not found");
        }
    }

    public Iterable<OperacionDTO> getAllOperaciones() {
        return StreamSupport.stream(operacionRepository.findAll().spliterator(), false)
                .map(operacion -> new OperacionDTO(operacion.getId(), operacion.getCreditCard().getNumero(), operacion.getImporte()))
                .collect(Collectors.toList());
    }

    final int MAXIMO_POR_OPERACION = 1000;
    public boolean isOperacionValid(OperacionDTO operacionDTO) {
        return operacionDTO.getImporte() < MAXIMO_POR_OPERACION;//1000; // > 0;
    }

    public boolean isCreditCardValid(VisaCreditCardDTO creditCardDTO) {
        return creditCardDTO.getFechaVencimiento().isAfter(LocalDate.now());
    }

    public boolean areCardsDifferent(VisaCreditCardDTO card1, VisaCreditCardDTO card2) {
        return !card1.getNumero().equals(card2.getNumero());
    }


    public String getNumeroDeTarjeta(Long id) {
        Optional<CreditCardEntity> creditCard = creditCardRepository.findById(id);
        return creditCard.map(CreditCardEntity::getNumero).orElse("Tarjeta no encontrada");
    }
    
    public TasaOperacionDTO getTasaOperacion(Long operacionId) {
        Optional<OperacionEntity> operacionOpt = operacionRepository.findById(operacionId);
        if (operacionOpt.isPresent()) {
            OperacionEntity operacion = operacionOpt.get();
            String brandName = operacion.getCreditCard().getBrand().getName();
            Class<? extends BrandService> serviceClass = brandServiceMap.get(brandName);
            if (serviceClass != null) {
                BrandService brandService = context.getBean(serviceClass);
                double tasa = tarjetaService.getTasaRate(brandService);
                return new TasaOperacionDTO(operacionId, tasa);
            } else {
                throw new RuntimeException("No se encontró el servicio para la marca: " + brandName);
            }
        } else {
            throw new RuntimeException("Operación no encontrada");
        }
    }
}
