package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.MaterialDao;
import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;

@Controller
public class RemoverMaterialController {

    @Autowired
    private MaterialDao materialDao;

    @GetMapping("/remover-material/{id}")
    public String removerMaterial(@PathVariable("id") int materialId, RedirectAttributes redirectAttributes) {
        materialDao.deleteMaterial(materialId);
        redirectAttributes.addFlashAttribute("mensagem", "Material removido com sucesso.");
        return "redirect:/listarmateriais";
    }
}