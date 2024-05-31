package com.charly.service;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class TasaAmexStrategy implements ITasaStrategy{

	
	public double calcular() {
		LocalDate fecha = LocalDate.now();
        int month = fecha.getMonthValue();
        return month * 0.1;
    }	
}
