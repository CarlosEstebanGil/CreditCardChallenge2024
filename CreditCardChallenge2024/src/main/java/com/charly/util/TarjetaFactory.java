package com.charly.util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.charly.dto.CreditCardDTO;
import com.charly.dto.VisaCreditCardDTO;
import com.charly.entity.BrandEntity;
import com.charly.entity.CreditCardEntity;
import com.charly.dto.AmexCreditCardDTO;
import com.charly.dto.BrandDTO;
import com.charly.dto.NaraCreditCardDTO;
import com.charly.service.BrandService;
import com.charly.service.VisaService;
import com.charly.service.AmexService;
import com.charly.service.NaraService;
import com.charly.service.TarjetaService;

@Component
public class TarjetaFactory<T extends CreditCardDTO> {

    @Autowired
    TarjetaService tarjetaService;

    @Autowired
    private Map<Class<? extends CreditCardDTO>, Class<? extends BrandService>> serviceMap;

    private final ApplicationContext context;

    @Autowired
    public TarjetaFactory(ApplicationContext context) {
        this.context = context;
    }

    public T createCreditCard(Class<T> dtoType, String cardHolder, String numero, LocalDate fechaVencimiento) throws Exception{
        Class<? extends BrandService> serviceClass = serviceMap.get(dtoType);
        if (serviceClass == null) {
            throw new IllegalArgumentException("No service found for DTO type: " + dtoType.getName());
        }

        // Si no existe creo la nueva tarjeta...
        
        BrandService brandService = context.getBean(serviceClass);

        CreditCardDTO creditCardDTO = createDTOInstance(dtoType);
        creditCardDTO.setCardHolder(cardHolder);
        creditCardDTO.setNumero(numero);
        creditCardDTO.setFechaVencimiento(fechaVencimiento);
        creditCardDTO.setBrand(brandService.getBrandByName(getCreditCardNamesByDTOType(creditCardDTO)));

        //new: robustez (xq con def unique nombre parece q no tira exception sino q la inserta igual ) 
        // Verifico entonces si ya existe una tarjeta con el mismo número
        CreditCardEntity existingCard = tarjetaService.findByNumero(creditCardDTO.getNumero());
        if (existingCard != null) {
            throw new Exception("Ya existe una tarjeta con el mismo número");
        }

        Long id = saveCreditCardInDatabase(creditCardDTO, brandService);
        creditCardDTO.setId(id);

        return dtoType.cast(creditCardDTO);
    }

    private Long saveCreditCardInDatabase(CreditCardDTO creditCardDTO, BrandService brandService) {
        String brandName = getCreditCardNamesByDTOType(creditCardDTO);

        BrandEntity brandEntity = findOrCreateBrand(brandName, brandService);

        CreditCardEntity creditCardEntity = new CreditCardEntity();
        creditCardEntity.setCardHolder(creditCardDTO.getCardHolder());
        creditCardEntity.setNumero(creditCardDTO.getNumero());
        creditCardEntity.setFechaVencimiento(creditCardDTO.getFechaVencimiento());
        creditCardEntity.setBrand(brandEntity);

        Long generatedCardId = tarjetaService.saveCreditCard(creditCardEntity);

        return generatedCardId;
    }

    private BrandEntity findOrCreateBrand(String brandName, BrandService brandService) {
        BrandEntity brandEntity = brandService.findBrandEntityByName(brandName);
        if (brandEntity == null) {
            BrandDTO brandDTO = brandService.getBrandByName(brandName);
            brandEntity = convertDTOToEntity(brandDTO);
            brandService.saveBrand(brandEntity);
        }
        return brandEntity;
    }

    private BrandEntity convertDTOToEntity(BrandDTO brandDTO) {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName(brandDTO.getName());
        brandEntity.setInternacional(brandDTO.isInternacional());
        return brandEntity;
    }

    protected String getCreditCardNamesByDTOType(CreditCardDTO creditCardDTO) {
        String r;
        switch (creditCardDTO.getClass().getSimpleName()) {
            case "VisaCreditCardDTO":
                r = "Visa";
                break;
            case "AmexCreditCardDTO":
                r = "Amex";
                break;
            case "NaraCreditCardDTO":
                r = "Nara";
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + creditCardDTO.getClass().getSimpleName());
        }
        return r;
    }

    private T createDTOInstance(Class<T> dtoType) {
        try {
            return dtoType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create DTO instance", e);
        }
    }
}
