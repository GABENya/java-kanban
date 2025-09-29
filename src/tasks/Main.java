package tasks;

import managers.Managers;
import managers.TaskManager;
import test.Task;  // Добавлен импорт для Task из пакета test

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefaultTaskManager();

        // Добавление задач
        Task task1 = new Task(0, "Task1", "Description1");
        int task1Id = manager.addTask(task1);

        Epic epic1 = new Epic(0, "Epic1", "tasks.Epic Description1");
        int epic1Id = manager.addEpic(epic1);

        Subtask subtask1 = new Subtask(0, "Subtask1", "tasks.Subtask Description1", epic1Id);
        int subtask1Id = manager.addSubtask(subtask1);

        // Получение и вывод задач
        System.out.println("Tasks:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nEpics:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }

        System.out.println("\nSubtasks:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        // Просмотр истории
        manager.getTask(task1Id);
        manager.getEpic(epic1Id);
        manager.getSubtask(subtask1Id);

        System.out.println("\nHistory:");
        for (Task historyTask : manager.getHistory()) {
            System.out.println(historyTask);
        }
    }
}
