
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class HistoryManagerTest {
    private HistoryManager history;

    @BeforeEach
    void setup() {
        history = Managers.getDefaultHistory();
    }

    @Test
    void addAndRetrieveHistory() {
        Task task1 = new Task(1, "History Task1", "History Description1");
        Task task2 = new Task(2, "History Task2", "History Description2");
        history.add(task1);
        history.add(task2);

        assertEquals(2, history.getHistory().size(), "История должна содержать два элемента");
        assertTrue(history.getHistory().contains(task1), "История должна содержать task1");
        assertTrue(history.getHistory().contains(task2), "История должна содержать task2");
    }

    @Test
    void addDuplicateTaskDoesNotCreateMultipleEntries() {
        Task task = new Task(3, "Duplicate Task", "Duplicate Description");
        history.add(task);
        history.add(task);

        assertEquals(1, history.getHistory().size(), "История не должна содержать дубликатов одной и той же задачи");
    }

    @Test
    void historyDoesNotExceedMaxSize() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task(i, "Task " + i, "Description " + i);
            history.add(task);
        }

        assertEquals(InMemoryHistoryManager.MAX_HISTORY_SIZE, history.getHistory().size(),
                "История должна ограничиваться максимальным размером");
        // Проверим, что первые задачи удалены
        for (int i = 1; i <= 5; i++) {
            Task task = new Task(i, "Task " + i, "Description " + i);
            assertFalse(history.getHistory().contains(task), "Первоначальные задачи должны быть удалены из истории");
        }
    }

    @Test
    void getHistoryReturnsCopyNotOriginalList() {
        Task task = new Task(4, "Immutable Task", "Immutable Description");
        history.add(task);
        List<Task> retrievedHistory = history.getHistory();
        retrievedHistory.add(new Task(5, "Fake Task", "Fake Description"));

        assertEquals(1, history.getHistory().size(), "Изменения в возвращаемом списке не должны влиять на внутреннюю историю");
    }

    @Test
    void addingNullDoesNothing() {
        history.add(null);
        assertTrue(history.getHistory().isEmpty(), "Добавление null не должно изменять историю");
    }
}