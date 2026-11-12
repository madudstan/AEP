package br.com.AEP.exception;

public class InstituicaoNotFoundException extends RuntimeException {
    public InstituicaoNotFoundException(String id) {
        super("Instituição não encontrada: " + id);
    }
}
