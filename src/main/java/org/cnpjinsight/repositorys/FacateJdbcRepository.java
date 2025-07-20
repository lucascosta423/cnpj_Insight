package org.cnpjinsight.repositorys;

import org.cnpjinsight.model.CnaesModel;
import org.cnpjinsight.model.EstadosModel;
import org.cnpjinsight.model.MunicipiosModel;
import org.cnpjinsight.model.NaturezasJuridicasModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FacateJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public FacateJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CnaesModel> listAllCnaes() {
        String sql = "SELECT codigo, descricao FROM rfb_cnpj.cnaes";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CnaesModel cnaesModel = new CnaesModel();
            cnaesModel.setCodigo(rs.getString("codigo"));
            cnaesModel.setDescricao(rs.getString("descricao"));
            return cnaesModel;
        });
    }

    public List<NaturezasJuridicasModel> listAllNaturezasJuridicas() {
        String sql = "SELECT codigo, descricao FROM rfb_cnpj.naturezas_juridicas";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            NaturezasJuridicasModel naturezasJuridicasModel = new NaturezasJuridicasModel();
            naturezasJuridicasModel.setCodigo(rs.getString("codigo"));
            naturezasJuridicasModel.setDescricao(rs.getString("descricao"));
            return naturezasJuridicasModel;
        });
    }

    public List<EstadosModel> listAllEstados() {
        String sql = "SELECT codigo_uf, uf FROM rfb_cnpj.estados";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EstadosModel estadoModel = new EstadosModel();
            estadoModel.setCodigo_uf(rs.getInt("codigo_uf"));
            estadoModel.setUf(rs.getString("uf"));
            return estadoModel;
        });
    }

    public List<MunicipiosModel> listAllMunicipios() {
        String sql = "SELECT\n" +
                "\tes.uf,\n" +
                "\tmu.siafi_id,\n" +
                "\tmu.nome\n" +
                "FROM rfb_cnpj.municipios mu\n" +
                "JOIN rfb_cnpj.estados es\n" +
                "\tON mu.codigo_uf = es.codigo_uf";

        return jdbcTemplate.query(sql,(rs, rowNum) -> {
            MunicipiosModel municipiosModel = new MunicipiosModel();
            municipiosModel.setUf(rs.getString("uf"));
            municipiosModel.setSiafi_id(rs.getString("siafi_id"));
            municipiosModel.setNome(rs.getString("nome"));

            return municipiosModel;
        });
    }

    public List<MunicipiosModel> findByNameMunicipality(String busca) {
        String sql = """
        SELECT 
            es.uf,
            mu.siafi_id,
            mu.nome
        FROM rfb_cnpj.municipios mu
        JOIN rfb_cnpj.estados es ON mu.codigo_uf = es.codigo_uf
        WHERE LOWER(mu.nome) LIKE '%' || ? || '%'
        ORDER BY mu.nome
        LIMIT 50
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MunicipiosModel municipio = new MunicipiosModel();
            municipio.setUf(rs.getString("uf"));
            municipio.setSiafi_id(rs.getString("siafi_id"));
            municipio.setNome(rs.getString("nome"));
            return municipio;
        }, busca.toLowerCase());
    }

    public List<CnaesModel> findByDescricaoCnaes(String busca) {
        String sql = """
        SELECT codigo, descricao FROM rfb_cnpj.cnaes    
        WHERE LOWER(descricao) LIKE '%' || ? || '%'
        ORDER BY descricao
        LIMIT 50
    """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CnaesModel cnaesModel = new CnaesModel();
            cnaesModel.setCodigo(rs.getString("codigo"));
            cnaesModel.setDescricao(rs.getString("descricao"));
            return cnaesModel;
        }, busca.toLowerCase());
    }

    public List<NaturezasJuridicasModel> findByDescricaoNaturezas(String busca) {
        String sql = """
        SELECT codigo, descricao FROM rfb_cnpj.naturezas_juridicas    
        WHERE LOWER(descricao) LIKE '%' || ? || '%'
        ORDER BY descricao
        LIMIT 50
    """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            NaturezasJuridicasModel naturezasJuridicasModel = new NaturezasJuridicasModel();
            naturezasJuridicasModel.setCodigo(rs.getString("codigo"));
            naturezasJuridicasModel.setDescricao(rs.getString("descricao"));
            return naturezasJuridicasModel;
        }, busca.toLowerCase());
    }

    public List<EstadosModel> findByUf(String busca) {
        String sql = """
        SELECT codigo_uf, uf FROM rfb_cnpj.estados
        WHERE LOWER(uf) LIKE '%' || ? || '%'
        ORDER BY uf
        LIMIT 10
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EstadosModel estadoModel = new EstadosModel();
            estadoModel.setCodigo_uf(rs.getInt("codigo_uf"));
            estadoModel.setUf(rs.getString("uf"));
            return estadoModel;
        }, busca.toLowerCase());
    }

}

