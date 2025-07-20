package org.cnpjinsight.services;

import org.postgresql.core.BaseConnection;
import org.postgresql.copy.CopyManager;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TaskManager taskManager;

    @Value("${export.directory:exports}")
    private String exportDir;

    public String executeExport(List<String> uf, List<String> naturezas, List<String> situacoes,
                                List<String> cnaes, List<String> mei, int limite, String cursorCnpj,
                                String taskId) throws Exception {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String fileName = String.format("export_%s_%s.csv",taskId,timestamp);
        File outFile = new File(exportDir, fileName);

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            // Captura o PID atual e registra no TaskManager
            try (Statement st = conn.createStatement();
                 var rs = st.executeQuery("SELECT pg_backend_pid()")) {
                if (rs.next()) {
                    int pid = rs.getInt(1);
                    taskManager.registerPid(taskId, pid);
                }
            }

            createTempTable(conn);

            executeProcedure(conn, uf, naturezas, situacoes, cnaes, mei, limite, cursorCnpj);

            exportToCsv(conn, outFile);

            conn.commit();
        } catch (PSQLException e) {
            return "CANCELADO";
        }
        return "CONCLUIDO";
    }

    private void createTempTable(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TEMP TABLE temp_export_dados (" +
                    "cnpj_completo varchar, razao_social varchar, situacao_cadastral varchar, " +
                    "cnae_fiscal_principal varchar, cnae_descricao varchar, natureza_codigo varchar, " +
                    "natureza_descricao varchar, tipo_logradouro varchar, logradouro varchar, " +
                    "bairro varchar, cep varchar, cidade varchar, uf varchar, telefone_1 varchar, " +
                    "telefone_2 varchar, fax varchar, email varchar, opcao_mei char) ON COMMIT DROP;");
        }
    }

    private void executeProcedure(Connection conn, List<String> uf, List<String> naturezas,
                                  List<String> situacoes, List<String> cnaes, List<String> mei,
                                  int limite, String cursorCnpj) throws Exception {

        try (PreparedStatement ps = conn.prepareStatement(
                "CALL rfb_cnpj.exporta_dados_procedure(?, ?, ?, ?, ?, ?, ?)")) {
            ps.setArray(1, toSqlArray(conn, uf));
            ps.setArray(2, toSqlArray(conn, naturezas));
            ps.setArray(3, toSqlArray(conn, situacoes));
            ps.setArray(4, toSqlArray(conn, cnaes));
            ps.setArray(5, toSqlArray(conn, mei));
            ps.setInt(6, limite);
            if (cursorCnpj != null) ps.setString(7, cursorCnpj);
            else ps.setNull(7, java.sql.Types.VARCHAR);
            ps.execute();
        }
    }

    private void exportToCsv(Connection conn, File outFile) throws Exception {
        CopyManager cm = new CopyManager(conn.unwrap(BaseConnection.class));
        try (OutputStream fos = new FileOutputStream(outFile)) {
            cm.copyOut("COPY temp_export_dados TO STDOUT WITH (FORMAT CSV, HEADER, DELIMITER ';',ENCODING 'UTF8')", fos);
        }
    }

    private java.sql.Array toSqlArray(Connection conn, List<String> list) throws Exception {
        if (list == null || list.isEmpty()) {
            return conn.createArrayOf("text", null);
        }
        return conn.createArrayOf("text", list.toArray());
    }
}
