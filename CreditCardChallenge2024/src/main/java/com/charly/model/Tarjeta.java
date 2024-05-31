package com.charly.model;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.charly.dto.CreditCardDTO;
import com.charly.service.TarjetaService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Tarjeta {
    
	
	private String numero;
    private LocalDate fechaVencimiento;
    
    private Marca brand;
    

    // TODO pendiente por que x ahora no lo uso para los casos de prueba solicitados en el doc del challenge.
    public double getTasaRate() {	
    	return -1; // la implementacion real es facil: -> la tarj tiene un atrib brand q tiene un atrib name y yo tengo    
    };			   // 									  un map xa obtener la impl concreta asociada xej visaservice q 
    			   //									  x strategy auto ejec TasaVisaSatrategy y me da la tasa desde el 
    			   //									  algorimo de tasavisastrategy 

    public boolean isValid() {
        return fechaVencimiento.isAfter(LocalDate.now());
    }
    
    /**
     * @author Carlos Gil
     * @param  creditCardDTO
     * @return Tarjeta
     * 
     * {@summary}@ Metodo de utilidad que mapea un creditcarddto a un tarjeta ( osea un dto a su clase analoga de modelo ) en este caso map 1a1 
     */
    public static Tarjeta createTarjetaFromCreditCardDTO(CreditCardDTO creditCardDTO) {
    	Tarjeta tarjeta=null;
    	Marca marcaAsociada =null;
    	if (creditCardDTO != null) {
    		//Mapeo data:
    		tarjeta = new Tarjeta();
    		tarjeta.setNumero(creditCardDTO.getNumero());
    		tarjeta.setFechaVencimiento(creditCardDTO.getFechaVencimiento());
    		marcaAsociada=new Marca();
    		marcaAsociada.setId(creditCardDTO.getBrand().getId());
    		marcaAsociada.setInternacional(creditCardDTO.getBrand().isInternacional());
    		marcaAsociada.setName(creditCardDTO.getBrand().getName());
    		tarjeta.setBrand(marcaAsociada);
    	}
    	return tarjeta;
    }
}