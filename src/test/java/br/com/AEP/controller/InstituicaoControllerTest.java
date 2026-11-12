package br.com.AEP.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import br.com.AEP.dto.EnderecoResponse;
import br.com.AEP.dto.InstituicaoCreateRequest;
import br.com.AEP.dto.InstituicaoResponse;
import br.com.AEP.service.InstituicaoService;

@WebMvcTest(InstituicaoController.class)
class InstituicaoControllerTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean InstituicaoService service;

    @Test
    void deveCriarInstituicaoComEndereco() throws Exception {
        when(service.criar(any(InstituicaoCreateRequest.class))).thenReturn(
                new InstituicaoResponse("1", "Instituto Esperança", "123", "a@a.com", "44999999999",
                        new EnderecoResponse("Rua A", "10", "Maringá", "PR", "87000000")));
        mockMvc.perform(post("/api/instituicoes").contentType(MediaType.APPLICATION_JSON).content("""
                {"nome":"Instituto Esperança","documento":"123","email":"a@a.com","telefone":"44999999999",
                 "endereco":{"rua":"Rua A","numero":"10","cidade":"Maringá","estado":"PR","cep":"87000000"}}
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.endereco.cidade").value("Maringá"));
    }
}
