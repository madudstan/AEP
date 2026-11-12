package br.com.AEP.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.AEP.dto.AlimentoCreateRequest;
import br.com.AEP.dto.AlimentoResponse;
import br.com.AEP.dto.AlimentoUpdateRequest;
import br.com.AEP.exception.AlimentoNotFoundException;
import br.com.AEP.mapper.AlimentoMapper;
import br.com.AEP.model.Alimento;
import br.com.AEP.model.StatusAlimento;
import br.com.AEP.repository.AlimentoRepository;

@ExtendWith(MockitoExtension.class)
class AlimentoServiceTest {

    @Mock private AlimentoRepository repository;
    @Mock private AlimentoMapper mapper;
    @InjectMocks private AlimentoService service;

    @Test
    void deveListarAlimentos() {
        Alimento arroz = alimento("1", "Arroz");
        Alimento feijao = alimento("2", "Feijão");
        AlimentoResponse arrozResponse = response(arroz);
        AlimentoResponse feijaoResponse = response(feijao);
        when(repository.findAll()).thenReturn(List.of(arroz, feijao));
        when(mapper.toResponse(arroz)).thenReturn(arrozResponse);
        when(mapper.toResponse(feijao)).thenReturn(feijaoResponse);

        assertEquals(List.of(arrozResponse, feijaoResponse), service.listar());
    }

    @Test
    void deveBuscarAlimentoExistente() {
        Alimento alimento = alimento("1", "Arroz");
        AlimentoResponse response = response(alimento);
        when(repository.findById("1")).thenReturn(Optional.of(alimento));
        when(mapper.toResponse(alimento)).thenReturn(response);

        assertEquals(response, service.buscarPorId("1"));
    }

    @Test
    void deveLancarExcecaoAoBuscarAlimentoInexistente() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        AlimentoNotFoundException exception = assertThrows(
                AlimentoNotFoundException.class, () -> service.buscarPorId("999"));

        assertEquals("Alimento não encontrado: 999", exception.getMessage());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveCriarAlimento() {
        AlimentoCreateRequest request = request("Arroz");
        Alimento novo = alimento(null, "Arroz");
        Alimento salvo = alimento("1", "Arroz");
        AlimentoResponse response = response(salvo);
        when(mapper.toModel(request)).thenReturn(novo);
        when(repository.save(novo)).thenReturn(salvo);
        when(mapper.toResponse(salvo)).thenReturn(response);

        assertEquals(response, service.criar(request));
        verify(repository).save(novo);
    }

    @Test
    void deveAtualizarAlimentoExistente() {
        Alimento existente = alimento("1", "Arroz");
        AlimentoUpdateRequest request = new AlimentoUpdateRequest(
                "Arroz Integral", 15.0, "kg", dataValidade(), "Mercado Central", StatusAlimento.DISPONIVEL);
        AlimentoResponse response = new AlimentoResponse(
                "1", "Arroz Integral", 15.0, "kg", dataValidade(), "Mercado Central", StatusAlimento.DISPONIVEL);
        when(repository.findById("1")).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);
        when(mapper.toResponse(existente)).thenReturn(response);

        assertEquals(response, service.atualizar("1", request));
        verify(mapper).updateModel(request, existente);
        verify(repository).save(existente);
    }

    @Test
    void deveLancarExcecaoAoAtualizarAlimentoInexistente() {
        when(repository.findById("999")).thenReturn(Optional.empty());
        AlimentoUpdateRequest request = new AlimentoUpdateRequest(
                "Arroz", 10.0, "kg", dataValidade(), "Mercado", StatusAlimento.DISPONIVEL);

        assertThrows(AlimentoNotFoundException.class, () -> service.atualizar("999", request));
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
        verifyNoInteractions(mapper);
    }

    @Test
    void deveExcluirAlimentoExistente() {
        Alimento alimento = alimento("1", "Arroz");
        when(repository.findById("1")).thenReturn(Optional.of(alimento));

        service.excluir("1");

        verify(repository).delete(alimento);
    }

    @Test
    void deveLancarExcecaoAoExcluirAlimentoInexistente() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        assertThrows(AlimentoNotFoundException.class, () -> service.excluir("999"));
        verify(repository, never()).delete(org.mockito.ArgumentMatchers.any());
    }

    private Alimento alimento(String id, String nome) {
        return new Alimento(id, nome, 20.0, "kg", dataValidade(),
                "Supermercado Esperança", StatusAlimento.DISPONIVEL);
    }

    private AlimentoCreateRequest request(String nome) {
        return new AlimentoCreateRequest(nome, 20.0, "kg", dataValidade(),
                "Supermercado Esperança", StatusAlimento.DISPONIVEL);
    }

    private AlimentoResponse response(Alimento alimento) {
        return new AlimentoResponse(alimento.getId(), alimento.getNome(), alimento.getQuantidade(),
                alimento.getUnidade(), alimento.getDataValidade(), alimento.getOrigem(), alimento.getStatus());
    }

    private LocalDate dataValidade() { return LocalDate.of(2026, 12, 31); }
}
