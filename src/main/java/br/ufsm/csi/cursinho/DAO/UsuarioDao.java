package br.ufsm.csi.cursinho.DAO;

import br.ufsm.csi.cursinho.model.TipoUsuario;
import br.ufsm.csi.cursinho.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UsuarioDao {
    private DataSource dataSource;

    @Autowired
    public UsuarioDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void create(Usuario usuario) throws SQLException {
        if (isUsernameTaken(usuario.getNome())) {
            throw new SQLException("Username is already taken.");
        }

        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO usuarios (nome, idade, senha, tipoUsuario) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usuario.getNome());
            stmt.setInt(2, usuario.getIdade());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTipoUsuario().toString());
            stmt.executeUpdate();
        }
    }

    public Usuario getUser(String username, String password) throws SQLException {
        System.out.println("Getting user " + username);
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE nome = ? AND senha = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setIdade(rs.getInt("idade"));
                usuario.setTipoUsuario(TipoUsuario.fromString(rs.getString("tipoUsuario")));
                System.out.println("User found: " + usuario.getNome());
                return usuario;
            }
        }
        System.out.println("No user found");
        return null;
    }


    public Usuario getUserByName(String username) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE nome = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setIdade(rs.getInt("idade"));
                usuario.setTipoUsuario(TipoUsuario.fromString(rs.getString("tipoUsuario")));
                return usuario;
            }
        }

        return null;
    }

    private boolean isUsernameTaken(String username) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM usuarios WHERE nome = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            return count > 0;
        }
    }
}
