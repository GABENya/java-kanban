package managers;

import java.util.List;
import test.Task;  // Добавлен импорт для Task из пакета test
import tasks.Epic;  // Добавлен импорт для Epic из пакета tasks
import tasks.Status;  // Добавлен импорт для Status из пакета tasks
import tasks.Subtask;  // Добавлен импорт для Subtask из пакета tasks

public interface TaskManager {
    // test.Task operations
    int addTask(Task task);
    Task getTask(int id);
    void updateTaskStatus(int id, Status status);
    void deleteTask(int id);
    List<Task> getAllTasks();

    // tasks.Epic operations
    int addEpic(Epic epic);
    Epic getEpic(int id);
    void deleteEpic(int id);
    List<Epic> getAllEpics();

    // tasks.Subtask operations
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
