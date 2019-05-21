import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TODOList {
  static JSONArray list;

  public static void main(String[] args) {
    try {
      Object obj = new JSONParser().parse(new FileReader("todo-list.json"));
      System.out.println("file readed");
      list = (JSONArray) obj;

      if (list.size() > 1000) {
        System.err.println("Too big todo list");
      }

      Scanner scanner = new Scanner(System.in);
      while (true) {
        System.out.println("Please, enter command");
        String cmd = scanner.nextLine();

        try {
          switch (cmd) {
            case "show": {
              readTODOList();
              break;
            }

            case "done": {
              System.out.println("Enter id of done task:");
              int id = Integer.parseInt(scanner.nextLine());

              markAsDone(id);
              break;
            }

            case "add": {
              System.out.println("Enter task description:");
              String description = scanner.nextLine();

              addNewTask(description);
              break;
            }

            case "delete": {
              System.out.println("Enter id of task to delete:");
              int id = Integer.parseInt(scanner.nextLine());

              deleteTask(id);
              break;
            }

            case "exit": {
              finishSession();
              return;
            }

            default: {
              System.out.println("Command does not exists");
              printHelpMessage();
              break;
            }
          }
        } catch (TODOException e) {
          System.err.println(e);
        }
      }

    } catch (Exception e) {
      System.err.println("todo-list file does not exists or incorrect");
      System.err.println("todo-list should be written in further format:");
      printTODOFormat();
    }
  }


  public static void readTODOList() {
    for (Object i : list) {
      if (i != null) {
        JSONObject task = (JSONObject) i;

        System.out.println("Task " + task.get("id"));
        System.out.println(task.get("description"));
        if ((Boolean) task.get("done")) {
          System.out.println("Task done");
        } else {
          System.out.println("In progress");
        }

        System.out.println();
      }
    }
  }

  public static void markAsDone(int id) throws TODOException {
    try {
      JSONObject task = (JSONObject) list.get(id);
      JSONObject newTask = new JSONObject();

      newTask.put("id", id);
      newTask.put("description", task.get("description"));
      newTask.put("done", true);

      list.set(id, newTask);

      System.out.println("Task " + id + " done\n");
    } catch (Exception e){
      throw new TODOException(id);
    }
  }

  public static void addNewTask(String description) {
    JSONObject task = new JSONObject();
    int id = list.size();

    task.put("id", id);
    task.put("description", description);
    task.put("done", false);

    list.add(task);

    System.out.println("Task " + id + " added\n");
  }

  public static void deleteTask(int id) throws TODOException {
    try {
      if (list.get(id) == null) {
        throw new TODOException(id);
      }

      list.set(id, null);
      System.out.println("Task " + id + " deleted\n");
    } catch (Exception e) {
      throw new TODOException(id);
    }
  }

  public static void finishSession() {
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

    } catch (Exception e) {
      System.err.println("Unable to finish session");
    }
  }

  private static void printTODOFormat() {
    System.out.println("[\n  {");
    System.out.println("    \"id\": int");
    System.out.println("    \"description\": String");
    System.out.println("    \"done\": bool");
    System.out.println("  }\n]");
  }

  private static void printHelpMessage() {
    System.out.println("Help:");
    System.out.println("show - show existing tasks");
    System.out.println("done - mark task as done");
    System.out.println("add - add new task");
    System.out.println("delete - delete existing task");
    System.out.println("exit - finish session\n");
  }
}
