package br.com.fiap.recicleglobal.controller;

import br.com.fiap.recicleglobal.bo.DoacaoBO;
import br.com.fiap.recicleglobal.dto.DoacaoForm;
import br.com.fiap.recicleglobal.dto.DoacaoView;
import br.com.fiap.recicleglobal.model.CategoriaDoacao;
import br.com.fiap.recicleglobal.model.StatusDoacao;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doacoes")
public class DoacaoController {
    private final DoacaoBO bo;

    public DoacaoController(DoacaoBO bo) { this.bo = bo; }

    @GetMapping
    public List<DoacaoView> listar(@RequestParam(required = false) CategoriaDoacao categoria,
                                   @RequestParam(required = false) StatusDoacao status) {
        return bo.listar(categoria, status);
    }

    @GetMapping("/{id}")
    public DoacaoView buscar(@PathVariable Long id) { return bo.buscar(id); }

    @PostMapping
    public ResponseEntity<DoacaoView> cadastrar(@Valid @RequestBody DoacaoForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bo.cadastrar(form));
    }

    @PutMapping("/{id}")
    public DoacaoView atualizar(@PathVariable Long id, @Valid @RequestBody DoacaoForm form) {
        return bo.atualizar(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { bo.excluir(id); }
}
