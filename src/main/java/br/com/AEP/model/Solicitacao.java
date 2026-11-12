package br.com.AEP.model;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "solicitacoes")
public class Solicitacao {
    @Id
    private String id;
    private String instituicaoId;
    private LocalDate dataSolicitacao;
    private String destinatarioNome;
    private String destinatarioDocumento;
    private String destinatarioRua;
    private String destinatarioCidade;
    private String destinatarioEstado;
    private String motivo;
    private String observacao;
    private LocalDate dataEntrega;
    private StatusSolicitacao status;
    private List<ItemSolicitacao> itens;

    public Solicitacao() {
    }

    public Solicitacao(String id, String instituicaoId, LocalDate dataSolicitacao,
                       String observacao, StatusSolicitacao status, List<ItemSolicitacao> itens) {
        this(id, instituicaoId, dataSolicitacao, null, null, null, null, null, null,
                observacao, null, status, itens);
    }

    public Solicitacao(String id, String instituicaoId, LocalDate dataSolicitacao,
                       String destinatarioNome, String destinatarioDocumento,
                       String destinatarioRua, String destinatarioCidade, String destinatarioEstado,
                       String motivo, String observacao, LocalDate dataEntrega,
                       StatusSolicitacao status, List<ItemSolicitacao> itens) {
        this.id = id;
        this.instituicaoId = instituicaoId;
        this.dataSolicitacao = dataSolicitacao;
        this.destinatarioNome = destinatarioNome;
        this.destinatarioDocumento = destinatarioDocumento;
        this.destinatarioRua = destinatarioRua;
        this.destinatarioCidade = destinatarioCidade;
        this.destinatarioEstado = destinatarioEstado;
        this.motivo = motivo;
        this.observacao = observacao;
        this.dataEntrega = dataEntrega;
        this.status = status;
        this.itens = itens;
    }

    public String getId() { return id; }
    public String getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(String instituicaoId) { this.instituicaoId = instituicaoId; }
    public LocalDate getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDate dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }
    public String getDestinatarioNome() { return destinatarioNome; }
    public void setDestinatarioNome(String destinatarioNome) { this.destinatarioNome = destinatarioNome; }
    public String getDestinatarioDocumento() { return destinatarioDocumento; }
    public void setDestinatarioDocumento(String destinatarioDocumento) { this.destinatarioDocumento = destinatarioDocumento; }
    public String getDestinatarioRua() { return destinatarioRua; }
    public void setDestinatarioRua(String destinatarioRua) { this.destinatarioRua = destinatarioRua; }
    public String getDestinatarioCidade() { return destinatarioCidade; }
    public void setDestinatarioCidade(String destinatarioCidade) { this.destinatarioCidade = destinatarioCidade; }
    public String getDestinatarioEstado() { return destinatarioEstado; }
    public void setDestinatarioEstado(String destinatarioEstado) { this.destinatarioEstado = destinatarioEstado; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
    public LocalDate getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }
    public StatusSolicitacao getStatus() { return status; }
    public void setStatus(StatusSolicitacao status) { this.status = status; }
    public List<ItemSolicitacao> getItens() { return itens; }
    public void setItens(List<ItemSolicitacao> itens) { this.itens = itens; }
}
