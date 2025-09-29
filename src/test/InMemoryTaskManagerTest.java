package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import managers.HistoryManager;  // Добавлен импорт для HistoryManager из пакета managers
import managers.Managers;  // Добавлен импорт для Managers из пакета managers
import managers.TaskManager;  // Добавлен импорт для TaskManager из пакета managers
import tasks.Epic;  // Добавлен импорт для Epic из пакета tasks
import tasks.Status;  // Добавлен импорт для Status из пакета tasks
import tasks.Subtask;  // Добавлен импорт для Subtask из пакета tasks
import java.util.List;

public class InMemoryTaskManagerTest {
    private TaskManager manager;
    private HistoryManager history;

    @BeforeEach
    void setup() {
        history = Managers.getDefaultHistory();
        manager = Managers.getDefaultTaskManager();
    }

    @Test
    void addAndRetrieveTask() {
        Task task = new Task(0, "test.Task A", "Description A");
        int taskId = manager.addTask(task);
        Task retrieved = manager.getTask(taskId);
        Assertions.assertEquals(taskId, retrieved.getId(), "ID добавленной задачи должен совпадать с полученной");
        Assertions.assertEquals("test.Task A", retrieved.getTitle(), "Заголовок задачи должен совпадать");
        Assertions.assertEquals("Description A", retrieved.getDescription(), "Описание задачи должно совпадать");
        Assertions.assertEquals(Status.NEW, retrieved.getStatus(), "Статус новой задачи должен быть NEW");
    }

    @Test
    void addAndRetrieveEpic() {
        Epic epic = new Epic(0, "tasks.Epic A", "tasks.Epic Description A");
        int epicId = manager.addEpic(epic);
        Epic retrieved = manager.getEpic(epicId);
        Assertions.assertEquals(epicId, retrieved.getId(), "ID добавленного эпика должен совпадать с полученным");
        Assertions.assertEquals("tasks.Epic A", retrieved.getTitle(), "Заголовок эпика должен совпадать");
        Assertions.assertEquals("tasks.Epic Description A", retrieved.getDescription(), "Описание эпика должно совпадать");
        Assertions.assertEquals(Status.NEW, retrieved.getStatus(), "Статус нового эпика должен быть NEW");
    }

    @Test
    void addAndRetrieveSubtask() {
        Epic epic = new Epic(0, "tasks.Epic B", "tasks.Epic Description B");
        int epicId = manager.addEpic(epic);
        Subtask subtask = new Subtask(0, "tasks.Subtask A", "tasks.Subtask Description A", epicId);
        int subtaskId = manager.addSubtask(subtask);
        Subtask retrieved = manager.getSubtask(subtaskId);
        Assertions.assertEquals(subtaskId, retrieved.getId(), "ID добавленной подзадачи должен совпадать с полученным");
        Assertions.assertEquals("tasks.Subtask A", retrieved.getTitle(), "Заголовок подзадачи должен совпадать");
        Assertions.assertEquals("tasks.Subtask Description A", retrieved.getDescription(), "Описание подзадачи должно совпадать");
        Assertions.assertEquals(epicId, retrieved.getEpicId(), "tasks.Epic ID подзадачи должен совпадать");
        Assertions.assertEquals(Status.NEW, retrieved.getStatus(), "Статус новой подзадачи должен быть NEW");
    }

    @Test
    void addingSubtaskToNonExistentEpicThrowsException() {
        Subtask subtask = new Subtask(0, "tasks.Subtask B", "tasks.Subtask Description B", 999);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addSubtask(subtask);
        });
        String expectedMessage = "Эпик с ID 999 не найден";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Должно выбрасываться исключение при добавлении подзадачи к несуществующему эпику");
    }

    @Test
    void updateTaskStatus() {
        Task task = new Task(0, "test.Task B", "Description B");
        int taskId = manager.addTask(task);
        manager.updateTaskStatus(taskId, Status.IN_PROGRESS);
        Task updated = manager.getTask(taskId);
        Assertions.assertEquals(Status.IN_PROGRESS, updated.getStatus(), "Статус задачи должен обновиться на IN_PROGRESS");
    }

    @Test
    void updateSubtaskStatusUpdatesEpicStatus() {
        Epic epic = new Epic(0, "tasks.Epic C", "tasks.Epic Description C");
        int epicId = manager.addEpic(epic);
        Subtask sub1 = new Subtask(0, "tasks.Subtask C1", "tasks.Subtask Description C1", epicId);
        Subtask sub2 = new Subtask(0, "tasks.Subtask C2", "tasks.Subtask Description C2", epicId);
        int sub1Id = manager.addSubtask(sub1);
        int sub2Id = manager.addSubtask(sub2);

        // Все подзадачи NEW, эпик должен быть NEW
        Epic retrievedEpic = manager.getEpic(epicId);
        Assertions.assertEquals(Status.NEW, retrievedEpic.getStatus(), "Эпик должен быть в статусе NEW, если все подзадачи NEW");

        // Обновляем одну подзадачу на IN_PROGRESS, эпик должен стать IN_PROGRESS
        manager.updateSubtaskStatus(sub1Id, Status.IN_PROGRESS);
        retrievedEpic = manager.getEpic(epicId);
        Assertions.assertEquals(Status.IN_PROGRESS, retrievedEpic.getStatus(), "Эпик должен быть в статусе IN_PROGRESS, если подзадача IN_PROGRESS");

        // Обновляем подзадачи на DONE, эпик должен стать DONE
        manager.updateSubtaskStatus(sub1Id, Status.DONE);
        manager.updateSubtaskStatus(sub2Id, Status.DONE);
        retrievedEpic = manager.getEpic(epicId);
        Assertions.assertEquals(Status.DONE, retrievedEpic.getStatus(), "Эпик должен быть в статусе DONE, если все подзадачи DONE");
    }

    @Test
    void deletingTaskRemovesItFromManager() {
        Task task = new Task(0, "test.Task C", "Description C");
        int taskId = manager.addTask(task);
        Assertions.assertNotNull(manager.getTask(taskId), "Задача должна существовать после добавления");
        manager.deleteTask(taskId);
        Assertions.assertNull(manager.getTask(taskId), "Задача должна быть удалена из менеджера");
    }

    @Test
    void deletingEpicRemovesAllItsSubtasks() {
        Epic epic = new Epic(0, "tasks.Epic D", "tasks.Epic Description D");
        int epicId = manager.addEpic(epic);
        Subtask sub1 = new Subtask(0, "tasks.Subtask D1", "tasks.Subtask Description D1", epicId);
        Subtask sub2 = new Subtask(0, "tasks.Subtask D2", "tasks.Subtask Description D2", epicId);
        int sub1Id = manager.addSubtask(sub1);
        int sub2Id = manager.addSubtask(sub2);

        Assertions.assertNotNull(manager.getSubtask(sub1Id), "Подзадача 1 должна существовать после добавления");
        Assertions.assertNotNull(manager.getSubtask(sub2Id), "Подзадача 2 должна существовать после добавления");

        manager.deleteEpic(epicId);

        Assertions.assertNull(manager.getEpic(epicId), "Эпик должен быть удалён");
        Assertions.assertNull(manager.getSubtask(sub1Id), "Подзадача 1 должна быть удалена вместе с эпиκом");
        Assertions.assertNull(manager.getSubtask(sub2Id), "Подзадача 2 должна быть удалена вместе с эпиķом");
    }

    @Test
    void getHistoryReturnsCorrectSequence() {
        Task task1 = new Task(0, "test.Task D1", "Description D1");
        Task task2 = new Task(0, "test.Task D2", "Description D2");
        int id1 = manager.addTask(task1);
        int id2 = manager.addTask(task2);

        manager.getTask(id1);
        manager.getTask(id2);
        manager.getTask(id1); // Повторный вызов

        List<Task> history = manager.getHistory();
        assertEquals(2, history.size(), "История должна содержать только уникальные просмотры");
        Assertions.assertEquals(id1, history.get(0).getId(), "Последний просмотр должен быть первым в истории");
        Assertions.assertEquals(id2, history.get(1).getId(), "Первый просмотр должен быть вторым в истории");
    }
}
