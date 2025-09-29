package managers;

import java.util.*;
import test.Task;  // Добавлен импорт для Task из пакета test
import tasks.Epic;  // Добавлен импорт для Epic из пакета tasks
import tasks.Status;  // Добавлен импорт для Status из пакета tasks
import tasks.Subtask;  // Добавлен импорт для Subtask из пакета tasks

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int currentId = 1;
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public int addTask(Task task) {
        task.setId(currentId++);  // Используем setter вместо прямого присваивания
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void updateTaskStatus(int id, Status status) {
        Task task = tasks.get(id);
        if (task != null) {
            task.setStatus(status);
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public int addEpic(Epic epic) {
        epic.setId(currentId++);  // Используем setter вместо прямого присваивания
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
                historyManager.remove(subId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public int addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new IllegalArgumentException("Эпик с ID " + subtask.getEpicId() + " не найден");
        }
        subtask.setId(currentId++);  // Используем setter вместо прямого присваивания
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
        updateEpicStatus(subtask.getEpicId());
        return subtask.getId();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void updateSubtaskStatus(int id, Status status) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            subtask.setStatus(status);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic.getId());
            }
            historyManager.remove(id);
        }
    }

    @Override
    public List<Subtask> getAllSubtasksForEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return Collections.emptyList();
        List<Subtask> result = new ArrayList<>();
        for (int subId : epic.getSubtaskIds()) {
            Subtask sub = subtasks.get(subId);
            if (sub != null) {
                result.add(sub);
            }
        }
        return result;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void clearAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        historyManager.getHistory().clear();
        currentId = 1;
    }

    private void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Subtask> epicSubtasks = getAllSubtasksForEpic(epicId);
        if (epicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask sub : epicSubtasks) {
            if (sub.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (sub.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
