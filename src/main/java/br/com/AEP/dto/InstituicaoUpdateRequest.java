package br.com.AEP.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InstituicaoUpdateRequest(
        @NotBlank(message = "nome é obrigatório") String nome,
        @NotBlank(message = "documento é obrigatório") String documento,
        @NotBlank(message = "email é obrigatório") @Email(message = "email inválido") String email,
        @NotBlank(message = "telefone é obrigatório") String telefone,
        @NotNull(message = "endereco é obrigatório") @Valid EnderecoRequest endereco) {
}
