package com.charly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperacionDTO {

    private Long id;
    private String numeroTarjeta;
    private Double importe;

}
