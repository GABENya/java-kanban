package tasks;

import java.util.ArrayList;
import java.util.List;
import test.Task;  // Добавлен импорт для Task из пакета test

public class Epic extends Task {
    private List<Integer> subtaskIds;

    public Epic(int id, String title, String description) {
        super(id, title, description);
        this.subtaskIds = new ArrayList<>();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public void clearSubtasks() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
