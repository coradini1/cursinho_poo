    package br.ufsm.csi.cursinho.DAO;

    import br.ufsm.csi.cursinho.model.Curso;
    import br.ufsm.csi.cursinho.model.Material;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.dao.EmptyResultDataAccessException;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.jdbc.support.GeneratedKeyHolder;
    import org.springframework.jdbc.support.KeyHolder;
    import org.springframework.stereotype.Repository;

    import java.sql.PreparedStatement;
    import java.sql.Statement;
    import java.util.List;

    @Repository
    public class CursoDao {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        public void createCurso(Curso curso, List<Integer> materiaisSelecionados) {
            String sql = "INSERT INTO Cursos (nome, descricao, professor_id) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, curso.getNome());
                ps.setString(2, curso.getDescricao());
                ps.setInt(3, curso.getProfessorId());
                return ps;
            }, keyHolder);

            int cursoId = keyHolder.getKey().intValue();

            if (materiaisSelecionados != null) {
                String vincularMaterialSql = "UPDATE Materiais SET curso_id = ? WHERE id = ?";
                for (Integer materialId : materiaisSelecionados) {
                    jdbcTemplate.update(vincularMaterialSql, cursoId, materialId);
                }
            }
        }

        public int getProfessorIdDoUsuario(String username) {
            String sql = "SELECT id FROM Usuarios WHERE username = ?";
            Integer professorId = jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class);
            if (professorId != null) {
                return professorId.intValue();
            } else {
                throw new IllegalArgumentException("Professor não encontrado. Certifique-se de que o usuário esteja registrado como professor.");
            }
        }

        public List<Curso> getCursosByProfessorId(int professorId) {
            String sql = "SELECT * FROM Cursos WHERE professor_id = ?";
            return jdbcTemplate.query(sql, new Object[]{professorId}, (rs, rowNum) -> {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                curso.setDescricao(rs.getString("descricao"));
                return curso;
            });
        }

        public Curso getCursoById(int id) {
            String sql = "SELECT * FROM Cursos WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                curso.setDescricao(rs.getString("descricao"));
                return curso;
            });
        }

        public Curso getCursoByNome(String nome) {
            String sql = "SELECT * FROM Cursos WHERE nome = ?";
            try {
                return jdbcTemplate.queryForObject(sql, new Object[]{nome}, (rs, rowNum) -> {
                    Curso curso = new Curso();
                    curso.setId(rs.getInt("id"));
                    curso.setNome(rs.getString("nome"));
                    curso.setDescricao(rs.getString("descricao"));
                    return curso;
                });
            } catch (EmptyResultDataAccessException e) {
                return null;
            }
        }

        public List<Curso> getAllCursos() {
            String sql = "SELECT * FROM Cursos";
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                Curso curso = new Curso();
                curso.setId(rs.getInt("id"));
                curso.setNome(rs.getString("nome"));
                curso.setDescricao(rs.getString("descricao"));
                return curso;
            });
        }

        public void updateCurso(Curso curso) {
            String sql = "UPDATE Cursos SET nome = ?, descricao = ? WHERE id = ?";
            jdbcTemplate.update(sql, curso.getNome(), curso.getDescricao(), curso.getId());
        }

        public void deleteCurso(int id) {
            // Remover a referência do curso dos materiais
            String updateMateriaisSql = "UPDATE Materiais SET curso_id = NULL WHERE curso_id = ?";
            jdbcTemplate.update(updateMateriaisSql, id);

            // Remover o curso da tabela Cursos
            String deleteCursoSql = "DELETE FROM Cursos WHERE id = ?";
            jdbcTemplate.update(deleteCursoSql, id);
        }

        public void vincularMaterial(int courseId, int materialId) {
            String sql = "UPDATE Materiais SET curso_id = ? WHERE id = ?";
            jdbcTemplate.update(sql, courseId, materialId);
        }

        public void desvincularMaterial(int courseId) {
            String sql = "UPDATE Materiais SET curso_id = NULL WHERE curso_id = ?";
            jdbcTemplate.update(sql, courseId);
        }

        public List<Material> getMateriaisByCursoId(int courseId) {
            String sql = "SELECT * FROM Materiais WHERE curso_id = ?";
            return jdbcTemplate.query(sql, new Object[]{courseId}, (rs, rowNum) -> {
                Material material = new Material();
                material.setId(rs.getInt("id"));
                material.setNome(rs.getString("nome"));
                material.setDescricao(rs.getString("descricao"));
                return material;
            });
        }
    }
