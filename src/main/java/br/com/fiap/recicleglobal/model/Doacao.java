package br.com.fiap.recicleglobal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "RG_DOACAO")
public class Doacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doacao_seq")
    @SequenceGenerator(name = "doacao_seq", sequenceName = "RG_DOACAO_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORIA", nullable = false, length = 20)
    private CategoriaDoacao categoria;

    @Column(name = "TITULO", nullable = false, length = 120)
    private String titulo;

    @Column(name = "DESCRICAO", length = 500)
    private String descricao;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "DATA_VALIDADE")
    private LocalDate dataValidade;

    @Column(name = "BAIRRO", nullable = false, length = 100)
    private String bairro;

    @Column(name = "LATITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private StatusDoacao status;

    @Column(name = "CRIADA_EM", nullable = false)
    private LocalDateTime criadaEm;

    protected Doacao() {}

    public Doacao(Usuario usuario) {
        this.usuario = usuario;
        this.status = StatusDoacao.DISPONIVEL;
        this.criadaEm = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public CategoriaDoacao getCategoria() { return categoria; }
    public String getTitulo() { return titulo; }
    public String getDescricao() { return descricao; }
    public Integer getQuantidade() { return quantidade; }
    public LocalDate getDataValidade() { return dataValidade; }
    public String getBairro() { return bairro; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public StatusDoacao getStatus() { return status; }
    public LocalDateTime getCriadaEm() { return criadaEm; }

    public void setStatus(StatusDoacao status) { this.status = status; }
    public void atualizar(CategoriaDoacao categoria, String titulo, String descricao, Integer quantidade,
                          LocalDate dataValidade, String bairro, BigDecimal latitude, BigDecimal longitude) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.dataValidade = dataValidade;
        this.bairro = bairro;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
