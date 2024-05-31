package com.charly.service;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

@Component
public class TasaVisaStrategy implements ITasaStrategy{

	@Override
    public double calcular() {
        LocalDate fecha = LocalDate.now();
        int year = fecha.getYear() % 100; // Tomar solo los dos últimos dígitos del año
        int month = fecha.getMonthValue();
        return (double) year / month;
    }
}
