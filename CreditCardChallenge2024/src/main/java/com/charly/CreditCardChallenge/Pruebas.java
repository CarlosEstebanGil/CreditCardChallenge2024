package com.charly.CreditCardChallenge;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.charly.dto.VisaCreditCardDTO;
import com.charly.model.Tarjeta;
import com.charly.dto.AmexCreditCardDTO;
import com.charly.dto.BrandDTO;
import com.charly.dto.NaraCreditCardDTO;
import com.charly.dto.OperacionDTO;
import com.charly.dto.TasaOperacionDTO;
import com.charly.service.AmexService;
import com.charly.service.NaraService;
import com.charly.service.OperacionService;
import com.charly.service.TarjetaService;
import com.charly.service.VisaService;
import com.charly.util.TarjetaFactory;

/**
 * 
 * @author Carlos Gil
 *
 * Casos de Prueba Solicitados en el Documento del Challenge
 * 
 * Obs: 
 * 		1- Estas Pruebas son ejecutadas automaticamente por el sistema ya que esta clase implementa CommandLineRunner
 *   	2- Las salidas se hacen por consola; pero tambien podrian haberse hecho por log (log4j)
 *      3- Una vez starteada la app y ejecutadas estas pruebas queda el servidor escuchando en puerto 80, donde tambien se puede 
 *      	probar el controller solicitado (que de paso reutilice la operacion con id 1 recien creada por estas pruebas, ej:
 *      	http://localhost:8080/operaciones/tasa/1
 *      
 *      En resumen. CreditCardChallengeApplication -> run as -> java Application ( ver rtados por consola ) y luego probar el controller
 */

@Component
@Order(2) // Se ejecuta después de DatabaseInitializer
public class Pruebas implements CommandLineRunner {

    private final TarjetaFactory<VisaCreditCardDTO> tarjetaFactoryVisa;
    private final OperacionService operacionService;

    private final TarjetaFactory<NaraCreditCardDTO> tarjetaFactoryNara;
    private final TarjetaFactory<AmexCreditCardDTO> tarjetaFactoryAmex;
    
    @Autowired
    private VisaService visaService;
    
    @Autowired
    private NaraService naraService;
    
    @Autowired
    private AmexService amexService;
    
    @Autowired
    private TarjetaService tarjetaService;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public Pruebas(TarjetaFactory<VisaCreditCardDTO> tarjetaFactoryVisa, TarjetaFactory<AmexCreditCardDTO> tarjetaFactoryAmex,
    							TarjetaFactory<NaraCreditCardDTO> tarjetaFactoryNara, OperacionService operacionService) {
        this.tarjetaFactoryVisa = tarjetaFactoryVisa;
        this.tarjetaFactoryAmex = tarjetaFactoryAmex;
        this.tarjetaFactoryNara = tarjetaFactoryNara;
        this.operacionService = operacionService;
    }

	//Saber: Mi applicacion ya crea automaticamente las tablas y popula automaticamente las marcas existentes en el mercado en el "startup", 
	//			ya que el factory necesita saberlas en su codigo de antemano para poder asociarles el @service correspondiente a c/u.
	
	// 		entonces: -> tarjetas disponibles en DB: (1, 'Visa', true); (2, 'Nara', false); (3, 'Amex', true);   

    @Override
    public void run(String... args) throws Exception {

    	//0) Limpio pruebas anteriores: (es opcional ,pero practico xa volver a correr estos mismos casos de prueba en otra ejecucion/startup)
        limpiarDataPruebasPrevias();
        
        //a) Crear una tarjeta usando mi API (xa crear 1 tarjeta utilizo el factory. este la crea y la graba en db incluido el brand, todo auto)
        VisaCreditCardDTO visaCreditCard = tarjetaFactoryVisa.createCreditCard(VisaCreditCardDTO.class, "Carlos Gil", "8111111111111111", LocalDate.now().plusYears(1));
       
        Tarjeta tarjetaVisa = Tarjeta.createTarjetaFromCreditCardDTO(visaCreditCard); 

        //b) Creo un objeto tarjeta de dominio  de negocio desde el dto obtenido del factory ( el cual ademas recordemos la dió de alta en db )
        
        if (tarjetaVisa != null) {
            System.out.println("Información de la tarjeta Visa:");
            System.out.println(tarjetaVisa.toString());
        } else {
            System.out.println("No se pudo crear la tarjeta Visa.");
        }

        // CASOS DE PRUEBA SOLICITADOS EN EL DOCUMENTO DEL CHALLENGE: 
        // _________________________________________________________
        
        //1) mostrar el id de la tarjeta guardada
        
        Long idTarjeta = visaCreditCard.getId();
        System.out.println("Tarjeta guardada con ID: " + idTarjeta);
        
        //2) Crear una Operación 
        
        OperacionDTO operacion1 = new OperacionDTO(null, visaCreditCard.getNumero(), 100.0);
        OperacionDTO savedOperacion = operacionService.saveOperacion(operacion1);

        //3) Obtener y mostrar todas las operaciones
        
        Iterable<OperacionDTO> operaciones = operacionService.getAllOperaciones();
        operaciones.forEach(op -> System.out.println("Operación: " + op.getId() + ", Importe: " + op.getImporte()));

        //4) Verificar si una operación es válida
        
        System.out.println("Operación 1 es válida: " + operacionService.isOperacionValid(operacion1));

        //5)a) Verificar si una tarjeta de crédito es válida ( solo verifica el booleano, no solicitado en el doc del challenge pero lo dejo )
        
        System.out.println("Tarjeta de crédito Visa es válida: " + operacionService.isCreditCardValid(visaCreditCard));

        //5)b) Verificar si una tarjeta de credito es valida PARA OPERAR: (solicitado en el doc del challenge )
        //						-------				  ------ -----------
        System.out.println( "Tarjeta Válida PARA OPERAR: " + tarjetaService.esTarjetaValidaParaOperar(visaCreditCard));
        
        //6) Verificar si dos tarjetas son diferentes ( ya lo habia hecho con los dto pero en realidad iria todo con clases tarjeta del model)  
        //													lo dejo asi x motivos de tiempo ya q la implementacion es igual pero en el model
        
        VisaCreditCardDTO otraVisaCreditCard = tarjetaFactoryVisa.createCreditCard(VisaCreditCardDTO.class, "Juan Perez", "8222222222222222", LocalDate.now().plusYears(2));
        System.out.println("Tarjeta Visa es diferente de otra Tarjeta Visa: " + operacionService.areCardsDifferent(visaCreditCard, otraVisaCreditCard));

        //7) Obtener tasa de una operación
        
        	//a) Prueba con tasa NARA
        	double tasaVisa = tarjetaService.getTasaRate(visaService);
	    	System.out.println("Tasa de operación para VISA: " + tasaVisa);
	    
	        //b) Prueba con tasa NARA
	    	double tasaNara= tarjetaService.getTasaRate(naraService);//("Visa");
	        System.out.println("Tasa de operación para NARA: " + tasaNara);
	
	        //c) Prueba con tasa AMEX

	        double tasaAmex = tarjetaService.getTasaRate(amexService);//("Visa");
	        System.out.println("Tasa de operación para AMEX: " + tasaAmex);
	        
        //8) Obtener el número de una tarjeta de crédito por su ID
        
	    String numeroTarjeta = operacionService.getNumeroDeTarjeta(visaCreditCard.getId());
        System.out.println("Número de tarjeta Visa: " + numeroTarjeta);
        
        //9) Creacion de una tarjeta amex
        
        AmexCreditCardDTO amexCreditCard = tarjetaFactoryAmex.createCreditCard(AmexCreditCardDTO.class, "Juan Perez", "5222222222222222", LocalDate.now().plusYears(2));
        
        if (amexCreditCard != null) {
            System.out.println("Información de la Amex Visa:");
            System.out.println(visaCreditCard.toString());
        } else {
            System.out.println("No se pudo crear la tarjeta Amex.");
        }

        //10) Creacion de una tarjeta Nara
        
        NaraCreditCardDTO naraCreditCard = tarjetaFactoryNara.createCreditCard(NaraCreditCardDTO.class, "Juan Perez", "6222222222222222", LocalDate.now().plusYears(2));
        
        if (amexCreditCard != null) {
            System.out.println("Información de la Nara Visa:");
            System.out.println(naraCreditCard.toString());
        } else {
            System.out.println("No se pudo crear la Nara Amex.");
        }
        
        //11) Prueba "getTasafromoperacion" ( tasa depende de brand asoc a la tarj asoc a la op )
        //		( Obs: uso la operacion1 creada anteriormente )
       
        TasaOperacionDTO  tasaOperacionDTO = operacionService.getTasaOperacion(operacion1.getId());
        Double tasaFromOp = tasaOperacionDTO.getTasa(); 
        System.out.println("La tasa Obtenida desde una operacion dada su tarj cred asociada que tiene un brand especifico asociado es: "+ tasaFromOp + " calculado dinamicamente por el strategy pattern ");
        
       
        // ROBUSTEZ: ( arroja nuestra custom exception de que no se puede crear una tarjeta con el mismo 
        //						numero de tarjeta (mas allá del id autogen de la entidad	) 
        VisaCreditCardDTO mismaVisaCreditCardNumber;
        try {
        	
        	mismaVisaCreditCardNumber = tarjetaFactoryVisa.createCreditCard(VisaCreditCardDTO.class, "Carlos Gil", "8111111111111111", LocalDate.now().plusYears(1));			
		} catch (Exception e) {
			System.out.println("Custom Aplication Exception: La tarjeta con ese numero ya existia y la aplicacion ante ese caso arroja una custom exception tarjeta repetida" );
		}
        
    }
    private void limpiarDataPruebasPrevias() {
        // Limpio la tabla de operaciones
        jdbcTemplate.execute("DELETE FROM operacionentity");
        jdbcTemplate.execute("ALTER TABLE operacionentity AUTO_INCREMENT = 1");

        // Limpio la tabla de tarjetas de crédito
        jdbcTemplate.execute("DELETE FROM creditcardentity");
        jdbcTemplate.execute("ALTER TABLE creditcardentity AUTO_INCREMENT = 1");
    }
}
