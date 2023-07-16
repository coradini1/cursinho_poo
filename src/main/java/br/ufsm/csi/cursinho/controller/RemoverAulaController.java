package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.AulaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RemoverAulaController {

    @Autowired
    private AulaDao aulaDao;

    @RequestMapping(value = "/deletar-aula/{id}", method = RequestMethod.GET)
    public String removeAula(@PathVariable("id") Long id) {
        aulaDao.deleteAula(id.intValue());
        return "redirect:/listar-aulas";
    }
}
