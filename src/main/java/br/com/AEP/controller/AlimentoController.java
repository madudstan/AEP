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

import br.com.AEP.dto.AlimentoCreateRequest;
import br.com.AEP.dto.AlimentoResponse;
import br.com.AEP.dto.AlimentoUpdateRequest;
import br.com.AEP.service.AlimentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final AlimentoService service;

    public AlimentoController(AlimentoService service) { this.service = service; }

    @GetMapping
    public List<AlimentoResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public AlimentoResponse buscarPorId(@PathVariable String id) { return service.buscarPorId(id); }

    @PostMapping
    public ResponseEntity<AlimentoResponse> criar(@Valid @RequestBody AlimentoCreateRequest request) {
        AlimentoResponse response = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public AlimentoResponse atualizar(@PathVariable String id,
                                      @Valid @RequestBody AlimentoUpdateRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
