package polozenko;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TODOList {
  static JSONArray list;

  TODOList() {
    try {
      Object obj = new JSONParser().parse(new FileReader("todo-list.json"));
      list = (JSONArray) obj;
    } catch (IOException e) {
      System.err.println("todo-list file does not exists or incorrect");
    } catch (ParseException e) {
      System.err.println("todo-list should be written in further format:");
      printTODOFormat();
    }
  }


  public void readTODOList() {
    int taskCnt = 0;
    for (Object i : list) {
      taskCnt = checkPages(taskCnt);
      if (taskCnt == -1) {
        break;
      }

      if (i != null) {
        JSONObject task = (JSONObject) i;

        printTask(task);
      }
    }
  }

  public void markAsDone(int id) throws TODOException {
    try {
      JSONObject task = (JSONObject) list.get(id);
      JSONObject newTask = new JSONObject();

      newTask.put("id", id);
      newTask.put("description", task.get("description"));
      newTask.put("done", true);

      list.set(id, newTask);

      System.out.println("Task " + id + " done\n");
    } catch (NullPointerException | IndexOutOfBoundsException e){
      throw new TODOException(id);
    }
  }

  public void addNewTask(String description) {
    JSONObject task = new JSONObject();
    int id = list.size();

    task.put("id", id);
    task.put("description", description);
    task.put("done", false);

    list.add(task);

    System.out.println("Task " + id + " added\n");
  }

  public void deleteTask(int id) throws TODOException {
    try {
      if (list.get(id) == null) {
        throw new TODOException(id);
      }

      list.set(id, null);
      System.out.println("Task " + id + " deleted\n");
    } catch (NullPointerException | IndexOutOfBoundsException e) {
      throw new TODOException(id);
    }
  }

  public void filterTODOList() {
    int taskCnt = 0;
    for (Object i: list) {
      taskCnt = checkPages(taskCnt);
      if (taskCnt == -1) {
        break;
      }

      JSONObject task = (JSONObject) i;

      if (!(Boolean) task.get("done")) {
        printTask(task);
      }
    }
  }

  public void finishSession() {
    File file = new File("todo-list.json");
    file.delete();
    int counter = 0;

    try {
      file.createNewFile();
      BufferedWriter writer = Files.newBufferedWriter(Paths.get("todo-list.json"));

      writer.write("[\n");
      for (Object i : list) {
        if (counter != 0) {
          writer.write(",\n");
        }
        if (i != null) {
          JSONObject task = (JSONObject) i;
          task.put("id", counter);
          counter++;

          writer.write(task.toJSONString());
        }
      }
      writer.write("\n]\n");

      writer.close();
      System.out.println("Session finished");

    } catch (IOException e) {
      System.err.println("Unable to finish session");
    }
  }

  private int checkPages(int taskCnt) {
    if (taskCnt == 100) {
      System.out.println("Would you like to see next 100 tasks? (yes/no) total - " + list.size() + " tasks");

      Scanner scanner = new Scanner(System.in);
      if (scanner.nextLine().equals("no")) {
        return -1;
      } else {
        return 0;
      }
    }

    return taskCnt + 1;
  }

  private void printTask(JSONObject task) {
    System.out.println("Task " + task.get("id"));
    System.out.println(task.get("description"));
    if ((Boolean) task.get("done")) {
      System.out.println("Task done");
    } else {
      System.out.println("In progress");
    }

    System.out.println();
  }

  private void printTODOFormat() {
    System.out.println("[\n  {");
    System.out.println("    \"id\": int");
    System.out.println("    \"description\": String");
    System.out.println("    \"done\": bool");
    System.out.println("  }\n]");
  }

  public void printHelpMessage() {
    System.out.println("Help:");
    System.out.println("show - show existing tasks");
    System.out.println("done - mark task as done");
    System.out.println("add - add new task");
    System.out.println("delete - delete existing task");
    System.out.println("filter - show only tasks which are in progress");
    System.out.println("help - print help information");
    System.out.println("exit - finish session\n");
  }
}
