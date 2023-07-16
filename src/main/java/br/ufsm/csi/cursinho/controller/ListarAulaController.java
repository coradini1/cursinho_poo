package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.AulaDao;
import br.ufsm.csi.cursinho.DAO.MaterialDao;
import br.ufsm.csi.cursinho.model.Aula;
import br.ufsm.csi.cursinho.model.Curso;
import br.ufsm.csi.cursinho.model.Material;
import br.ufsm.csi.cursinho.model.TipoUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ListarAulaController {

    @Autowired
    private AulaDao aulaDao;

    @Autowired
    private MaterialDao materialDao;

    @GetMapping("/listar-aulas")
    public String listarAulas(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("username") != null) {
            List<Aula> aulas = aulaDao.getAulas();
            model.addAttribute("aulas", aulas);

            for (Aula aula : aulas) {
                Curso curso = aula.getCurso();
                List<Material> materiais = materialDao.getMateriaisByCursoId(curso.getId());
                curso.setMateriais(materiais);
            }

            TipoUsuario tipoUsuario = (TipoUsuario) session.getAttribute("tipoUsuario");
            String tipoUsuarioString = tipoUsuario.toString();
            model.addAttribute("tipoUsuario", tipoUsuarioString);

            return "view/listar-aulas";
        } else {
            return "redirect:/";
        }
    }
}
