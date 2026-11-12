package br.com.AEP.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.AEP.dto.AlimentoCreateRequest;
import br.com.AEP.dto.AlimentoResponse;
import br.com.AEP.dto.AlimentoUpdateRequest;
import br.com.AEP.exception.AlimentoNotFoundException;
import br.com.AEP.model.StatusAlimento;
import br.com.AEP.service.AlimentoService;

@WebMvcTest(AlimentoController.class)
class AlimentoControllerTest {

    private static final String BASE_PATH = "/api/alimentos";
    private static final LocalDate VALIDADE = LocalDate.of(2026, 12, 31);

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private AlimentoService service;

    @Test
    void deveListarAlimentos() throws Exception {
        when(service.listar()).thenReturn(List.of(response("1", "Arroz"), response("2", "Feijão")));

        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").value("Arroz"))
                .andExpect(jsonPath("$[0].quantidade").value(20.0));
    }

    @Test
    void deveBuscarAlimentoExistente() throws Exception {
        when(service.buscarPorId("1")).thenReturn(response("1", "Arroz"));

        mockMvc.perform(get(BASE_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nome").value("Arroz"))
                .andExpect(jsonPath("$.dataValidade").value("2026-12-31"))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    void deveRetornarNotFoundAoBuscarAlimentoInexistente() throws Exception {
        when(service.buscarPorId("999")).thenThrow(new AlimentoNotFoundException("999"));

        mockMvc.perform(get(BASE_PATH + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Alimento não encontrado: 999"));
    }

    @Test
    void deveCriarAlimentoValido() throws Exception {
        AlimentoCreateRequest request = request();
        when(service.criar(request)).thenReturn(response("1", "Arroz"));

        mockMvc.perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost" + BASE_PATH + "/1"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nome").value("Arroz"));
    }

    @Test
    void deveRejeitarCriacaoInvalida() throws Exception {
        String json = """
                {
                  "nome": " ",
                  "quantidade": 0,
                  "unidade": "",
                  "dataValidade": null,
                  "origem": "",
                  "status": null
                }
                """;

        mockMvc.perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.nome").value("nome é obrigatório"))
                .andExpect(jsonPath("$.fieldErrors.quantidade").value("quantidade deve ser maior que zero"))
                .andExpect(jsonPath("$.fieldErrors.unidade").value("unidade é obrigatória"))
                .andExpect(jsonPath("$.fieldErrors.dataValidade").value("dataValidade é obrigatória"))
                .andExpect(jsonPath("$.fieldErrors.origem").value("origem é obrigatória"))
                .andExpect(jsonPath("$.fieldErrors.status").value("status é obrigatório"));

        verify(service, never()).criar(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deveAtualizarAlimentoValido() throws Exception {
        AlimentoUpdateRequest request = new AlimentoUpdateRequest(
                "Arroz Integral", 15.0, "kg", VALIDADE, "Mercado Central", StatusAlimento.DISPONIVEL);
        when(service.atualizar("1", request)).thenReturn(response("1", "Arroz Integral"));

        mockMvc.perform(put(BASE_PATH + "/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Arroz Integral"));
    }

    @Test
    void deveRetornarNotFoundAoAtualizarInexistente() throws Exception {
        AlimentoUpdateRequest request = requestUpdate();
        when(service.atualizar("999", request)).thenThrow(new AlimentoNotFoundException("999"));

        mockMvc.perform(put(BASE_PATH + "/999").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveExcluirAlimentoExistente() throws Exception {
        doNothing().when(service).excluir("1");

        mockMvc.perform(delete(BASE_PATH + "/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(service).excluir("1");
    }

    @Test
    void deveRetornarNotFoundAoExcluirInexistente() throws Exception {
        doThrow(new AlimentoNotFoundException("999")).when(service).excluir("999");

        mockMvc.perform(delete(BASE_PATH + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Alimento não encontrado: 999"));
    }

    private AlimentoCreateRequest request() {
        return new AlimentoCreateRequest("Arroz", 20.0, "kg", VALIDADE,
                "Supermercado Esperança", StatusAlimento.DISPONIVEL);
    }

    private AlimentoUpdateRequest requestUpdate() {
        return new AlimentoUpdateRequest("Arroz", 20.0, "kg", VALIDADE,
                "Supermercado Esperança", StatusAlimento.DISPONIVEL);
    }

    private AlimentoResponse response(String id, String nome) {
        return new AlimentoResponse(id, nome, 20.0, "kg", VALIDADE,
                "Supermercado Esperança", StatusAlimento.DISPONIVEL);
    }
}
