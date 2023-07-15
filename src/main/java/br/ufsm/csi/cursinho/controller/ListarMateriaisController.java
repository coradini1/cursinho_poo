    package br.ufsm.csi.cursinho.controller;

    import br.ufsm.csi.cursinho.DAO.MaterialDao;
    import br.ufsm.csi.cursinho.model.Material;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;

    import java.util.List;

    @Controller
    public class ListarMateriaisController {

        @Autowired
        private MaterialDao materialDao;

        @GetMapping("/listarmateriais")
        public String listarMateriais(Model model) {
            List<Material> materiais = materialDao.getMateriais();
            model.addAttribute("materiais", materiais);
            return "view/listar-materiais";
        }
    }