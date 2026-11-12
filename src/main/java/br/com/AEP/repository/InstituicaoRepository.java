package br.com.AEP.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.com.AEP.model.Instituicao;

public interface InstituicaoRepository extends MongoRepository<Instituicao, String> {
}
