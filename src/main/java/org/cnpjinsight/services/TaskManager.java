package org.cnpjinsight.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskManager {

    private final DataSource dataSource;
    private final Map<String, String> taskMap = new ConcurrentHashMap<>();
    private final Map<String, Integer> pidMap = new ConcurrentHashMap<>();

    public TaskManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String createTask() {
        String taskId = UUID.randomUUID().toString().substring(0, 8);
        taskMap.put(taskId, "PENDENTE");
        return taskId;
    }

    public void registerPid(String taskId, int pid) {
        pidMap.put(taskId, pid);
    }

    public void updateTaskStatus(String taskId, String status) {
        taskMap.put(taskId, status);
        if (!Objects.equals(status, "EXECUTANDO")) {
            taskMap.remove(taskId);
            pidMap.remove(taskId);
        }
    }

    public Integer getPid(String taskId) {
        return pidMap.get(taskId);
    }

    public String getTaskStatus(String taskId) {
        return taskMap.get(taskId);
    }

    public boolean taskExists(String taskId) {
        return taskMap.containsKey(taskId);
    }

    public void removeTask(String taskId) {
        Integer pid = pidMap.get(taskId);

        if (pid != null) {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT pg_cancel_backend(?)")) {
                ps.setInt(1, pid);
                ps.execute();
            } catch (SQLException e) {
                System.err.println("Erro ao cancelar backend: " + e.getMessage());
            }
        }
        pidMap.remove(taskId);
        taskMap.remove(taskId);
    }

    public void removePid(String taskId) {
        pidMap.remove(taskId);
    }

    public Map<String, String> getAllTasks() {
        return new ConcurrentHashMap<>(taskMap);
    }
}