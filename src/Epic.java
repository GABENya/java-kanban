import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String title, String description) {
        super(id, title, description);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }
}
