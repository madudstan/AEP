package br.com.AEP.exception;

public class SolicitacaoNotFoundException extends RuntimeException {
    public SolicitacaoNotFoundException(String id) {
        super("Solicitação não encontrada: " + id);
    }
}
