package br.com.AEP.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.AEP.model.Alimento;
import br.com.AEP.model.StatusAlimento;
import br.com.AEP.repository.AlimentoRepository;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AlimentoApiIT {

    private static final String BASE_PATH = "/api/alimentos";
    private static final LocalDate VALIDADE = LocalDate.of(2026, 12, 31);

    @Container
    static final MongoDBContainer MONGO = new MongoDBContainer(DockerImageName.parse("mongo:7.0"));

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);
    }

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AlimentoRepository repository;

    @BeforeEach
    void limparBanco() { repository.deleteAll(); }

    @Test
    void deveInserirDocumentoPelaApi() throws Exception {
        String json = json("Arroz", 20.0, "kg", "Supermercado Esperança", "DISPONIVEL");

        String responseBody = mockMvc.perform(post(BASE_PATH).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Arroz"))
                .andExpect(jsonPath("$.quantidade").value(20.0))
                .andReturn().getResponse().getContentAsString();

        JsonNode response = objectMapper.readTree(responseBody);
        assertNotNull(repository.findById(response.get("id").asText()).orElse(null));
    }

    @Test
    void deveRecuperarDocumentoPelaApi() throws Exception {
        repository.save(alimento("1", "Arroz"));

        mockMvc.perform(get(BASE_PATH + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nome").value("Arroz"))
                .andExpect(jsonPath("$.dataValidade").value("2026-12-31"))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    void deveListarDocumentos() throws Exception {
        repository.saveAll(List.of(alimento("1", "Arroz"), alimento("2", "Feijão")));

        mockMvc.perform(get(BASE_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome").exists())
                .andExpect(jsonPath("$[0].quantidade").exists())
                .andExpect(jsonPath("$[0].status").exists());
    }

    @Test
    void deveAtualizarDocumentoPelaApiPreservandoId() throws Exception {
        repository.save(alimento("1", "Arroz"));
        String json = json("Arroz Integral", 15.0, "kg", "Mercado Central", "DISPONIVEL");

        mockMvc.perform(put(BASE_PATH + "/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.nome").value("Arroz Integral"));

        assertEquals("Arroz Integral", repository.findById("1").orElseThrow().getNome());
    }

    @Test
    void deveExcluirDocumentoPelaApi() throws Exception {
        repository.save(alimento("1", "Arroz"));

        mockMvc.perform(delete(BASE_PATH + "/1"))
                .andExpect(status().isNoContent());

        assertFalse(repository.existsById("1"));
    }

    @Test
    void deveRetornarNotFoundParaIdInexistente() throws Exception {
        mockMvc.perform(get(BASE_PATH + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Alimento não encontrado: 999"));
    }

    private Alimento alimento(String id, String nome) {
        return new Alimento(id, nome, 20.0, "kg", VALIDADE,
                "Supermercado Esperança", StatusAlimento.DISPONIVEL);
    }

    private String json(String nome, double quantidade, String unidade, String origem, String status) {
        return """
                {"nome":"%s","quantidade":%s,"unidade":"%s","dataValidade":"%s","origem":"%s","status":"%s"}
                """.formatted(nome, quantidade, unidade, VALIDADE, origem, status);
    }
}
