package br.com.fiap.recicleglobal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class StatusController {
    @GetMapping("/")
    public Map<String, Object> inicio() {

        return Map.of(
                "projeto", "RecicleGlobal",
                "descricao", "Plataforma de doacoes solidarias",
                "status", "online",
                "versao", "1.0",
                "endpoints", List.of(
                        "/api/status",
                        "/api/usuarios",
                        "/api/instituicoes",
                        "/api/doacoes",
                        "/api/solicitacoes"
                )
        );
    }

    @GetMapping("/api/status")
    public Map<String, String> status() {

        return Map.of(
                "aplicacao", "RecicleGlobal",
                "status", "online"
        );
    }
}
