package br.ufsm.csi.cursinho.DAO;

import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MaterialDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MaterialDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createMaterial(Material material) {
        String sql = "INSERT INTO Materiais (nome, descricao, caminho_arquivo, curso_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, material.getNome(), material.getDescricao(), material.getCaminhoArquivo(), material.getCursoId());
    }

    public List<Material> getMateriaisByCursoId(int cursoId) {
        String sql = "SELECT * FROM Materiais WHERE curso_id = ?";
        return jdbcTemplate.query(sql, this::mapMaterial, cursoId);
    }

    public List<Material> getMateriais() {
        String sql = "SELECT * FROM Materiais";
        return jdbcTemplate.query(sql, this::mapMaterial);
    }

    public Material getMaterialById(int materialId) {
        String sql = "SELECT * FROM Materiais WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapMaterial, materialId);
    }

    public void updateMaterial(Long id, Material material) {
        String sql = "UPDATE Materiais SET nome = ?, descricao = ?, caminho_arquivo = ?, curso_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, material.getNome(), material.getDescricao(), material.getCaminhoArquivo(), material.getCursoId(), id);
    }

    public void deleteMaterial(int materialId) {
        String sql = "DELETE FROM Materiais WHERE id = ?";
        jdbcTemplate.update(sql, materialId);
    }

    private Material mapMaterial(ResultSet rs, int rowNum) throws SQLException {
        Material material = new Material();
        material.setId(rs.getInt("id"));
        material.setNome(rs.getString("nome"));
        material.setDescricao(rs.getString("descricao"));
        material.setCaminhoArquivo(rs.getString("caminho_arquivo"));
        material.setCursoId(rs.getInt("curso_id"));
        return material;
    }
}
