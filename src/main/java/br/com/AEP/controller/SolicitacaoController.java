package br.com.AEP.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.AEP.dto.SolicitacaoCreateRequest;
import br.com.AEP.dto.SolicitacaoResponse;
import br.com.AEP.dto.StatusSolicitacaoRequest;
import br.com.AEP.service.SolicitacaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {
    private final SolicitacaoService service;

    public SolicitacaoController(SolicitacaoService service) { this.service = service; }

    @GetMapping
    public List<SolicitacaoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public SolicitacaoResponse buscarPorId(@PathVariable String id) { return service.buscarPorId(id); }

    @GetMapping("/instituicao/{instituicaoId}")
    public List<SolicitacaoResponse> listarPorInstituicao(@PathVariable String instituicaoId) {
        return service.listarPorInstituicao(instituicaoId);
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponse> criar(@Valid @RequestBody SolicitacaoCreateRequest request) {
        SolicitacaoResponse response = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{id}/status")
    public SolicitacaoResponse atualizarStatus(@PathVariable String id,
                                               @Valid @RequestBody StatusSolicitacaoRequest request) {
        return service.atualizarStatus(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
