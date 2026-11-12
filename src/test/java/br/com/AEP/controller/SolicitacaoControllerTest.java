package br.com.AEP.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import br.com.AEP.dto.ItemSolicitacaoResponse;
import br.com.AEP.dto.SolicitacaoCreateRequest;
import br.com.AEP.dto.SolicitacaoResponse;
import br.com.AEP.model.StatusSolicitacao;
import br.com.AEP.service.SolicitacaoService;

@WebMvcTest(SolicitacaoController.class)
class SolicitacaoControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean SolicitacaoService service;

    @Test
    void deveCriarSolicitacaoComListaDeItens() throws Exception {
        when(service.criar(any(SolicitacaoCreateRequest.class))).thenReturn(
                new SolicitacaoResponse("1", "inst-1", null, "Urgente", StatusSolicitacao.PENDENTE,
                        List.of(new ItemSolicitacaoResponse("ali-1", 5.0, "kg"))));
        mockMvc.perform(post("/api/solicitacoes").contentType(MediaType.APPLICATION_JSON).content("""
                {"instituicaoId":"inst-1","observacao":"Urgente",
                 "itens":[{"alimentoId":"ali-1","quantidadeSolicitada":5,"unidade":"kg"}]}
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.itens[0].alimentoId").value("ali-1"));
    }
}
