package com.charly.service;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class TasaNaraStrategy implements ITasaStrategy {

	@Override
	public double calcular() {
		 LocalDate fecha = LocalDate.now();
		 int dayOfMonth = fecha.getDayOfMonth();
		 return dayOfMonth * 0.5;
	}
}

