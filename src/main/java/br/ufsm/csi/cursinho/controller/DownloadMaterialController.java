package br.ufsm.csi.cursinho.controller;

import br.ufsm.csi.cursinho.DAO.MaterialDao;
import br.ufsm.csi.cursinho.model.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class DownloadMaterialController {

    @Autowired
    private MaterialDao materialDao;

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadMaterial(@RequestParam("id") int materialId) throws IOException {
        Material material = materialDao.getMaterialById(materialId);

        if (material == null) {
            return ResponseEntity.notFound().build();
        }

        // Obter o caminho absoluto do arquivo do material
        String caminhoArquivo = material.getCaminhoArquivo();

        // Criar o objeto File com o caminho do arquivo
        File arquivoMaterial = new File(caminhoArquivo);

        // Verificar se o arquivo existe
        if (!arquivoMaterial.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Preparar o recurso do arquivo para download
        InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(arquivoMaterial));
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        // Definir o cabeçalho de resposta para o navegador
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivoMaterial.getName());

        // Retornar a resposta com o arquivo para download
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(arquivoMaterial.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
