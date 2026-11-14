package br.com.fiap.recicleglobal.controller;

import br.com.fiap.recicleglobal.bo.UsuarioBO;
import br.com.fiap.recicleglobal.dto.UsuarioForm;
import br.com.fiap.recicleglobal.dto.UsuarioView;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioBO bo;

    public UsuarioController(UsuarioBO bo) { this.bo = bo; }

    @GetMapping
    public List<UsuarioView> listar() { return bo.listar(); }

    @GetMapping("/{id}")
    public UsuarioView buscar(@PathVariable Long id) { return bo.buscar(id); }

    @PostMapping
    public ResponseEntity<UsuarioView> cadastrar(@Valid @RequestBody UsuarioForm form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bo.cadastrar(form));
    }

    @PutMapping("/{id}")
    public UsuarioView atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioForm form) {
        return bo.atualizar(id, form);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { bo.excluir(id); }
}
