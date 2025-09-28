import java.util.List;

public interface TaskManager {
    // Task operations
    int addTask(Task task);
    Task getTask(int id);
    void updateTaskStatus(int id, Status status);
    void deleteTask(int id);
    List<Task> getAllTasks();

    // Epic operations
    int addEpic(Epic epic);
    Epic getEpic(int id);
    void deleteEpic(int id);
    List<Epic> getAllEpics();

    // Subtask operations
    int addSubtask(Subtask subtask);
    Subtask getSubtask(int id);
    void updateSubtaskStatus(int id, Status status);
    void deleteSubtask(int id);
    List<Subtask> getAllSubtasksForEpic(int epicId);
    List<Subtask> getAllSubtasks();

    // History
    List<Task> getHistory();

    // Additional methods
    void clearAll();
}