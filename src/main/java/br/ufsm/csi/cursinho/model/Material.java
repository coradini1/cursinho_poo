package br.ufsm.csi.cursinho.model;

import javax.persistence.*;

@Entity
@Table(name = "Materiais")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String descricao;
    private String caminhoArquivo;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getCursoId() {
        if (curso != null) {
            return curso.getId();
        }
        return 0;
    }

    public void setCursoId(int cursoId) {
        if (curso == null) {
            curso = new Curso();
        }
        curso.setId(cursoId);
    }
}
