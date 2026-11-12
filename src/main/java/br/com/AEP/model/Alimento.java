package br.com.AEP.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "alimentos")
public class Alimento {

    @Id
    private String id;
    private String nome;
    private Double quantidade;
    private String unidade;
    private LocalDate dataValidade;
    private String origem;
    private StatusAlimento status;

    public Alimento(String id, String nome, Double quantidade, String unidade,
                    LocalDate dataValidade, String origem, StatusAlimento status) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.dataValidade = dataValidade;
        this.origem = origem;
        this.status = status;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Double getQuantidade() { return quantidade; }
    public void setQuantidade(Double quantidade) { this.quantidade = quantidade; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public StatusAlimento getStatus() { return status; }
    public void setStatus(StatusAlimento status) { this.status = status; }
}
