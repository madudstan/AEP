package br.com.AEP.mapper;

import org.springframework.stereotype.Component;

import br.com.AEP.dto.AlimentoCreateRequest;
import br.com.AEP.dto.AlimentoResponse;
import br.com.AEP.dto.AlimentoUpdateRequest;
import br.com.AEP.model.Alimento;
import br.com.AEP.util.ShortIdGenerator;

@Component
public class AlimentoMapper {

    public Alimento toModel(AlimentoCreateRequest request) {
        return new Alimento(ShortIdGenerator.next(), request.nome(), request.quantidade(), request.unidade(),
                request.dataValidade(), request.origem(), request.status());
    }

    public void updateModel(AlimentoUpdateRequest request, Alimento alimento) {
        alimento.setNome(request.nome());
        alimento.setQuantidade(request.quantidade());
        alimento.setUnidade(request.unidade());
        alimento.setDataValidade(request.dataValidade());
        alimento.setOrigem(request.origem());
        alimento.setStatus(request.status());
    }

    public AlimentoResponse toResponse(Alimento alimento) {
        return new AlimentoResponse(
                alimento.getId(), alimento.getNome(), alimento.getQuantidade(), alimento.getUnidade(),
                alimento.getDataValidade(), alimento.getOrigem(), alimento.getStatus());
    }
}
