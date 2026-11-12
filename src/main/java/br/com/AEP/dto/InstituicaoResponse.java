package br.com.AEP.dto;

public record InstituicaoResponse(String id, String nome, String documento, String email,
                                  String telefone, EnderecoResponse endereco) {
}
