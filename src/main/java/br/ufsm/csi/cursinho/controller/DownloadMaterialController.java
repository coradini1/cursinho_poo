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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Controller
public class DownloadMaterialController {

    @Autowired
    private MaterialDao materialDao;

    @GetMapping({"/download", "/download-material/{id}"})
    public ResponseEntity<InputStreamResource> downloadMaterial(@RequestParam(value = "id", required = false) Optional<Integer> queryId,
                                                                @PathVariable(value = "id", required = false) Optional<Integer> pathId) throws IOException {

        int materialId = queryId.orElse(pathId.orElse(-1));

        if(materialId == -1){
            return ResponseEntity.notFound().build();
        }

        Material material = materialDao.getMaterialById(materialId);

        if (material == null) {
            return ResponseEntity.notFound().build();
        }

        String caminhoArquivo = material.getCaminhoArquivo();

        File arquivoMaterial = new File(caminhoArquivo);

        if (!arquivoMaterial.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(arquivoMaterial));
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivoMaterial.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(arquivoMaterial.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
