package br.com.AEP.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.AEP.dto.AlimentoCreateRequest;
import br.com.AEP.dto.AlimentoResponse;
import br.com.AEP.dto.AlimentoUpdateRequest;
import br.com.AEP.exception.AlimentoNotFoundException;
import br.com.AEP.mapper.AlimentoMapper;
import br.com.AEP.model.Alimento;
import br.com.AEP.repository.AlimentoRepository;

@Service
public class AlimentoService {

    private final AlimentoRepository repository;
    private final AlimentoMapper mapper;

    public AlimentoService(AlimentoRepository repository, AlimentoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AlimentoResponse> listar() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    public AlimentoResponse buscarPorId(String id) {
        return mapper.toResponse(buscarModelPorId(id));
    }

    public AlimentoResponse criar(AlimentoCreateRequest request) {
        Alimento alimento = mapper.toModel(request);
        return mapper.toResponse(repository.save(alimento));
    }

    public AlimentoResponse atualizar(String id, AlimentoUpdateRequest request) {
        Alimento alimento = buscarModelPorId(id);
        mapper.updateModel(request, alimento);
        return mapper.toResponse(repository.save(alimento));
    }

    public void excluir(String id) {
        repository.delete(buscarModelPorId(id));
    }

    private Alimento buscarModelPorId(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new AlimentoNotFoundException(id));
    }
}
