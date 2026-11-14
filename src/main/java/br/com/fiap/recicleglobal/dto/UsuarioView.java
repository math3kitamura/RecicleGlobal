package br.com.fiap.recicleglobal.dto;

import br.com.fiap.recicleglobal.model.Usuario;

public record UsuarioView(Long id, String nome, String email) {
    public static UsuarioView from(Usuario usuario) {
        return new UsuarioView(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
