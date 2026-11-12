package br.com.AEP.dto;

import br.com.AEP.model.StatusSolicitacao;
import jakarta.validation.constraints.NotNull;

public record StatusSolicitacaoRequest(
        @NotNull(message = "status é obrigatório") StatusSolicitacao status) {
}
