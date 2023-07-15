package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.CursoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RemoverCursoController {

    @Autowired
    private CursoDao cursoDao;

    @GetMapping("/remover-curso/{id}")
    public String removerCurso(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        cursoDao.deleteCurso(id);
        redirectAttributes.addFlashAttribute("mensagem", "Curso removido com sucesso.");
        return "redirect:/listarcursos";
    }
}
