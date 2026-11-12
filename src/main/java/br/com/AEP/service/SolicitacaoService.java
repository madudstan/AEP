package br.com.AEP.service;

import java.util.List;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import br.com.AEP.dto.SolicitacaoCreateRequest;
import br.com.AEP.dto.SolicitacaoResponse;
import br.com.AEP.dto.StatusSolicitacaoRequest;
import br.com.AEP.exception.BusinessRuleException;
import br.com.AEP.exception.InstituicaoNotFoundException;
import br.com.AEP.exception.SolicitacaoNotFoundException;
import br.com.AEP.mapper.SolicitacaoMapper;
import br.com.AEP.model.Alimento;
import br.com.AEP.model.Solicitacao;
import br.com.AEP.model.StatusAlimento;
import br.com.AEP.repository.AlimentoRepository;
import br.com.AEP.repository.InstituicaoRepository;
import br.com.AEP.repository.SolicitacaoRepository;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository repository;
    private final InstituicaoRepository instituicaoRepository;
    private final AlimentoRepository alimentoRepository;
    private final SolicitacaoMapper mapper;

    public SolicitacaoService(SolicitacaoRepository repository, InstituicaoRepository instituicaoRepository,
                              AlimentoRepository alimentoRepository, SolicitacaoMapper mapper) {
        this.repository = repository;
        this.instituicaoRepository = instituicaoRepository;
        this.alimentoRepository = alimentoRepository;
        this.mapper = mapper;
    }

    public List<SolicitacaoResponse> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public List<SolicitacaoResponse> listarPorInstituicao(String instituicaoId) {
        validarInstituicao(instituicaoId);
        return repository.findByInstituicaoId(instituicaoId).stream().map(mapper::toResponse).toList();
    }

    public SolicitacaoResponse buscarPorId(String id) {
        return mapper.toResponse(buscarModelPorId(id));
    }

    public SolicitacaoResponse criar(SolicitacaoCreateRequest request) {
        validarInstituicao(request.instituicaoId());
        request.itens().forEach(item -> {
            Alimento alimento = alimentoRepository.findById(item.alimentoId())
                    .orElseThrow(() -> new BusinessRuleException("Alimento não encontrado: " + item.alimentoId()));
            if (alimento.getStatus() != StatusAlimento.DISPONIVEL) {
                throw new BusinessRuleException("Alimento indisponível para solicitação: " + item.alimentoId());
            }
            if (!alimento.getUnidade().equalsIgnoreCase(item.unidade())) {
                throw new BusinessRuleException("A unidade do item não corresponde ao alimento: " + item.alimentoId());
            }
        });
        return mapper.toResponse(repository.save(mapper.toModel(request)));
    }

    public SolicitacaoResponse atualizarStatus(String id, StatusSolicitacaoRequest request) {
        Solicitacao solicitacao = buscarModelPorId(id);
        if (solicitacao.getStatus() == br.com.AEP.model.StatusSolicitacao.ATENDIDA
                || solicitacao.getStatus() == br.com.AEP.model.StatusSolicitacao.CANCELADA) {
            throw new BusinessRuleException("Não é possível alterar uma solicitação encerrada");
        }
        solicitacao.setStatus(request.status());
        solicitacao.setDataEntrega(request.status() == br.com.AEP.model.StatusSolicitacao.ATENDIDA
                ? LocalDate.now() : null);
        return mapper.toResponse(repository.save(solicitacao));
    }

    public void excluir(String id) {
        repository.delete(buscarModelPorId(id));
    }

    private void validarInstituicao(String id) {
        if (!instituicaoRepository.existsById(id)) {
            throw new InstituicaoNotFoundException(id);
        }
    }

    private Solicitacao buscarModelPorId(String id) {
        return repository.findById(id).orElseThrow(() -> new SolicitacaoNotFoundException(id));
    }
}
