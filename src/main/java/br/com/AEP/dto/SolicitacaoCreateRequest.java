package br.com.AEP.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record SolicitacaoCreateRequest(
        @NotBlank(message = "instituicaoId é obrigatório") String instituicaoId,
        String destinatarioNome,
        String destinatarioDocumento,
        String destinatarioRua,
        String destinatarioCidade,
        String destinatarioEstado,
        String motivo,
        String observacao,
        @NotEmpty(message = "a solicitação deve possuir ao menos um item")
        List<@Valid ItemSolicitacaoRequest> itens) {

    public SolicitacaoCreateRequest(String instituicaoId, String observacao,
                                    List<ItemSolicitacaoRequest> itens) {
        this(instituicaoId, null, null, null, null, null, null, observacao, itens);
    }
}
