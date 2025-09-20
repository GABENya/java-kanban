import java.util.*;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();

    private int idCounter = 1;

    private int generateId() {
        return idCounter++;
    }

    public int addTask(Task task) {
        task.id = generateId();
        tasks.put(task.id, task);
        return task.id;
    }

    public int addEpic(Epic epic) {
        epic.id = generateId();
        epics.put(epic.id, epic);
        return epic.id;
    }

    public int addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())) {
            throw new IllegalArgumentException("Эпик с таким ID не существует");
        }
        subtask.id = generateId();
        subtasks.put(subtask.id, subtask);
        // Обновляем эпик
        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.id);
        updateEpicStatus(epic);
        return subtask.id;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.id)) {
            tasks.put(task.id, task);
            if (task instanceof Subtask) {
                Epic epic = epics.get(((Subtask) task).getEpicId());
                updateEpicStatus(epic);
            }
        } else {
            throw new NoSuchElementException("Задача с таким ID не найдена");
        }
    }

    public void updateTaskStatus(int taskId, Status status) {
        if (tasks.containsKey(taskId)) {
            Task task = tasks.get(taskId);
            task.setStatus(status);
            if (task instanceof Subtask) {
                Epic epic = epics.get(((Subtask) task).getEpicId());
                updateEpicStatus(epic);
            }
        } else {
            throw new NoSuchElementException("Задача с таким ID не найдена");
        }
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
            }
            epics.remove(id);
        }
    }

    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            int epId = subtask.getEpicId();
            if (epics.containsKey(epId)) {
                epics.get(epId).removeSubtaskId(id);
                updateEpicStatus(epics.get(epId));
            }
            subtasks.remove(id);
        }
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    private void updateEpicStatus(Epic epic) {
        if (epic == null || epic.getSubtaskIds().isEmpty()) {
            if (epic != null) {
                epic.setStatus(Status.NEW);
            }
            return;
        }
        boolean allNew = true;
        boolean allDone = true;
        for (int subId : epic.getSubtaskIds()) {
            Subtask sub = subtasks.get(subId);
            if (sub != null) {
                if (sub.getStatus() != Status.DONE) {
                    allDone = false;
                }
                if (sub.getStatus() != Status.NEW) {
                    allNew = false;
                }
            }
        }
        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    public void printAllTasks() {
        System.out.println("Задачи:");
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
        System.out.println("\nЭпики:");
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
        System.out.println("\nПодзадачи:");
        for (Subtask sub : subtasks.values()) {
            System.out.println(sub);
        }
        System.out.println();
    }
}