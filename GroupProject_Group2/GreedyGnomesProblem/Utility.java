import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

  public static String[][] parseMap(String fileName) throws IOException {
    BufferedReader br = Utility.getReader(fileName);
    try {
      String[] mapSize = getMapSize(br.readLine());
      String[][] map = new String[Integer.parseInt(mapSize[0])][Integer.parseInt(mapSize[1])];
      getMapValue(br, map);
      System.out.println("\n*** Original map ***\n");
      displayMap(map);
      return map;
    } finally {
      br.close();
    }
  }

  public static BufferedReader getReader(String filename) {
    try {
      return new BufferedReader(new FileReader(filename));
    } catch (Exception e) {
      printError("FILE_INVALID", new String[] { filename });
    }
    return null;
  }

  /**
   * Method to get the map size from the first line of the input file
   *
   * @param line first input line
   */
  public static String[] getMapSize(String line) {
    if (line == null) {
      printError("FILE_EMPTY", null);
    } else {
      String[] mapSize = line.split("\\s+");
      if (mapSize.length != 2) {
        printError("MAP_SIZE", null);
      } else {
        return mapSize;
      }
    }
    return null;
  }

  /**
   * Method to go through each line in input file to parse the input map
   *
   * @param br  BufferedReader
   * @param map the 2D String[][] map
   */
  public static void getMapValue(BufferedReader br, String[][] map) throws IOException {
    int numberOfRows = map.length;
    int numberOfColumns = map[0].length;
    String line = br.readLine();
    int row = 0;
    while (line != null) {
      // Check if row has exceeded the specify number of rows
      if (row > numberOfRows - 1) {
        printError("ROWS_SURPLUS", new String[] { String.valueOf(numberOfRows) });
      }
      String[] characters = line.toUpperCase().split(" ");
      int rowSize = characters.length;
      // Check if column does not match the specify number of columns
      if (rowSize != numberOfColumns) {
        printError("COLUMNS_MISTMATCH",
            new String[] { String.valueOf(row + 1), String.valueOf(rowSize), String.valueOf(numberOfColumns) });
      } else {
        // Parse and validate each character in the row
        for (int column = 0; column < rowSize; ++column) {
          if (validateCharacter(characters[column])) {
            map[row][column] = characters[column];
          } else {
            printError("INVALID_CHARACTER", new String[] { String.valueOf(row + 1), String.valueOf(column + 1) });
          }
        }
      }
      row++;
      line = br.readLine();
    }
    // Check if row is less than the specify number of rows
    if (row < numberOfRows) {
      printError("ROWS_SHORTAGE", new String[] { String.valueOf(numberOfRows) });
    }
  }

  public static boolean validateCharacter(String character) {
    // Character needs to be x, X, ., or a positive number
    Pattern pattern = Pattern.compile("^[xX]$|^[.]$|^[0-9]+$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(character);
    return matcher.find();
  }

  public static void printError(String err, String[] params) {
    switch (err) {
      case "ROWS_SHORTAGE":
        System.out.println("The number of rows is less than the specificed figure (" + params[0] + ").");
        break;
      case "ROWS_SURPLUS":
        System.out.println("The number of rows exceeds specificed figure (" + params[0] + "). ");
        System.out.println("Make sure there are no redundant new lines.");
        break;
      case "COLUMNS_MISTMATCH":
        System.out
            .print("Row " + params[0] + " has mismatched number of columns (" + params[1] + " vs " + params[2] + "). ");
        System.out.println("Make sure there is one space between every two characters.");
        break;
      case "MAP_SIZE":
        System.out.println("The first line must include 2 numbers indicating map's number of rows and columns.");
        break;
      case "FILE_EMPTY":
        System.out.println("File can not be empty.");
        break;
      case "FILE_INVALID":
        System.out.println("Can not open file with name: " + params[0]);
        break;
      case "ARGUMENTS_INVALID":
        System.out.println("Please input one and only one arguement (filename)");
        break;
      case "INVALID_CHARACTER":
        System.out.println("Row " + params[0] + ", column " + params[1] + " has invalid character.");
        System.out.println("Each character must be X or x or . or a positive number.");
        break;
    }
    System.exit(-1);
  }

  public static void displayMap(String[][] map) {
    int row = map.length;
    int col = map[0].length;
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        System.out.print(map[i][j] + "\t");
      }
      System.out.print("\n");
    }
  }

  /**
   * Method to parse value of String in the cell from the map to the integer value
   * to process the node and path,
   * 
   * @param value of the cell in string data type
   * @return value of cell in integer data type
   */
  public static int parseValue(String value) {
    if (".".equals(value)) {
      return 0;
    }
    return Integer.parseInt(value);
  }

}