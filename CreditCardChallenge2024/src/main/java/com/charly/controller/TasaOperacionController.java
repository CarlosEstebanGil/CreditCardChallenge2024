package com.charly.controller;

import com.charly.dto.TasaOperacionDTO;
import com.charly.service.OperacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operaciones")
public class TasaOperacionController {

    @Autowired
    private OperacionService operacionService;

    @GetMapping("/tasa/{operacionId}")
    public TasaOperacionDTO getTasaOperacion(@PathVariable Long operacionId) {
        return operacionService.getTasaOperacion(operacionId);
    }

    @GetMapping("/test")
    public String test() {
        return "El controlador está funcionando";
    }
}
