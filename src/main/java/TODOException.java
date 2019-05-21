public class TODOException extends Exception {
  public TODOException(int id) {
    super("Task " + id + " does not exists");
  }
}
