package polozenko;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TODOListTests {
  TODOList todoList = new TODOList();

  @Test
  public void taskListIsNotNull() {
    Assertions.assertNotNull(todoList.list);
  }

  @Test
  public void markAsDoneFirstTask() {
    try {
      todoList.markAsDone(0);

      JSONObject task = (JSONObject) todoList.list.get(0);
      Assertions.assertTrue((Boolean) task.get("done"));
    } catch (TODOException e) {
      Assertions.assertNull(todoList.list.get(0));
    }
  }

  @Test
  public void deleteFirstTask() {
    try {
      todoList.deleteTask(0);

      Assertions.assertNull(todoList.list.get(0));
    } catch (TODOException e) {
      Assertions.assertNull(todoList.list.get(0));
    }
  }
}
