package br.com.AEP.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.AEP.model.Solicitacao;

public interface SolicitacaoRepository extends MongoRepository<Solicitacao, String> {
    List<Solicitacao> findByInstituicaoId(String instituicaoId);
}
