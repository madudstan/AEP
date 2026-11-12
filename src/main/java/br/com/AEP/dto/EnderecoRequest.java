package br.com.AEP.dto;

import jakarta.validation.constraints.NotBlank;

public record EnderecoRequest(
        @NotBlank(message = "rua é obrigatória") String rua,
        @NotBlank(message = "numero é obrigatório") String numero,
        @NotBlank(message = "cidade é obrigatória") String cidade,
        @NotBlank(message = "estado é obrigatório") String estado,
        String cep) {
}
