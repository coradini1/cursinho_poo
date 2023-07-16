package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.AulaDao;
import br.ufsm.csi.cursinho.DAO.CursoDao;
import br.ufsm.csi.cursinho.DAO.MaterialDao;
import br.ufsm.csi.cursinho.model.Aula;
import br.ufsm.csi.cursinho.model.Curso;
import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RegistrarAulaController {

    @Autowired
    private AulaDao aulaDao;

    @Autowired
    private CursoDao cursoDao;

    @Autowired
    private MaterialDao materialDao;

    @GetMapping("/registrar-aula")
    public String mostrarFormularioRegistroAula(Model model) {
        List<Curso> cursos = cursoDao.getAllCursos();

        List<Material> materiais = materialDao.getMateriais();

        model.addAttribute("cursos", cursos);
        model.addAttribute("materiais", materiais);
        model.addAttribute("aula", new Aula());
        return "registrar/registrar-aula";
    }

    @PostMapping("/registrar-aula")
    public String registrarAula(@ModelAttribute("aula") @Valid Aula aula, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registrar/registrar-aula";
        }

        // Salva a nova aula no banco de dados
        aulaDao.createAula(aula);

        redirectAttributes.addFlashAttribute("mensagem", "Aula registrada com sucesso.");
        return "redirect:/listar-aulas";
    }
}
