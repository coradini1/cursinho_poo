package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.UsuarioDao;
import br.ufsm.csi.cursinho.model.TipoUsuario;
import br.ufsm.csi.cursinho.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;

@Controller
public class RegistrarUserController {

    @Autowired
    private UsuarioDao usuarioDao;

    @PostMapping("/registrarUser")
    public String registrarUsuario(@RequestParam("nome") String nome,
                                   @RequestParam("senha") String senha,
                                   @RequestParam("idade") int idade,
                                   @RequestParam("tipoUsuario") String tipoUsuario,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Verifica se o usuário já está registrado
            if (usuarioDao.getUserByName(nome) != null) {
                // Redireciona para a página de registro com uma mensagem de erro
                redirectAttributes.addFlashAttribute("errorMessage", "Usuário já registrado, registre outro.");
                return "redirect:/registrar";

            }

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setSenha(senha);
            usuario.setIdade(idade);
            usuario.setTipoUsuario(TipoUsuario.fromString(tipoUsuario));

            usuarioDao.create(usuario);

            // Redireciona para a página de login com uma mensagem de sucesso
            redirectAttributes.addFlashAttribute("registroSucesso", "Usuário registrado com sucesso. Faça o login.");

            return "redirect:/";
        } catch (SQLException e) {
            e.printStackTrace();
            // Redireciona para a página de registro com uma mensagem de erro
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao registrar usuário.");
            return "redirect:/registrar.html";
        }
    }
}
