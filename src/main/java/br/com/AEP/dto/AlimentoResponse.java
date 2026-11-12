package br.com.AEP.dto;

import java.time.LocalDate;

import br.com.AEP.model.StatusAlimento;

public record AlimentoResponse(
        String id,
        String nome,
        Double quantidade,
        String unidade,
        LocalDate dataValidade,
        String origem,
        StatusAlimento status) {
}
