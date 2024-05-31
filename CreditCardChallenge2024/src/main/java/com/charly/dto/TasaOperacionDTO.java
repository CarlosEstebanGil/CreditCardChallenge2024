package com.charly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TasaOperacionDTO {

    private Long operacionId;
    private double tasa;

}
