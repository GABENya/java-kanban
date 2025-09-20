public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создаем задачи
        int task1Id = manager.addTask(new Task(0, "Задача 1", "Описание задачи 1"));
        int task2Id = manager.addTask(new Task(0, "Задача 2", "Описание задачи 2"));

        // Создаем эпик
        Epic epic1 = new Epic(0, "Эпик 1", "Описание эпика 1");
        int epic1Id = manager.addEpic(epic1);

        // Создаем подзадачи для эпика
        Subtask sub1 = new Subtask(0, "Подзадача 1", "Описание подзадачи 1", epic1Id);
        int sub1Id = manager.addSubtask(sub1);
        Subtask sub2 = new Subtask(0, "Подзадача 2", "Описание подзадачи 2", epic1Id);
        int sub2Id = manager.addSubtask(sub2);

        // Вывод текущего состояния
        System.out.println("Начальное состояние:");
        manager.printAllTasks();

        // Обновляем статус подзадач
        manager.updateTaskStatus(sub1Id, Status.DONE);
        manager.updateTaskStatus(sub2Id, Status.DONE);

        System.out.println("После завершения подзадач:");
        manager.printAllTasks();

        // Меняем статус одной подзадачи обратно на IN_PROGRESS
        manager.updateTaskStatus(sub1Id, Status.IN_PROGRESS);
        System.out.println("После изменения статуса подзадачи 1 на IN_PROGRESS:");
        manager.printAllTasks();

        // Удаляем подзадачу 2
        manager.deleteSubtask(sub2Id);
        System.out.println("После удаления подзадачи 2:");
        manager.printAllTasks();

        // Обновляем статус подзадачи 1 на DONE
        manager.updateTaskStatus(sub1Id, Status.DONE);
        System.out.println("После завершения подзадачи 1:");
        manager.printAllTasks();

        // Удаляем эпик
        manager.deleteEpic(epic1Id);
        System.out.println("После удаления эпика:");
        manager.printAllTasks();
    }
}