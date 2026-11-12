package br.com.AEP.mapper;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import br.com.AEP.dto.ItemSolicitacaoRequest;
import br.com.AEP.dto.ItemSolicitacaoResponse;
import br.com.AEP.dto.SolicitacaoCreateRequest;
import br.com.AEP.dto.SolicitacaoResponse;
import br.com.AEP.model.ItemSolicitacao;
import br.com.AEP.model.Solicitacao;
import br.com.AEP.model.StatusSolicitacao;
import br.com.AEP.util.ShortIdGenerator;

@Component
public class SolicitacaoMapper {
    public Solicitacao toModel(SolicitacaoCreateRequest request) {
        List<ItemSolicitacao> itens = request.itens().stream()
                .map(item -> new ItemSolicitacao(item.alimentoId(), item.quantidadeSolicitada(), item.unidade()))
                .toList();
        return new Solicitacao(ShortIdGenerator.next(), request.instituicaoId(), LocalDate.now(),
                request.destinatarioNome(), request.destinatarioDocumento(), request.destinatarioRua(),
                request.destinatarioCidade(), request.destinatarioEstado(), request.motivo(),
                request.observacao(), null, StatusSolicitacao.PENDENTE, itens);
    }

    public SolicitacaoResponse toResponse(Solicitacao solicitacao) {
        List<ItemSolicitacaoResponse> itens = solicitacao.getItens().stream()
                .map(item -> new ItemSolicitacaoResponse(item.getAlimentoId(), item.getQuantidadeSolicitada(), item.getUnidade()))
                .toList();
        return new SolicitacaoResponse(solicitacao.getId(), solicitacao.getInstituicaoId(),
                solicitacao.getDataSolicitacao(), solicitacao.getDestinatarioNome(),
                solicitacao.getDestinatarioDocumento(), solicitacao.getDestinatarioRua(),
                solicitacao.getDestinatarioCidade(), solicitacao.getDestinatarioEstado(),
                solicitacao.getMotivo(), solicitacao.getObservacao(), solicitacao.getDataEntrega(),
                solicitacao.getStatus(), itens);
    }
}
