package br.ufsm.csi.cursinho.DAO;

import br.ufsm.csi.cursinho.model.Aula;
import br.ufsm.csi.cursinho.model.Curso;
import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AulaDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CursoDao cursoDao;

    public void createAula(Aula aula) {
        String sql = "INSERT INTO Aulas (titulo, data, curso_id, material_id) VALUES (?, ?, ?, ?)";
        Object[] params = new Object[] { aula.getTitulo(), aula.getData(), aula.getCurso().getId(),
                aula.getMaterial() != null ? aula.getMaterial().getId() : null };
        jdbcTemplate.update(sql, params);
    }

    public List<Aula> getAulas() {
        String sql = "SELECT * FROM Aulas";
        List<Aula> aulas = jdbcTemplate.query(sql, this::mapAula);

        for (Aula aula : aulas) {
            int cursoId = aula.getCurso().getId();
            List<Material> materiais = cursoDao.getMateriaisByCursoId(cursoId);
            aula.getCurso().setMateriais(materiais);
        }

        return aulas;
    }

    public Aula getAulaById(int aulaId) {
        String sql = "SELECT * FROM Aulas WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapAula, aulaId);
    }

    public void updateAula(Aula aula) {
        String sql = "UPDATE Aulas SET titulo = ?, data = ?, curso_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, aula.getTitulo(), aula.getData(), aula.getCurso().getId(), aula.getId());
    }

    public void deleteAula(int aulaId) {
        String sql = "DELETE FROM Aulas WHERE id = ?";
        jdbcTemplate.update(sql, aulaId);
    }


    private Aula mapAula(ResultSet rs, int rowNum) throws SQLException {
        Aula aula = new Aula();
        aula.setId(rs.getLong("id"));
        aula.setTitulo(rs.getString("titulo"));
        aula.setData(rs.getDate("data").toLocalDate());

        // Buscar o curso relacionado à aula
        int cursoId = rs.getInt("curso_id");
        Curso curso = cursoDao.getCursoById(cursoId);
        aula.setCurso(curso);

        // Buscar os materiais associados ao curso
        List<Material> materiais = cursoDao.getMateriaisByCursoId(cursoId);
        curso.setMateriais(materiais);

        return aula;
    }



}
