package br.com.AEP.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.AEP.model.Alimento;

public interface AlimentoRepository extends MongoRepository<Alimento, String> {
}
