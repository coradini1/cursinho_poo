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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class EditarAulaController {

    @Autowired
    private AulaDao aulaDao;

    @Autowired
    private CursoDao cursoDao;

    @Autowired
    private MaterialDao materialDao;

    @RequestMapping(value = "/editar-aula/{id}", method = RequestMethod.GET)
    public String showEditAulaPage(@PathVariable("id") Long id, Model model) {
        Aula aula = aulaDao.getAulaById(id.intValue());
        model.addAttribute("aula", aula);

        List<Curso> todosCursos = cursoDao.getAllCursos();
        model.addAttribute("todosCursos", todosCursos);

        List<Material> todosMateriais = materialDao.getMateriais();
        model.addAttribute("todosMateriais", todosMateriais);

        return "view/editar-aula";
    }


    @RequestMapping(value = "/editar-aula", method = RequestMethod.POST)
    public String editAula(Aula aula) {
        aulaDao.updateAula(aula);
        return "redirect:/listar-aulas";
    }
}
