

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    @Test
    void getDefaultTaskManagerReturnsNonNull() {
        TaskManager manager = Managers.getDefaultTaskManager();
        assertNotNull(manager, "Managers.getDefaultTaskManager() должен возвращать не null");
    }

    @Test
    void getDefaultHistoryReturnsNonNull() {
        HistoryManager history = Managers.getDefaultHistory();
        assertNotNull(history, "Managers.getDefaultHistory() должен возвращать не null");
    }

    @Test
    void getDefaultTaskManagerHasEmptyTaskList() {
        TaskManager manager = Managers.getDefaultTaskManager();
        assertTrue(manager.getAllTasks().isEmpty(), "TaskManager должен иметь пустой список задач при инициализации");
    }

    @Test
    void getDefaultHistoryHasEmptyHistory() {
        HistoryManager history = Managers.getDefaultHistory();
        assertTrue(history.getHistory().isEmpty(), "HistoryManager должен иметь пустую историю при инициализации");
    }
}