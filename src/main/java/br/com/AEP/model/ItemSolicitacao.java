package br.com.AEP.model;

public class ItemSolicitacao {
    private String alimentoId;
    private Double quantidadeSolicitada;
    private String unidade;

    public ItemSolicitacao() {
    }

    public ItemSolicitacao(String alimentoId, Double quantidadeSolicitada, String unidade) {
        this.alimentoId = alimentoId;
        this.quantidadeSolicitada = quantidadeSolicitada;
        this.unidade = unidade;
    }

    public String getAlimentoId() { return alimentoId; }
    public void setAlimentoId(String alimentoId) { this.alimentoId = alimentoId; }
    public Double getQuantidadeSolicitada() { return quantidadeSolicitada; }
    public void setQuantidadeSolicitada(Double quantidadeSolicitada) { this.quantidadeSolicitada = quantidadeSolicitada; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
}
