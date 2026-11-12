package br.com.AEP.dto;

import java.time.LocalDate;
import java.util.List;
import br.com.AEP.model.StatusSolicitacao;

public record SolicitacaoResponse(String id, String instituicaoId, LocalDate dataSolicitacao,
                                  String destinatarioNome, String destinatarioDocumento,
                                  String destinatarioRua, String destinatarioCidade, String destinatarioEstado,
                                  String motivo, String observacao, LocalDate dataEntrega,
                                  StatusSolicitacao status, List<ItemSolicitacaoResponse> itens) {
    public SolicitacaoResponse(String id, String instituicaoId, LocalDate dataSolicitacao,
                               String observacao, StatusSolicitacao status,
                               List<ItemSolicitacaoResponse> itens) {
        this(id, instituicaoId, dataSolicitacao, null, null, null, null, null,
                null, observacao, null, status, itens);
    }
}
