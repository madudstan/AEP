package br.com.AEP.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.com.AEP.dto.InstituicaoCreateRequest;
import br.com.AEP.dto.InstituicaoResponse;
import br.com.AEP.dto.InstituicaoUpdateRequest;
import br.com.AEP.service.InstituicaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {
    private final InstituicaoService service;

    public InstituicaoController(InstituicaoService service) { this.service = service; }

    @GetMapping
    public List<InstituicaoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public InstituicaoResponse buscarPorId(@PathVariable String id) { return service.buscarPorId(id); }

    @PostMapping
    public ResponseEntity<InstituicaoResponse> criar(@Valid @RequestBody InstituicaoCreateRequest request) {
        InstituicaoResponse response = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public InstituicaoResponse atualizar(@PathVariable String id,
                                         @Valid @RequestBody InstituicaoUpdateRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
