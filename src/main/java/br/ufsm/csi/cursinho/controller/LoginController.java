package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.UsuarioDao;
import br.ufsm.csi.cursinho.model.TipoUsuario;
import br.ufsm.csi.cursinho.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsuarioDao usuarioDao;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/login-error")
    public String showLoginErrorPage(Model model) {
        model.addAttribute("loginError", "Username or password incorrect.");
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {
        try {
            Usuario usuario = usuarioDao.getUser(username, password);

            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", usuario.getId());
                session.setAttribute("tipoUsuario", usuario.getTipoUsuario());
                session.setAttribute("username", usuario.getNome());

                redirectAttributes.addFlashAttribute("username", usuario.getNome());
                redirectAttributes.addFlashAttribute("userId", usuario.getId());
                redirectAttributes.addFlashAttribute("tipoUsuario", usuario.getTipoUsuario());

                if (usuario.getTipoUsuario() == TipoUsuario.ALUNO) {
                    return "redirect:/listar-aulas";
                } else {
                    return "redirect:/listarcursos";
                }
            } else {
                return "redirect:/login-error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "erro";
        }
    }

    @GetMapping("/menu")
    public String showMenuPage(Model model) {
        return "view/menu";
    }

    @GetMapping("/registrar")
    public String showRegisterPage() {
        return "registrar/registrar";
    }
}
