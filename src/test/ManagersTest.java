package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import managers.HistoryManager;  // Добавлен импорт для HistoryManager из пакета managers
import managers.Managers;  // Добавлен импорт для Managers из пакета managers
import managers.TaskManager;  // Добавлен импорт для TaskManager из пакета managers

public class ManagersTest {

    @Test
    void getDefaultTaskManagerReturnsNonNull() {
        TaskManager manager = Managers.getDefaultTaskManager();
        assertNotNull(manager, "managers.Managers.getDefaultTaskManager() должен возвращать не null");
    }

    @Test
    void getDefaultHistoryReturnsNonNull() {
        HistoryManager history = Managers.getDefaultHistory();
        assertNotNull(history, "managers.Managers.getDefaultHistory() должен возвращать не null");
    }

    @Test
    void getDefaultTaskManagerHasEmptyTaskList() {
        TaskManager manager = Managers.getDefaultTaskManager();
        Assertions.assertTrue(manager.getAllTasks().isEmpty(), "managers.TaskManager должен иметь пустой список задач при инициализации");
    }

    @Test
    void getDefaultHistoryHasEmptyHistory() {
        HistoryManager history = Managers.getDefaultHistory();
        Assertions.assertTrue(history.getHistory().isEmpty(), "managers.HistoryManager должен иметь пустую историю при инициализации");
    }
}
