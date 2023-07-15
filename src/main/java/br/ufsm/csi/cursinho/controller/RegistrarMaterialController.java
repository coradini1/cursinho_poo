package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.MaterialDao;
import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
public class RegistrarMaterialController {

    @Autowired
    private MaterialDao materialDao;

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/registrar-material")
    public String showRegistrarMaterialPage(Model model) {
        model.addAttribute("material", new Material());
        return "registrar/registrar-material";
    }

    @PostMapping("/registrar-material")
    public String registrarMaterial(@ModelAttribute("material") @Valid Material material,
                                    BindingResult bindingResult,
                                    @RequestParam("arquivoPDF") MultipartFile arquivoPDF,
                                    RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "registrar/registrar-material";
        }

        // Verificar se o arquivo PDF foi selecionado
        if (arquivoPDF.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Selecione um arquivo PDF.");
            return "redirect:/registrar-material";
        }

        // Gerar um nome único para o arquivo PDF
        String nomeArquivo = UUID.randomUUID().toString() + "_" + arquivoPDF.getOriginalFilename();

        // Obter o caminho de armazenamento absoluto da pasta "uploads"
        String caminhoUploads = servletContext.getRealPath("/uploads");

        // Criar o diretório "uploads" se não existir
        File uploadsDir = new File(caminhoUploads);
        if (!uploadsDir.exists()) {
            uploadsDir.mkdirs();
        }

        // Salvar o arquivo PDF no diretório "uploads"
        String caminhoArquivo = caminhoUploads + File.separator + nomeArquivo;
        arquivoPDF.transferTo(new File(caminhoArquivo));

        // Configurar o caminho do arquivo PDF no objeto Material
        material.setCaminhoArquivo(caminhoArquivo);

        // Cadastrar o material
        materialDao.createMaterial(material);

        redirectAttributes.addFlashAttribute("mensagem", "Material registrado com sucesso.");
        return "redirect:/listarcursos";
    }
}
