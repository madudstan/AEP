package br.com.AEP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemSolicitacaoRequest(
        @NotBlank(message = "alimentoId é obrigatório") String alimentoId,
        @NotNull(message = "quantidadeSolicitada é obrigatória")
        @Positive(message = "quantidadeSolicitada deve ser maior que zero") Double quantidadeSolicitada,
        @NotBlank(message = "unidade é obrigatória") String unidade) {
}
