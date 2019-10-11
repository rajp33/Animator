package cs3500.animator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cs3500.animator.controller.AnimationController;
import cs3500.animator.controller.Controller;

/**
 * The 'main' class. Represents a fully functional animator application. Handles arguments and
 * parameters.
 */
public class Excellence {

  /**
   * Runs the animator with the given arguments.
   *
   * @param args arguments
   */
  public static void main(String[] args) {
    String viewType = null;
    String inputName = null;
    String outputName = null;
    int speed = 1;
    Map<String, String> argumentPair = new HashMap<>();

    //HANDLE ARGUMENTS
    if (args.length % 2 != 0) {
      throw new IllegalArgumentException("Invalid input: " + Arrays.deepToString(args));
    }
    for (int i = 0; i < args.length; i += 2) {
      if (argumentPair.containsKey(args[i])) {
        throw new IllegalArgumentException("The argument: " + args[i] + " cannot be given twice");
      }
      argumentPair.put(args[i], args[i + 1]);
    }

    for (String argument : argumentPair.keySet()) {
      switch (argument) {
        case "-view":
          viewType = argumentPair.get(argument);
          break;
        case "-in":
          inputName = argumentPair.get(argument);
          break;
        case "-out":
          outputName = argumentPair.get(argument);
          break;
        case "-speed":
          try {
            speed = Integer.parseInt(argumentPair.get(argument));
          } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Speed must be an integer");
          }
          break;
        default:
          throw new IllegalArgumentException("The argument: " + argument + " is not valid");
      }
    }
    if (viewType == null || inputName == null) {
      throw new IllegalArgumentException("-view and -in must be used as arguments");
    }

    //READ FILE
    Readable doc = readFile(inputName);

    AnimationController controller = new Controller(doc, viewType, inputName, outputName, speed);
    controller.runController();
  }

  /**
   * Read from the file with the given name. The file must exist in the directory of this program.
   *
   * @param name name of the file to read from.
   * @return A readable object containing the contents of the file.
   * @throws IllegalArgumentException when the file doesn't exist in the directory.
   */
  private static Readable readFile(String name) throws IllegalArgumentException {
    try {
      return new FileReader(new File(name));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("File with name: " + name + " does not exist in " +
              "this directory.");
    }
  }
}