package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.CursoDao;
import br.ufsm.csi.cursinho.model.Curso;
import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.ufsm.csi.cursinho.DAO.MaterialDao;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Controller
public class RegistrarCursoController {

    @Autowired
    private CursoDao cursoDao;

    @Autowired
    private MaterialDao materialDao;

    @GetMapping("/registrar-curso")
    public String showRegistrarCursoPage(Model model) {
        List<Material> materiais = materialDao.getMateriais();
        model.addAttribute("materiais", materiais);
        model.addAttribute("curso", new Curso());
        return "registrar/registrar-curso";
    }

    @PostMapping("/registrar-curso")
    public String registrarCurso(@ModelAttribute("curso") @Valid Curso curso,
                                 @RequestParam(value = "materiaisSelecionados", required = false) List<Integer> materiaisSelecionados,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            List<Material> materiais = materialDao.getMateriais();
            redirectAttributes.addFlashAttribute("curso", curso);
            redirectAttributes.addFlashAttribute("materiais", materiais);
            return "redirect:/registrar-curso";
        }

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("erro", "Faça login para registrar um curso.");
            return "redirect:/";
        }

        Curso cursoExistente = cursoDao.getCursoByNome(curso.getNome());
        if (cursoExistente != null) {
            redirectAttributes.addFlashAttribute("erro", "Esse curso já está registrado no sistema.");
            return "redirect:/registrar-curso";
        }

        curso.setProfessorId(userId);

        if (materiaisSelecionados != null) {
            for (Integer materialId : materiaisSelecionados) {
                Material material = materialDao.getMaterialById(materialId);
                if (material != null) {
                    curso.adicionarMaterial(material);
                }
            }
        }

        cursoDao.createCurso(curso, materiaisSelecionados);

        redirectAttributes.addFlashAttribute("mensagem", "Curso registrado com sucesso.");
        return "redirect:/listarcursos";
    }
}
