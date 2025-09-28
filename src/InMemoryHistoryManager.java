import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int MAX_HISTORY_SIZE = 10;
    private final Map<Integer, CustomLinkedList.Node> historyMap = new HashMap<>();
    private final CustomLinkedList historyList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) return;
        if (historyMap.containsKey(task.getId())) {
            historyList.removeNode(historyMap.get(task.getId()));
        }
        if (historyList.size() >= MAX_HISTORY_SIZE) {
            CustomLinkedList.Node first = historyList.removeFirst();
            if (first != null) {
                historyMap.remove(first.task.getId());
            }
        }
        CustomLinkedList.Node newNode = historyList.linkLast(task);
        historyMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        CustomLinkedList.Node node = historyMap.remove(id);
        if (node != null) {
            historyList.removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        for (Task task : historyList) {
            history.add(task);
        }
        return history;
    }

    // Внутренний класс для связного списка истории
    private static class CustomLinkedList implements Iterable<Task> {
        private Node first;
        private Node last;
        private int size = 0;

        public Node linkLast(Task task) {
            final Node oldLast = last;
            final Node newNode = new Node(oldLast, task, null);
            last = newNode;
            if (oldLast == null)
                first = newNode;
            else
                oldLast.next = newNode;
            size++;
            return newNode;
        }

        public Node removeFirst() {
            if (first == null) return null;
            Node removed = first;
            first = first.next;
            if (first == null)
                last = null;
            else
                first.prev = null;
            size--;
            return removed;
        }

        public void removeNode(Node node) {
            if (node == null) return;
            if (node.prev == null) {
                first = node.next;
            } else {
                node.prev.next = node.next;
            }
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
            size--;
        }

        public int size() {
            return size;
        }

        @Override
        public Iterator<Task> iterator() {
            return new Iterator<Task>() {
                private Node current = first;

                @Override
                public boolean hasNext() {
                    return current != null;
                }

                @Override
                public Task next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Task task = current.task;
                    current = current.next;
                    return task;
                }
            };
        }

        // Внутренний класс узла
        static class Node {
            Task task;
            Node prev;
            Node next;

            public Node(Node prev, Task task, Node next) {
                this.prev = prev;
                this.task = task;
                this.next = next;
            }
        }
    }
}