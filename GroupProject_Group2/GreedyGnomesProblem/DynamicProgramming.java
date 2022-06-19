/**
 * This class represents each cell in the map with row, column (or the
 * coordinates of a cell) and its values, in integer data types.
 *
 */
class Node {
  public Node child;
  public Node parent;
  // accummulative value traced back from the end of each possible path until a
  // specific node
  public int accummulatedValue = 0;
  public int row;
  public int column;
  public int stepCount = 0;

  public Node(int row, int column) {
    this.row = row;
    this.column = column;
  }
}

public class DynamicProgramming {

  public void run(String[][] map) {
    System.out.println("Processing...\n");
    // the 2D Node[][] to store best route starting from each node
    Node[][] nodesList = new Node[map.length][map[0].length];
    // recursive call count
    int[] count = { 0 };
    // start node of the best route
    Node head = findBestRoute(map, 0, 0, nodesList, count);
    // display results
    displayResult(head, map, count);
  }

  /**
   * Method to find the best route given a starting position
   *
   * @param row       the int represents row where the cell located.
   * @param col       the int represents column where the cell located.
   * @param map       the 2D String[][] map
   * @param nodesList the 2D Node[][] to store best route starting from each node
   *                  in the map
   * @param count     recursive call count
   */
  public Node findBestRoute(String[][] map, int row, int col, Node[][] nodesList, int[] count) {
    // Increase recursive call
    count[0]++;

    // Create new node and store it to the map
    Node node = new Node(row, col);
    nodesList[row][col] = node;

    // Check termination condition
    if (isBlocked(map, row, col)) {
      node.accummulatedValue = Utility.parseValue(map[row][col]);
      // Only start to count step if there is gold collected
      if (node.accummulatedValue != 0) {
        node.stepCount = 1;
      }
      return node;
    }

    // Check if both direction is free to go
    if (!isVerticallyBlocked(map, row, col) && !isHorizontallyBlocked(map, row, col)) {
      // Find the best route of the right direction if it hasn't ben computed before
      Node rightNode = nodesList[row][col + 1] != null ? nodesList[row][col + 1]
          : findBestRoute(map, row, col + 1, nodesList, count);
      // Find the best route of the down direction if it hasn't ben computed before
      Node downNode = nodesList[row + 1][col] != null ? nodesList[row + 1][col]
          : findBestRoute(map, row + 1, col, nodesList, count);
      // Compare the two directions and select the better route with more golds and
      // less steps
      // Remember the first node of the selected direction as the current node's child
      if ((rightNode.accummulatedValue > downNode.accummulatedValue) ||
          ((rightNode.accummulatedValue == downNode.accummulatedValue) && rightNode.stepCount < downNode.stepCount)) {
        attachChildToParent(node, rightNode, map);
      } else {
        attachChildToParent(node, downNode, map);
      }
    }

    // Check if right movement is blocked
    if (isVerticallyBlocked(map, row, col)) {
      // Find the best route of the down direction if it hasn't ben computed before
      // Remember the first node of the down direction as the current node's child
      Node downNode = nodesList[row + 1][col] != null ? nodesList[row + 1][col]
          : findBestRoute(map, row + 1, col, nodesList, count);
      attachChildToParent(node, downNode, map);
    }

    // Check if down movement is blocked
    if (isHorizontallyBlocked(map, row, col)) {
      // Find the best route of the right direction if it hasn't ben computed before
      // Remember the first node of the right direction as the current node's child
      Node rightNode = nodesList[row][col + 1] != null ? nodesList[row][col + 1]
          : findBestRoute(map, row, col + 1, nodesList, count);
      attachChildToParent(node, rightNode, map);
    }
    return node;
  }

  /**
   * Method to attach a parent and a child node together and compute the
   * accummulated golds as well as counting steps
   *
   * @param parent the parent node
   * @param child  the child node
   * @param map    the 2D String[][] map
   */
  public void attachChildToParent(Node parent, Node child, String[][] map) {
    parent.child = child;
    parent.accummulatedValue = Utility.parseValue(map[parent.row][parent.column]) + child.accummulatedValue;
    // Counting steps if golds found
    if (child.accummulatedValue != 0) {
      parent.stepCount = child.stepCount + 1;
    }
  }

  public boolean isBlocked(String[][] map, int row, int col) {
    return (isVerticallyBlocked(map, row, col) && isHorizontallyBlocked(map, row, col));
  }

  public boolean isHorizontallyBlocked(String[][] map, int row, int col) {
    return (row == map.length - 1 || map[row + 1][col].equals("X"));
  }

  public boolean isVerticallyBlocked(String[][] map, int row, int col) {
    return (col == map[0].length - 1 || map[row][col + 1].equals("X"));
  }

  public void displayResult(Node node, String[][] map, int[] count) {
    System.out.println("\n*** Processed map (Dynamic Programming) ***\n");
    String route = traceBack(node, map);
    Utility.displayMap(map);
    System.out.println("\n*** Result (Dynamic Programming) ***\n");
    System.out.println("Route: " + route);
    System.out.println("Recursive counts: " + count[0]);
    System.out.println("Gold collected: " + node.accummulatedValue);
    System.out.println("Step counts: " + node.stepCount);
  }

  /**
   * Method to display the full path starting from the head node
   *
   * @param node the head node of the path
   * @param map  the 2D String[][] map
   */
  public String traceBack(Node node, String[][] map) {
    StringBuilder movements = new StringBuilder();
    // traverse through the linked list to transform the path characters and
    // retrieve the movements
    while (node.child != null && node.child.accummulatedValue != 0) {
      map[node.row][node.column] = map[node.row][node.column].equals(".") ? "+" : "G";
      movements.append(node.column < node.child.column ? "R, " : "D, ");
      node = node.child;
    }
    map[node.row][node.column] = map[node.row][node.column].equals(".") ? "+" : "G";
    return (movements.length() > 2 ? movements.substring(0, movements.length() - 2) : "");
  }
}
