package org.cnpjinsight.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.cnpjinsight.services.ExportService;
import org.cnpjinsight.services.FileManager;
import org.cnpjinsight.services.TaskManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/export")
@EnableAsync
public class ExportController {

    private final ExportService exportService;
    private final TaskManager taskManager;
    private final FileManager fileManager;
    private final TaskExecutor taskExecutor;

    public ExportController(ExportService exportService, TaskManager taskManager, FileManager fileManager, @Qualifier("applicationTaskExecutor") TaskExecutor taskExecutor) {
        this.exportService = exportService;
        this.taskManager = taskManager;
        this.fileManager = fileManager;
        this.taskExecutor = taskExecutor;
    }

    @GetMapping("/start")
    public ResponseEntity<Map<String, String>> startExport(
            @RequestParam(required = false) List<String> uf,
            @RequestParam(required = false) List<String> naturezas,
            @RequestParam(required = false) List<String> situacoes,
            @RequestParam(required = false) List<String> cnaes,
            @RequestParam(required = false) List<String> mei,
            @RequestParam(defaultValue = "1000000") int limite,
            @RequestParam(required = false) String cursorCnpj
    ) {
        String taskId = taskManager.createTask();

        taskExecutor.execute(() -> {
            try {
                taskManager.updateTaskStatus(taskId, "EXECUTANDO");
                String fileName = exportService.executeExport(uf, naturezas, situacoes, cnaes, mei, limite, cursorCnpj, taskId);
                taskManager.updateTaskStatus(taskId, fileName);
            } catch (Exception e) {
                taskManager.updateTaskStatus(taskId, "FALHA");
                e.printStackTrace();
            }
        });

        Map<String, String> response = new HashMap<>();
        response.put("taskId", taskId);
        response.put("status", "ACEITO");

        return ResponseEntity.accepted().body(response);
    }


    @GetMapping("/tasks")
    public ResponseEntity<Map<String, String>> getAllTasks() {
        Map<String, String> tasks = taskManager.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/tasks/delete/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable String taskId) {
        taskManager.removeTask(taskId);
        return ResponseEntity.status(HttpStatus.OK).body("DELETADO");
    }



    @GetMapping("/files")
    public ResponseEntity<List<FileManager.ExportFileInfo>> listFiles() {
        List<FileManager.ExportFileInfo> files = fileManager.listExportFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/download/file/{fileName}")
    public void downloadFile(@PathVariable String fileName, HttpServletResponse response) {
        if (!fileManager.fileExists(fileName)) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        response.setContentType("text/csv");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        try {
            fileManager.transferFile(fileName, response.getOutputStream());
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            e.printStackTrace();
        }
    }

    @DeleteMapping("/file/delete/{fileName}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String fileName) {
        Map<String, String> response = new HashMap<>();

        if (!fileManager.fileExists(fileName)) {
            response.put("status", "error");
            response.put("message", "Arquivo não encontrado");
            return ResponseEntity.notFound().build();
        }

        boolean deleted = fileManager.deleteFile(fileName);
        if (deleted) {
            response.put("status", "success");
            response.put("message", "Arquivo deletado com sucesso");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Erro ao deletar arquivo");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}