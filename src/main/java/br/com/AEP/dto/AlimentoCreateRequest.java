package br.com.AEP.dto;

import java.time.LocalDate;

import br.com.AEP.model.StatusAlimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AlimentoCreateRequest(
        @NotBlank(message = "nome é obrigatório")
        String nome,

        @NotNull(message = "quantidade é obrigatória")
        @Positive(message = "quantidade deve ser maior que zero")
        Double quantidade,

        @NotBlank(message = "unidade é obrigatória")
        String unidade,

        @NotNull(message = "dataValidade é obrigatória")
        LocalDate dataValidade,

        @NotBlank(message = "origem é obrigatória")
        String origem,

        @NotNull(message = "status é obrigatório")
        StatusAlimento status) {
}
