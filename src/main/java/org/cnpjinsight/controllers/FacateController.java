package org.cnpjinsight.controllers;

import org.cnpjinsight.dto.CnaeDto;
import org.cnpjinsight.dto.EstadosDTO;
import org.cnpjinsight.dto.MunicipiosDTO;
import org.cnpjinsight.dto.NaturezasJuridicasDto;
import org.cnpjinsight.services.FacateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rfbcnpj")
public class FacateController {

    private final FacateService facateService;

    public FacateController(FacateService facateService) {
        this.facateService = facateService;
    }

    @GetMapping("/cnaes")
    public List<CnaeDto> listAllCnaes(@RequestParam(required = false) String busca) {
        if (busca == null || busca.isBlank()) {
            return facateService.listAllCnaes();
        }
        return facateService.findByDescricaoCnaes(busca);
    }

    @GetMapping("/naturezas")
    public List<NaturezasJuridicasDto> listAllNaturezasJuridicas(@RequestParam(required = false) String busca) {
        if (busca == null || busca.isBlank()) {
            return facateService.listAllNaturezasJuridicas();
        }
        return facateService.findByDescricaoNaturezas(busca);
    }

    @GetMapping("/estados")
    public List<EstadosDTO> listAllEstados(@RequestParam(required = false) String busca) {
        if (busca != null && !busca.isBlank()) {
            return facateService.findByUf(busca);
        }
        return facateService.listAllEstados();
    }

    @GetMapping("/municipios")
    public List<MunicipiosDTO> listAllMunicipios(@RequestParam(required = false) String busca) {
        if (busca == null || busca.isBlank()) {
            return facateService.listAllMunicipios();
        }
        return facateService.findByNameMunicipality(busca);
    }
}
