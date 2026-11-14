package br.com.fiap.recicleglobal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "RG_SOLICITACAO")
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitacao_seq")
    @SequenceGenerator(name = "solicitacao_seq", sequenceName = "RG_SOLICITACAO_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DOACAO_ID", nullable = false)
    private Doacao doacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INTERESSADO_ID", nullable = false)
    private Usuario interessado;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private StatusSolicitacao status;

    @Column(name = "CRIADA_EM", nullable = false)
    private LocalDateTime criadaEm;

    protected Solicitacao() {}

    public Solicitacao(Doacao doacao, Usuario interessado) {
        this.doacao = doacao;
        this.interessado = interessado;
        this.status = StatusSolicitacao.PENDENTE;
        this.criadaEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Doacao getDoacao() { return doacao; }
    public Usuario getInteressado() { return interessado; }
    public StatusSolicitacao getStatus() { return status; }
    public LocalDateTime getCriadaEm() { return criadaEm; }
    public void setStatus(StatusSolicitacao status) { this.status = status; }
}
