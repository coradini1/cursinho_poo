package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.CursoDao;
import br.ufsm.csi.cursinho.model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

@Controller
public class ListarCursosController {

    @Autowired
    private CursoDao cursoDao;

    @GetMapping("/listarcursos")
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoDao.getAllCursos();
        model.addAttribute("cursos", cursos);

        return "view/menu";
    }



}
