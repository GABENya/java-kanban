package managers;

import java.util.List;
import test.Task;  // Добавлен импорт для Task из пакета test

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
