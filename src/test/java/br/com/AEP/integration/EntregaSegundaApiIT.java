package br.com.AEP.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import br.com.AEP.model.Alimento;
import br.com.AEP.model.Endereco;
import br.com.AEP.model.Instituicao;
import br.com.AEP.model.StatusAlimento;
import br.com.AEP.repository.AlimentoRepository;
import br.com.AEP.repository.InstituicaoRepository;
import br.com.AEP.repository.SolicitacaoRepository;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EntregaSegundaApiIT {
    @Container
    static final MongoDBContainer MONGO = new MongoDBContainer(DockerImageName.parse("mongo:7.0"));

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGO::getReplicaSetUrl);
    }

    @Autowired MockMvc mockMvc;
    @Autowired AlimentoRepository alimentoRepository;
    @Autowired InstituicaoRepository instituicaoRepository;
    @Autowired SolicitacaoRepository solicitacaoRepository;

    @BeforeEach
    void limparColecoes() {
        alimentoRepository.deleteAll();
        instituicaoRepository.deleteAll();
        solicitacaoRepository.deleteAll();
    }

    @Test
    void deveCriarSolicitacaoRelacionandoInstituicaoEAlimentoComListaAninhada() throws Exception {
        Alimento alimento = alimentoRepository.save(new Alimento(null, "Arroz", 20.0, "kg",
                LocalDate.now().plusDays(10), "Mercado", StatusAlimento.DISPONIVEL));
        Instituicao instituicao = instituicaoRepository.save(new Instituicao(null, "Instituto", "123",
                "a@a.com", "4499", new Endereco("Rua A", "10", "Maringá", "PR", "87000")));

        String body = """
                {"instituicaoId":"%s","observacao":"Urgente","itens":[{"alimentoId":"%s","quantidadeSolicitada":5,"unidade":"kg"}]}
                """.formatted(instituicao.getId(), alimento.getId());

        mockMvc.perform(post("/api/solicitacoes").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.instituicaoId").value(instituicao.getId()))
                .andExpect(jsonPath("$.itens", hasSize(1)))
                .andExpect(jsonPath("$.itens[0].alimentoId").value(alimento.getId()));

        assertEquals(1, solicitacaoRepository.findByInstituicaoId(instituicao.getId()).size());
    }
}
