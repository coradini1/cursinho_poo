package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.MaterialDao;
import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/editar-material")
public class EditarMaterialController {

    @Autowired
    private MaterialDao materialDao;

    @Autowired
    private ServletContext servletContext;

    @GetMapping
    public String mostrarFormularioEdicao(@RequestParam("id") int materialId, Model model) {
        Material material = materialDao.getMaterialById(materialId);
        if (material == null) {
            return "redirect:/listarmateriais";
        }
        model.addAttribute("material", material);
        return "view/editar-material";
    }

    @PostMapping("/{id}")
    public String editarMaterial(@PathVariable("id") int materialId, @ModelAttribute("material") @Valid Material material,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 @RequestParam("novoArquivoPDF") MultipartFile novoArquivoPDF) throws IOException {
        if (bindingResult.hasErrors()) {
            return "view/editar-material";
        }

        Material existingMaterial = materialDao.getMaterialById(materialId);
        if (existingMaterial == null) {
            return "redirect:/listarmateriais";
        }

        // Atualizar os dados do material existente
        existingMaterial.setNome(material.getNome());
        existingMaterial.setDescricao(material.getDescricao());
        existingMaterial.setCursoId(material.getCursoId()); // Adicionado a atualização do ID do curso

        if (!novoArquivoPDF.isEmpty()) {
            String caminhoArquivo = saveUploadedFile(novoArquivoPDF);
            existingMaterial.setCaminhoArquivo(caminhoArquivo);
        }

        // Salvar o material atualizado
        materialDao.updateMaterial((long) materialId, existingMaterial); // Modificado para corresponder à definição do DAO

        redirectAttributes.addFlashAttribute("mensagem", "Material atualizado com sucesso.");
        return "redirect:/listarmateriais";
    }

    private String saveUploadedFile(MultipartFile file) throws IOException {
        // Gerar um nome único para o arquivo PDF
        String nomeArquivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Obter o caminho de armazenamento absoluto da pasta "uploads"
        String caminhoUploads = servletContext.getRealPath("/uploads");

        // Criar o diretório "uploads" se não existir
        File uploadsDir = new File(caminhoUploads);
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs();
        }

        // Salvar o arquivo PDF no diretório "uploads"
        String caminhoArquivo = caminhoUploads + File.separator + nomeArquivo;
        file.transferTo(new File(caminhoArquivo));

        return caminhoArquivo;
    }
}
