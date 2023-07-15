package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.CursoDao;
import br.ufsm.csi.cursinho.DAO.MaterialDao;
import br.ufsm.csi.cursinho.model.Curso;
import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EditarCursoController {

    @Autowired
    private CursoDao cursoDao;

    @Autowired
    private MaterialDao materialDao;

    @GetMapping("/editar-curso")
    public String editarCursoForm(@RequestParam("id") int id, Model model) {
        Curso curso = cursoDao.getCursoById(id);
        if (curso == null) {
            return "redirect:/listarcursos";
        }
        model.addAttribute("curso", curso);

        List<Material> materiais = cursoDao.getMateriaisByCursoId(curso.getId());
        model.addAttribute("materiais", materiais);

        // Aqui você busca todos os materiais disponíveis
        List<Material> todosMateriais = materialDao.getMateriais();
        model.addAttribute("todosMateriais", todosMateriais);

        return "view/editar-curso";
    }

    @PostMapping("/atualizar-curso")
    public String atualizarCurso(@Valid Curso curso, BindingResult result, @RequestParam Integer materialSelecionado) {
        if (result.hasErrors()) {
            return "editar-curso";
        }

        cursoDao.updateCurso(curso);

        // Adicione uma verificação aqui se o materialSelecionado não for nulo
        if (materialSelecionado != null) {
            // Desvincule todos os materiais do curso
            cursoDao.desvincularMaterial(curso.getId());

            // Vincule o material selecionado ao curso
            cursoDao.vincularMaterial(curso.getId(), materialSelecionado);
        }

        return "redirect:/listarcursos";
    }
}