package br.com.AEP.exception;

public class AlimentoNotFoundException extends RuntimeException {
    public AlimentoNotFoundException(String id) {
        super("Alimento não encontrado: " + id);
    }
}