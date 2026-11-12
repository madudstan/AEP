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
import br.com.AEP.dto.EnderecoRequest;
import br.com.AEP.dto.InstituicaoCreateRequest;
import br.com.AEP.dto.InstituicaoResponse;
import br.com.AEP.dto.InstituicaoUpdateRequest;
import br.com.AEP.exception.InstituicaoNotFoundException;
import br.com.AEP.mapper.InstituicaoMapper;
import br.com.AEP.model.Endereco;
import br.com.AEP.model.Instituicao;
import br.com.AEP.repository.InstituicaoRepository;

@ExtendWith(MockitoExtension.class)
class InstituicaoServiceTest {
    @Mock InstituicaoRepository repository;
    @Mock InstituicaoMapper mapper;
    @InjectMocks InstituicaoService service;

    @Test
    void deveCriarInstituicao() {
        InstituicaoCreateRequest request = request();
        Instituicao model = instituicao(null);
        Instituicao saved = instituicao("1");
        InstituicaoResponse response = new InstituicaoResponse("1", "Instituto Esperança", "123", "a@a.com", "44999999999", null);
        when(mapper.toModel(request)).thenReturn(model);
        when(repository.save(model)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        assertEquals(response, service.criar(request));
        verify(repository).save(model);
    }

    @Test
    void deveListarInstituicoes() {
        Instituicao model = instituicao("1");
        InstituicaoResponse response = new InstituicaoResponse("1", "Instituto Esperança", "123", "a@a.com", "44999999999", null);
        when(repository.findAll()).thenReturn(List.of(model));
        when(mapper.toResponse(model)).thenReturn(response);
        assertEquals(List.of(response), service.listar());
    }

    @Test
    void deveLancarErroAoBuscarInstituicaoInexistente() {
        when(repository.findById("999")).thenReturn(Optional.empty());
        assertThrows(InstituicaoNotFoundException.class, () -> service.buscarPorId("999"));
    }

    @Test
    void deveAtualizarInstituicaoExistente() {
        Instituicao model = instituicao("1");
        InstituicaoResponse response = new InstituicaoResponse("1", "Instituto Novo", "123", "novo@a.com", "44", null);
        InstituicaoUpdateRequest request = new InstituicaoUpdateRequest("Instituto Novo", "123", "novo@a.com", "44",
                new EnderecoRequest("Rua B", "20", "Maringá", "PR", "87000"));
        when(repository.findById("1")).thenReturn(Optional.of(model));
        when(repository.save(model)).thenReturn(model);
        when(mapper.toResponse(model)).thenReturn(response);
        assertEquals(response, service.atualizar("1", request));
        verify(mapper).updateModel(request, model);
    }

    @Test
    void deveExcluirInstituicaoExistente() {
        Instituicao model = instituicao("1");
        when(repository.findById("1")).thenReturn(Optional.of(model));
        service.excluir("1");
        verify(repository).delete(model);
    }

    private InstituicaoCreateRequest request() {
        return new InstituicaoCreateRequest("Instituto Esperança", "123", "a@a.com", "44999999999",
                new EnderecoRequest("Rua A", "10", "Maringá", "PR", "87000000"));
    }

    private Instituicao instituicao(String id) {
        return new Instituicao(id, "Instituto Esperança", "123", "a@a.com", "44999999999",
                new Endereco("Rua A", "10", "Maringá", "PR", "87000000"));
    }
}
