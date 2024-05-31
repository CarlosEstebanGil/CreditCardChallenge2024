package com.charly.dto;

import lombok.Data;

@Data
public class VisaCreditCardDTO extends CreditCardDTO{
	
	@Override
	public String toString() {
		return "VisaCreditCardDTO [hashCode()=" + hashCode() + ", toString()=" + super.toString() + ", getCardHolder()="
				+ getCardHolder() + ", getNumero()=" + getNumero() + ", getFechaVencimiento()=" + getFechaVencimiento()
				+ ", getBrand()=" + getBrand() + ", getClass()=" + getClass() + "]";
	}

}
