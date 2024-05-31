package com.charly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


 @SpringBootApplication(scanBasePackages =  { "com.charly", "com.charly.entity", "com.charly.configuration", "com.charly.util", 
											"com.charly.CreditCardChallenge" , "com.charly.service", "com.charly.repository",
											"com.charly.controller"} )
/**
 * @author Carlos Gil
 * 
 *  Punto de entrada a la aplicacion. ( run as -> java application )  
 * 
 *  Se Ejecuta Automaticamente pruebas.java (CommandLineRunner) con los casos de prueba solicitados en el documento del challenge.
 *    
 */
public class CreditCardChallengeApplication {

	public static void main(String[] args) throws Exception{
		SpringApplication.run(CreditCardChallengeApplication.class, args);
	}

}
