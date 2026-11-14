package br.com.fiap.recicleglobal.controller;

import br.com.fiap.recicleglobal.bo.InstituicaoBO;
import br.com.fiap.recicleglobal.dto.InstituicaoForm;
import br.com.fiap.recicleglobal.dto.InstituicaoView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {
    private final InstituicaoBO bo;

    public InstituicaoController(InstituicaoBO bo) { this.bo = bo; }

    @GetMapping
    public List<InstituicaoView> listar() { return bo.listar(); }

    @GetMapping("/{id}")
    public InstituicaoView buscar(@PathVariable Long id) { return bo.buscar(id); }

    @PostMapping
    public ResponseEntity<InstituicaoView> cadastrar(@Valid @RequestBody InstituicaoForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bo.cadastrar(form));
    }

    @PutMapping("/{id}")
    public InstituicaoView atualizar(@PathVariable Long id, @Valid @RequestBody InstituicaoForm form) {
        return bo.atualizar(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { bo.excluir(id); }
}
