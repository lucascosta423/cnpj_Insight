package org.cnpjinsight.services;

import org.cnpjinsight.dto.CnaeDto;
import org.cnpjinsight.dto.EstadosDTO;
import org.cnpjinsight.dto.MunicipiosDTO;
import org.cnpjinsight.dto.NaturezasJuridicasDto;
import org.cnpjinsight.repositorys.FacateJdbcRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacateService {

    private final FacateJdbcRepository facateJdbcRepository;

    public FacateService(FacateJdbcRepository facateJdbcRepository) {
        this.facateJdbcRepository = facateJdbcRepository;
    }

    public List<CnaeDto> listAllCnaes() {
        return facateJdbcRepository.listAllCnaes()
                .stream()
                .map(c -> new CnaeDto(c.getCodigo(), c.getDescricao()))
                .toList();
    }


    public List<NaturezasJuridicasDto> listAllNaturezasJuridicas() {
        return facateJdbcRepository.listAllNaturezasJuridicas()
                .stream()
                .map(nj -> new NaturezasJuridicasDto(nj.getCodigo(), nj.getDescricao()))
                .toList();
    }

    public List<EstadosDTO> listAllEstados() {
        return facateJdbcRepository.listAllEstados()
                .stream()
                .map(es -> new EstadosDTO(es.getCodigo_uf(), es.getUf()))
                .toList();
    }

    public List<MunicipiosDTO> listAllMunicipios() {
        return facateJdbcRepository.listAllMunicipios()
                .stream()
                .map(mu -> new MunicipiosDTO(mu.getUf(),mu.getSiafi_id(), mu.getNome()))
                .toList();
    }

    public List<MunicipiosDTO> findByNameMunicipality(String busca) {
        return facateJdbcRepository.findByNameMunicipality(busca)
                .stream()
                .map(mu -> new MunicipiosDTO(mu.getUf(),mu.getSiafi_id(), mu.getNome()))
                .toList();
    }

    public List<CnaeDto> findByDescricaoCnaes(String busca) {
        return facateJdbcRepository.findByDescricaoCnaes(busca)
                .stream()
                .map(cn -> new CnaeDto(cn.getCodigo(),cn.getDescricao()))
                .toList();
    }

    public List<NaturezasJuridicasDto> findByDescricaoNaturezas(String busca) {
        return facateJdbcRepository.findByDescricaoNaturezas(busca)
                .stream()
                .map(cn -> new NaturezasJuridicasDto(cn.getCodigo(),cn.getDescricao()))
                .toList();
    }

    public List<EstadosDTO> findByUf(String uf) {
        return facateJdbcRepository.findByUf(uf)
                .stream()
                .map(es -> new EstadosDTO(es.getCodigo_uf(), es.getUf()))
                .toList();
    }

}

