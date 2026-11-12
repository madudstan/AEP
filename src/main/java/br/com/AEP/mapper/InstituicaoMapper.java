package br.com.AEP.mapper;

import org.springframework.stereotype.Component;
import br.com.AEP.dto.EnderecoRequest;
import br.com.AEP.dto.EnderecoResponse;
import br.com.AEP.dto.InstituicaoCreateRequest;
import br.com.AEP.dto.InstituicaoResponse;
import br.com.AEP.dto.InstituicaoUpdateRequest;
import br.com.AEP.model.Endereco;
import br.com.AEP.model.Instituicao;
import br.com.AEP.util.ShortIdGenerator;

@Component
public class InstituicaoMapper {
    public Instituicao toModel(InstituicaoCreateRequest request) {
        return new Instituicao(ShortIdGenerator.next(), request.nome(), request.documento(), request.email(),
                request.telefone(), toModel(request.endereco()));
    }

    public void updateModel(InstituicaoUpdateRequest request, Instituicao instituicao) {
        instituicao.setNome(request.nome());
        instituicao.setDocumento(request.documento());
        instituicao.setEmail(request.email());
        instituicao.setTelefone(request.telefone());
        instituicao.setEndereco(toModel(request.endereco()));
    }

    public InstituicaoResponse toResponse(Instituicao instituicao) {
        return new InstituicaoResponse(instituicao.getId(), instituicao.getNome(), instituicao.getDocumento(),
                instituicao.getEmail(), instituicao.getTelefone(), toResponse(instituicao.getEndereco()));
    }

    private Endereco toModel(EnderecoRequest request) {
        return new Endereco(request.rua(), request.numero(), request.cidade(), request.estado(), request.cep());
    }

    private EnderecoResponse toResponse(Endereco endereco) {
        return new EnderecoResponse(endereco.getRua(), endereco.getNumero(), endereco.getCidade(),
                endereco.getEstado(), endereco.getCep());
    }
}
