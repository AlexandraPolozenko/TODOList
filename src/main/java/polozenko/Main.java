package polozenko;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    TODOList todoList = new TODOList();

    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Please, enter command");
      String cmd = scanner.nextLine();

      try {
        switch (cmd) {
          case "show": {
            todoList.readTODOList();
            break;
          }

          case "done": {
            System.out.println("Enter id of done task:");
            int id = Integer.parseInt(scanner.nextLine());

            todoList.markAsDone(id);
            break;
          }

          case "add": {
            System.out.println("Enter task description:");
            String description = scanner.nextLine();

            todoList.addNewTask(description);
            break;
          }

          case "delete": {
            System.out.println("Enter id of task to delete:");
            int id = Integer.parseInt(scanner.nextLine());

            todoList.deleteTask(id);
            break;
          }

          case "filter": {
            todoList.filterTODOList();

            break;
          }

          case "help": {
            todoList.printHelpMessage();

            break;
          }

          case "exit": {
            todoList.finishSession();
            return;
          }

          default: {
            System.out.println("Command does not exists");
            todoList.printHelpMessage();
            break;
          }
        }
      } catch (TODOException e) {
        System.err.println(e);
      }
    }
  }
}
