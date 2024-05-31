package com.charly.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreditCardDTO {
	private Long id;
    private String cardHolder;
    private String numero;
    private LocalDate fechaVencimiento;
    private BrandDTO brand;
	@Override
	public String toString() {
		return "CreditCardDTO [cardHolder=" + cardHolder + ", numero=" + numero + ", fechaVencimiento="
				+ fechaVencimiento + ", brand=" + brand + "]";
	}
    
}