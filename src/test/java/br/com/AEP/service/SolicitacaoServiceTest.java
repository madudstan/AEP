package br.com.AEP.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.AEP.dto.ItemSolicitacaoRequest;
import br.com.AEP.dto.SolicitacaoCreateRequest;
import br.com.AEP.dto.SolicitacaoResponse;
import br.com.AEP.dto.StatusSolicitacaoRequest;
import br.com.AEP.exception.BusinessRuleException;
import br.com.AEP.exception.InstituicaoNotFoundException;
import br.com.AEP.mapper.SolicitacaoMapper;
import br.com.AEP.model.Alimento;
import br.com.AEP.model.ItemSolicitacao;
import br.com.AEP.model.Solicitacao;
import br.com.AEP.model.StatusAlimento;
import br.com.AEP.model.StatusSolicitacao;
import br.com.AEP.repository.AlimentoRepository;
import br.com.AEP.repository.InstituicaoRepository;
import br.com.AEP.repository.SolicitacaoRepository;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServiceTest {
    @Mock SolicitacaoRepository repository;
    @Mock InstituicaoRepository instituicaoRepository;
    @Mock AlimentoRepository alimentoRepository;
    @Mock SolicitacaoMapper mapper;
    @InjectMocks SolicitacaoService service;

    @Test
    void deveCriarSolicitacaoComItens() {
        SolicitacaoCreateRequest request = request();
        Solicitacao model = solicitacao("1", StatusSolicitacao.PENDENTE);
        SolicitacaoResponse response = new SolicitacaoResponse("1", "inst-1", null, null, StatusSolicitacao.PENDENTE, List.of());
        when(instituicaoRepository.existsById("inst-1")).thenReturn(true);
        when(alimentoRepository.findById("ali-1")).thenReturn(Optional.of(alimento()));
        when(mapper.toModel(request)).thenReturn(model);
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(response);

        assertEquals(response, service.criar(request));
        verify(repository).save(model);
    }

    @Test
    void deveRejeitarSolicitacaoDeInstituicaoInexistente() {
        when(instituicaoRepository.existsById("inst-1")).thenReturn(false);
        assertThrows(InstituicaoNotFoundException.class, () -> service.criar(request()));
    }

    @Test
    void deveRejeitarAlimentoIndisponivel() {
        when(instituicaoRepository.existsById("inst-1")).thenReturn(true);
        Alimento alimento = alimento();
        alimento.setStatus(StatusAlimento.INDISPONIVEL);
        when(alimentoRepository.findById("ali-1")).thenReturn(Optional.of(alimento));
        assertThrows(BusinessRuleException.class, () -> service.criar(request()));
    }

    @Test
    void deveAtualizarStatus() {
        Solicitacao solicitacao = solicitacao("1", StatusSolicitacao.PENDENTE);
        SolicitacaoResponse response = new SolicitacaoResponse("1", "inst-1", null, null, StatusSolicitacao.APROVADA, List.of());
        when(repository.findById("1")).thenReturn(Optional.of(solicitacao));
        when(repository.save(solicitacao)).thenReturn(solicitacao);
        when(mapper.toResponse(solicitacao)).thenReturn(response);
        assertEquals(response, service.atualizarStatus("1", new StatusSolicitacaoRequest(StatusSolicitacao.APROVADA)));
        assertEquals(StatusSolicitacao.APROVADA, solicitacao.getStatus());
    }

    private SolicitacaoCreateRequest request() {
        return new SolicitacaoCreateRequest("inst-1", "Urgente", List.of(new ItemSolicitacaoRequest("ali-1", 5.0, "kg")));
    }

    private Alimento alimento() {
        return new Alimento("ali-1", "Arroz", 20.0, "kg", null, "Mercado", StatusAlimento.DISPONIVEL);
    }

    private Solicitacao solicitacao(String id, StatusSolicitacao status) {
        return new Solicitacao(id, "inst-1", null, null, status,
                List.of(new ItemSolicitacao("ali-1", 5.0, "kg")));
    }
}
