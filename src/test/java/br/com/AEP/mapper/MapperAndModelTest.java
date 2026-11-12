package br.com.AEP.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import br.com.AEP.dto.EnderecoRequest;
import br.com.AEP.dto.InstituicaoCreateRequest;
import br.com.AEP.dto.InstituicaoUpdateRequest;
import br.com.AEP.dto.ItemSolicitacaoRequest;
import br.com.AEP.dto.SolicitacaoCreateRequest;
import br.com.AEP.model.Alimento;
import br.com.AEP.model.Endereco;
import br.com.AEP.model.Instituicao;
import br.com.AEP.model.ItemSolicitacao;
import br.com.AEP.model.Solicitacao;
import br.com.AEP.model.StatusAlimento;
import br.com.AEP.model.StatusSolicitacao;

class MapperAndModelTest {
    @Test
    void deveMapearInstituicaoComEnderecoAninhado() {
        InstituicaoMapper mapper = new InstituicaoMapper();
        InstituicaoCreateRequest request = new InstituicaoCreateRequest("Instituto", "123", "a@a.com", "4499",
                new EnderecoRequest("Rua A", "10", "Maringá", "PR", "87000"));
        Instituicao model = mapper.toModel(request);
        assertEquals("Rua A", model.getEndereco().getRua());
        assertEquals("Instituto", mapper.toResponse(model).nome());
        mapper.updateModel(new InstituicaoUpdateRequest("Instituto", "123", "a@a.com", "4499",
                new EnderecoRequest("Rua A", "10", "Maringá", "PR", "87000")), model);
        assertEquals("PR", model.getEndereco().getEstado());
    }

    @Test
    void deveMapearSolicitacaoComListaDeItens() {
        SolicitacaoMapper mapper = new SolicitacaoMapper();
        SolicitacaoCreateRequest request = new SolicitacaoCreateRequest("inst-1", "Urgente",
                List.of(new ItemSolicitacaoRequest("ali-1", 5.0, "kg")));
        Solicitacao model = mapper.toModel(request);
        assertEquals(StatusSolicitacao.PENDENTE, model.getStatus());
        assertEquals("ali-1", mapper.toResponse(model).itens().get(0).alimentoId());
    }

    @Test
    void deveExercitarModelosEEnums() {
        Endereco endereco = new Endereco();
        endereco.setRua("Rua"); endereco.setNumero("1"); endereco.setCidade("Maringá");
        endereco.setEstado("PR"); endereco.setCep("87000");
        assertEquals("87000", endereco.getCep());

        Instituicao instituicao = new Instituicao();
        instituicao.setNome("Instituto"); instituicao.setDocumento("1"); instituicao.setEmail("a@a.com");
        instituicao.setTelefone("44"); instituicao.setEndereco(endereco);
        assertEquals("Instituto", instituicao.getNome());

        ItemSolicitacao item = new ItemSolicitacao();
        item.setAlimentoId("ali"); item.setQuantidadeSolicitada(2.0); item.setUnidade("kg");
        assertEquals(2.0, item.getQuantidadeSolicitada());

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setInstituicaoId("inst"); solicitacao.setDataSolicitacao(LocalDate.now());
        solicitacao.setObservacao("obs"); solicitacao.setStatus(StatusSolicitacao.APROVADA);
        solicitacao.setItens(List.of(item));
        assertEquals("inst", solicitacao.getInstituicaoId());

        Alimento alimento = new Alimento("1", "Arroz", 10.0, "kg", LocalDate.now(), "Mercado", StatusAlimento.DISPONIVEL);
        alimento.setNome("Arroz Integral"); alimento.setQuantidade(8.0); alimento.setUnidade("kg");
        alimento.setDataValidade(LocalDate.now()); alimento.setOrigem("Mercado");
        alimento.setStatus(StatusAlimento.INDISPONIVEL);
        assertEquals(StatusAlimento.INDISPONIVEL, alimento.getStatus());
    }
}
