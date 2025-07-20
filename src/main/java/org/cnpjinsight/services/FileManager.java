package org.cnpjinsight.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileManager {

    @Value("${export.directory:exports}")
    private String exportDir;

    @PostConstruct
    public void init() {
        File dir = new File(exportDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public boolean fileExists(String fileName) {
        File file = new File(exportDir, fileName);
        return file.exists();
    }

    public File getFile(String fileName) {
        return new File(exportDir, fileName);
    }

    public void transferFile(String fileName, OutputStream outputStream) throws Exception {
        File file = new File(exportDir, fileName);
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.transferTo(outputStream);
        }
    }

    public List<ExportFileInfo> listExportFiles() {
        List<ExportFileInfo> fileList = new ArrayList<>();
        File dir = new File(exportDir);

        if (!dir.exists() || !dir.isDirectory()) {
            return fileList;
        }

        File[] files = dir.listFiles((file) -> file.getName().endsWith(".csv"));
        if (files != null) {
            for (File file : files) {
                try {
                    Path path = Paths.get(file.getAbsolutePath());
                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

                    ExportFileInfo fileInfo = new ExportFileInfo();
                    String fileName = file.getName();
                    fileInfo.setFileName(fileName);
                    fileInfo.setSize(file.length());
                    fileInfo.setCreatedDate(LocalDateTime.ofInstant(
                            attrs.creationTime().toInstant(), ZoneId.systemDefault()));
                    fileInfo.setModifiedDate(LocalDateTime.ofInstant(
                            attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));

                    // Extrair o taskId do nome do arquivo: export_<taskId>_<timestamp>.csv
                    if (fileName.startsWith("export_") && fileName.endsWith(".csv")) {
                        String base = fileName.substring(7, fileName.length() - 4); // Remove "export_" e ".csv"
                        int underscoreIndex = base.indexOf('_');
                        if (underscoreIndex > 0) {
                            String taskId = base.substring(0, underscoreIndex);
                            fileInfo.setTaskId(taskId);
                        }
                    }

                    fileList.add(fileInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        fileList.sort((a, b) -> b.getCreatedDate().compareTo(a.getCreatedDate()));
        return fileList;
    }

    public boolean deleteFile(String fileName) {
        File file = new File(exportDir, fileName);
        return file.exists() && file.delete();
    }

    public long getFileSize(String fileName) {
        File file = new File(exportDir, fileName);
        return file.exists() ? file.length() : 0;
    }

    // Classe interna para informações do arquivo
    public static class ExportFileInfo {
        private String taskId;
        private String fileName;
        private long size;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
        // Getters e Setters
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }

        public long getSize() { return size; }
        public void setSize(long size) { this.size = size; }

        public LocalDateTime getCreatedDate() { return createdDate; }
        public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

        public LocalDateTime getModifiedDate() { return modifiedDate; }
        public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }

        public String getFormattedSize() {
            if (size < 1024) return size + " B";
            if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
            if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024.0));
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
}