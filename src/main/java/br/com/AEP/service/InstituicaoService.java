package br.com.AEP.service;

import java.util.List;
import org.springframework.stereotype.Service;
import br.com.AEP.dto.InstituicaoCreateRequest;
import br.com.AEP.dto.InstituicaoResponse;
import br.com.AEP.dto.InstituicaoUpdateRequest;
import br.com.AEP.exception.InstituicaoNotFoundException;
import br.com.AEP.mapper.InstituicaoMapper;
import br.com.AEP.model.Instituicao;
import br.com.AEP.repository.InstituicaoRepository;

@Service
public class InstituicaoService {
    private final InstituicaoRepository repository;
    private final InstituicaoMapper mapper;

    public InstituicaoService(InstituicaoRepository repository, InstituicaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<InstituicaoResponse> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public InstituicaoResponse buscarPorId(String id) {
        return mapper.toResponse(buscarModelPorId(id));
    }

    public InstituicaoResponse criar(InstituicaoCreateRequest request) {
        return mapper.toResponse(repository.save(mapper.toModel(request)));
    }

    public InstituicaoResponse atualizar(String id, InstituicaoUpdateRequest request) {
        Instituicao instituicao = buscarModelPorId(id);
        mapper.updateModel(request, instituicao);
        return mapper.toResponse(repository.save(instituicao));
    }

    public void excluir(String id) {
        repository.delete(buscarModelPorId(id));
    }

    private Instituicao buscarModelPorId(String id) {
        return repository.findById(id).orElseThrow(() -> new InstituicaoNotFoundException(id));
    }
}
