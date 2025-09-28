
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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
        Task task = new Task(0, "Task A", "Description A");
        int taskId = manager.addTask(task);
        Task retrieved = manager.getTask(taskId);
        assertEquals(taskId, retrieved.getId(), "ID добавленной задачи должен совпадать с полученной");
        assertEquals("Task A", retrieved.getTitle(), "Заголовок задачи должен совпадать");
        assertEquals("Description A", retrieved.getDescription(), "Описание задачи должно совпадать");
        assertEquals(Status.NEW, retrieved.getStatus(), "Статус новой задачи должен быть NEW");
    }

    @Test
    void addAndRetrieveEpic() {
        Epic epic = new Epic(0, "Epic A", "Epic Description A");
        int epicId = manager.addEpic(epic);
        Epic retrieved = manager.getEpic(epicId);
        assertEquals(epicId, retrieved.getId(), "ID добавленного эпика должен совпадать с полученным");
        assertEquals("Epic A", retrieved.getTitle(), "Заголовок эпика должен совпадать");
        assertEquals("Epic Description A", retrieved.getDescription(), "Описание эпика должно совпадать");
        assertEquals(Status.NEW, retrieved.getStatus(), "Статус нового эпика должен быть NEW");
    }

    @Test
    void addAndRetrieveSubtask() {
        Epic epic = new Epic(0, "Epic B", "Epic Description B");
        int epicId = manager.addEpic(epic);
        Subtask subtask = new Subtask(0, "Subtask A", "Subtask Description A", epicId);
        int subtaskId = manager.addSubtask(subtask);
        Subtask retrieved = manager.getSubtask(subtaskId);
        assertEquals(subtaskId, retrieved.getId(), "ID добавленной подзадачи должен совпадать с полученным");
        assertEquals("Subtask A", retrieved.getTitle(), "Заголовок подзадачи должен совпадать");
        assertEquals("Subtask Description A", retrieved.getDescription(), "Описание подзадачи должно совпадать");
        assertEquals(epicId, retrieved.getEpicId(), "Epic ID подзадачи должен совпадать");
        assertEquals(Status.NEW, retrieved.getStatus(), "Статус новой подзадачи должен быть NEW");
    }

    @Test
    void addingSubtaskToNonExistentEpicThrowsException() {
        Subtask subtask = new Subtask(0, "Subtask B", "Subtask Description B", 999);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addSubtask(subtask);
        });
        String expectedMessage = "Эпик с ID 999 не найден";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Должно выбрасываться исключение при добавлении подзадачи к несуществующему эпику");
    }

    @Test
    void updateTaskStatus() {
        Task task = new Task(0, "Task B", "Description B");
        int taskId = manager.addTask(task);
        manager.updateTaskStatus(taskId, Status.IN_PROGRESS);
        Task updated = manager.getTask(taskId);
        assertEquals(Status.IN_PROGRESS, updated.getStatus(), "Статус задачи должен обновиться на IN_PROGRESS");
    }

    @Test
    void updateSubtaskStatusUpdatesEpicStatus() {
        Epic epic = new Epic(0, "Epic C", "Epic Description C");
        int epicId = manager.addEpic(epic);
        Subtask sub1 = new Subtask(0, "Subtask C1", "Subtask Description C1", epicId);
        Subtask sub2 = new Subtask(0, "Subtask C2", "Subtask Description C2", epicId);
        int sub1Id = manager.addSubtask(sub1);
        int sub2Id = manager.addSubtask(sub2);

        // Все подзадачи NEW, эпик должен быть NEW
        Epic retrievedEpic = manager.getEpic(epicId);
        assertEquals(Status.NEW, retrievedEpic.getStatus(), "Эпик должен быть в статусе NEW, если все подзадачи NEW");

        // Обновляем одну подзадачу на IN_PROGRESS, эпик должен стать IN_PROGRESS
        manager.updateSubtaskStatus(sub1Id, Status.IN_PROGRESS);
        retrievedEpic = manager.getEpic(epicId);
        assertEquals(Status.IN_PROGRESS, retrievedEpic.getStatus(), "Эпик должен быть в статусе IN_PROGRESS, если подзадача IN_PROGRESS");

        // Обновляем подзадачи на DONE, эпик должен стать DONE
        manager.updateSubtaskStatus(sub1Id, Status.DONE);
        manager.updateSubtaskStatus(sub2Id, Status.DONE);
        retrievedEpic = manager.getEpic(epicId);
        assertEquals(Status.DONE, retrievedEpic.getStatus(), "Эпик должен быть в статусе DONE, если все подзадачи DONE");
    }

    @Test
    void deletingTaskRemovesItFromManager() {
        Task task = new Task(0, "Task C", "Description C");
        int taskId = manager.addTask(task);
        assertNotNull(manager.getTask(taskId), "Задача должна существовать после добавления");
        manager.deleteTask(taskId);
        assertNull(manager.getTask(taskId), "Задача должна быть удалена из менеджера");
    }

    @Test
    void deletingEpicRemovesAllItsSubtasks() {
        Epic epic = new Epic(0, "Epic D", "Epic Description D");
        int epicId = manager.addEpic(epic);
        Subtask sub1 = new Subtask(0, "Subtask D1", "Subtask Description D1", epicId);
        Subtask sub2 = new Subtask(0, "Subtask D2", "Subtask Description D2", epicId);
        int sub1Id = manager.addSubtask(sub1);
        int sub2Id = manager.addSubtask(sub2);

        assertNotNull(manager.getSubtask(sub1Id), "Подзадача 1 должна существовать после добавления");
        assertNotNull(manager.getSubtask(sub2Id), "Подзадача 2 должна существовать после добавления");

        manager.deleteEpic(epicId);

        assertNull(manager.getEpic(epicId), "Эпик должен быть удалён");
        assertNull(manager.getSubtask(sub1Id), "Подзадача 1 должна быть удалена вместе с эпиκом");
        assertNull(manager.getSubtask(sub2Id), "Подзадача 2 должна быть удалена вместе с эпиķом");
    }

    @Test
    void getHistoryReturnsCorrectSequence() {
        Task task1 = new Task(0, "Task D1", "Description D1");
        Task task2 = new Task(0, "Task D2", "Description D2");
        int id1 = manager.addTask(task1);
        int id2 = manager.addTask(task2);

        manager.getTask(id1);
        manager.getTask(id2);
        manager.getTask(id1); // Повторный вызов

        List<Task> history = manager.getHistory();
        assertEquals(2, history.size(), "История должна содержать только уникальные просмотры");
        assertEquals(id1, history.get(0).getId(), "Последний просмотр должен быть первым в истории");
        assertEquals(id2, history.get(1).getId(), "Первый просмотр должен быть вторым в истории");
    }
}