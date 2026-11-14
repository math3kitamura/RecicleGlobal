package br.com.fiap.recicleglobal.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "RG_INSTITUICAO")
public class Instituicao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "instituicao_seq")
    @SequenceGenerator(name = "instituicao_seq", sequenceName = "RG_INSTITUICAO_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 120)
    private String nome;

    @Column(name = "DESCRICAO", length = 500)
    private String descricao;

    @Column(name = "BAIRRO", nullable = false, length = 100)
    private String bairro;

    @Column(name = "LATITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    protected Instituicao() {}

    public Instituicao(String nome, String descricao, String bairro, BigDecimal latitude, BigDecimal longitude) {
        this.nome = nome;
        this.descricao = descricao;
        this.bairro = bairro;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getBairro() { return bairro; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public void atualizar(String nome, String descricao, String bairro, BigDecimal latitude, BigDecimal longitude) {
        this.nome = nome;
        this.descricao = descricao;
        this.bairro = bairro;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
