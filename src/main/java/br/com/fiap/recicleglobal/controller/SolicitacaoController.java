package br.com.fiap.recicleglobal.controller;

import br.com.fiap.recicleglobal.bo.SolicitacaoBO;
import br.com.fiap.recicleglobal.dto.SolicitacaoForm;
import br.com.fiap.recicleglobal.dto.SolicitacaoView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {
    private final SolicitacaoBO bo;

    public SolicitacaoController(SolicitacaoBO bo) { this.bo = bo; }

    @GetMapping
    public List<SolicitacaoView> listar(@RequestParam(required = false) Long doacaoId) {
        return bo.listar(doacaoId);
    }

    @PostMapping
    public ResponseEntity<SolicitacaoView> criar(@Valid @RequestBody SolicitacaoForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bo.criar(form));
    }

    @PutMapping("/{id}/aceitar")
    public SolicitacaoView aceitar(@PathVariable Long id) { return bo.aceitar(id); }

    @PutMapping("/{id}/concluir")
    public SolicitacaoView concluir(@PathVariable Long id) { return bo.concluir(id); }

    @PutMapping("/{id}/cancelar")
    public SolicitacaoView cancelar(@PathVariable Long id) { return bo.cancelar(id); }
}
