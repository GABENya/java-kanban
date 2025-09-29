package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tasks.Epic;  // Добавлен импорт для Epic из пакета tasks
import tasks.Subtask;  // Добавлен импорт для Subtask из пакета tasks

public class TaskTest {

    @Test
    void tasksAreEqualIfIdsAreEqual() {
        Task t1 = new Task(1, "Task1", "Description1");
        Task t2 = new Task(1, "Task2", "Description2");
        assertEquals(t1, t2, "Задачи должны быть равны, если их ID совпадает");
    }

    @Test
    void epicsAreEqualIfIdsAreEqual() {
        Epic e1 = new Epic(2, "Epic1", "tasks.Epic Description1");
        Epic e2 = new Epic(2, "Epic2", "tasks.Epic Description2");
        assertEquals(e1, e2, "Эпики должны быть равны, если их ID совпадает");
    }

    @Test
    void subtasksAreEqualIfIdsAreEqual() {
        Subtask s1 = new Subtask(3, "Subtask1", "tasks.Subtask Description1", 2);
        Subtask s2 = new Subtask(3, "Subtask2", "tasks.Subtask Description2", 2);
        assertEquals(s1, s2, "Подзадачи должны быть равны, если их ID совпадает");
    }

    @Test
    void tasksWithDifferentIdsAreNotEqual() {
        Task t1 = new Task(1, "Task1", "Description1");
        Task t2 = new Task(2, "Task2", "Description2");
        assertNotEquals(t1, t2, "Задачи с разными ID не должны быть равны");
    }
}
