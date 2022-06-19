import java.util.ArrayList;
import java.util.List;

class ESNode {
    /** This class represents each cell in the map with row, column (or the coordinates of a cell) and its value, in integer data types.
     *
     */
    protected int row;
    protected int col;
    protected int value;
    public ESNode(int row, int col){
        this.row = row;
        this.col = col;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

class Path {
    /**
     * This class represents a path, which is a list containing all ESNodes, which will be compared later and display the best path.
     */
    protected List<ESNode> path;
    protected int goldValue = 0;
    protected int step = 0;
    protected String direction = "";
    public Path(){
        this.path = new ArrayList<>();
        this.goldValue = 0;
        this.step = 0;
    }
    public void add(ESNode ESNode){
        path.add(ESNode);
    }

    public List<ESNode> getPath() {
        return path;
    }

    public void setPath(List<ESNode> path) {
        this.path = path;
    }
    public int stepCount(){
        this.step = getPath().size()-1;
        setStep(step);
        return step;
    }

    public void setStep(int step) {

        this.step = step;
    }
    public void printPath(){
        System.out.println("Gold Value: " + goldValue);
    }

}


public class ExhaustiveSearch {

    // set the maximum size for the 2D array.
    static final int MAXSIZE = 27;
    // Store a temporary path using in Depth-First-Search function.
    private static Path onePath = new Path();
    // This is the optimal path.
    private static Path bestPath = new Path();
    // List to store all the paths in types of Path.
    private static String path = "";

    /**
     * Function returns true if the move taken is valid else it will return false.
     * 
     * @param row     the int represents row where the cell located.
     * @param col     the int represents column where the cell located.
     * @param map     the 2D String[][] map
     * @param rows    the number of valid rows, if row exceed this number then it is
     *                invalid
     * @param cols    the number of valid columns, if column exceed this number then
     *                it is invalid
     * @param visited the value of cell in visited boolean 2D array.
     * @return boolean value. True if the cell is valid and safe to move. False if
     *         the cell is not safe to visit.
     */
    private static boolean isSafe(int row, int col, String[][] map, int rows, int cols,
            boolean[][] visited) {
        return row != -1 && row != rows && col != -1 &&
                col != cols && !visited[row][col] &&
                !map[row][col].equals("X");
    }

    /**
     * Function to find and store all the possible path from the position (0,0) to
     * (m,n) using Depth First Search Algorithms, this is one of the
     * optimal solution for the exhaustive search to find maximum in all possible
     * paths.
     * 
     * @param row     the int represents row where the cell located.
     * @param col     the int represents column where the cell located.
     * @param map     the 2D String[][] map
     * @param rows    the number of valid rows, if row exceed this number then it is
     *                invalid
     * @param cols    the number of valid columns, if column exceed this number then
     *                it is invalid
     * @param visited the value of cell in visited boolean 2D array.
     */
    private static void depthFirstSearch(int row, int col, String[][] map,
            int rows, int cols, boolean[][] visited, int[] count) {
        count[0]++;
        ESNode curESNode = new ESNode(row, col);

        // This will check the initial point (i.e. (0, 0)) to start the paths.
        if (row == -1 || row == rows || col == -1 ||
                col == cols || visited[row][col] ||
                map[row][col].equals("X")) {

            return;
        }

        // If reach the last cell (n-1, n-1) or block both right and down then store the
        // path and return
        if (row == rows - 1 && col == cols - 1
                || !isSafe(row + 1, col, map, rows, cols, visited) && !isSafe(row, col + 1, map, rows, cols, visited)) {
            if (Utility.parseValue(map[row][col]) != 0) {
                curESNode.setValue(Utility.parseValue(map[row][col]));
                onePath.goldValue += curESNode.value;
            }
            onePath.add(curESNode);
            Path newPath = removeRedundant(onePath);
            newPath.direction = path.substring(0, newPath.stepCount());
            if (isOptimalPath(newPath, bestPath)) {
                bestPath = newPath;
                bestPath.direction = newPath.direction;
            }
            onePath = copyArrayList(onePath);
            return;
        }
        // Mark the cell as visited
        visited[row][col] = true;

        // Try for all the 2 directions (down,right) in the given order to get the
        // paths in lexicographical order
        // Check if downward move is valid
        if (isSafe(row + 1, col, map, rows, cols, visited)) {
            path += 'D';
            if (!onePath.getPath().contains(curESNode)) {
                onePath.add(curESNode);
                if (Utility.parseValue(map[row][col]) != 0) {
                    curESNode.setValue(Utility.parseValue(map[row][col]));
                    onePath.goldValue += curESNode.value;
                }
            }
            depthFirstSearch(row + 1, col, map, rows, cols,
                    visited, count);
            path = path.substring(0, path.length() - 1);
            ESNode ESNode = onePath.getPath().get(onePath.getPath().size() - 1);
            onePath.path.remove(ESNode);
            onePath.goldValue -= ESNode.value;
        }
        // Check if the right move is valid
        if (isSafe(row, col + 1, map, rows, cols, visited)) {
            path += 'R';
            if (!onePath.getPath().contains(curESNode)) {
                onePath.add(curESNode);
                if (Utility.parseValue(map[row][col]) != 0) {
                    curESNode.setValue(Utility.parseValue(map[row][col]));
                    onePath.goldValue += curESNode.value;
                }
            }
            depthFirstSearch(row, col + 1, map, rows, cols,
                    visited, count);
            path = path.substring(0, path.length() - 1);
            ESNode ESNode = onePath.getPath().get(onePath.getPath().size() - 1);
            onePath.path.remove(ESNode);
            onePath.goldValue -= ESNode.value;

        }
        // Mark the cell as unvisited for
        // other possible paths
        visited[row][col] = false;
    }

    /**
     * Function to copy a path to the new path without duplication. This function
     * aims to copy the previous path to the whole new path
     * and then add this copied path to the list of path and make the previous path
     * ( after 1 path is completed) empty.
     * 
     * @param originalPath this is the original completed path will be used for the
     *                     cop
     * @return the replicated path.
     */
    private static Path copyArrayList(Path originalPath) {
        Path path = new Path();
        for (ESNode ESNode : originalPath.getPath()) {
            if (!path.path.contains(ESNode)) {
                path.add(ESNode);
            }
        }
        path.goldValue = originalPath.goldValue;

        return path;
    }

    /**
     * Method to compare the path to consider whether the current path running in
     * Depth First Search the most optimal path.
     * 
     * @param newPath     the path after running the path
     * @param optimalPath the path that is currently the best path.
     * @return true if new path is more optimal, false when it is not.
     */
    private static boolean isOptimalPath(Path newPath, Path optimalPath) {
        boolean flag = newPath.goldValue > optimalPath.goldValue;
        if (newPath.goldValue == optimalPath.goldValue && newPath.stepCount() < optimalPath.stepCount()) {
            flag = true;
        }
        return flag;
    }

    /**
     * Function to remove the redundant element in the path, which means that the
     * path will cut
     * at the last cell with gold in the path, which means this function remove all
     * "." after the last gold value,
     * 
     * @param original original path to be cut
     * @return the new path with the redundant element are removed.
     */
    private static Path removeRedundant(Path original) {
        int lastGoldIndex = 0;
        Path newPath = new Path();
        for (int i = original.getPath().size() - 1; i >= 0; i--) {
            if (original.getPath().get(i).value != 0) {
                lastGoldIndex = i;
                break;
            }
        }
        newPath.setPath(original.getPath().subList(0, lastGoldIndex + 1));
        newPath.goldValue = original.goldValue;
        return newPath;

    }

    /**
     * Method to find and store all the valid paths calling the depth first search
     * method
     * 
     * @param map the map in 2D String[][].
     */
    private static void findAllPossiblePath(String[][] map) {
        int[] count = { 0 };
        boolean[][] visited = new boolean[MAXSIZE][MAXSIZE];
        int rows = map.length;
        int cols = map[0].length;
        // Call the utility function to
        // find the valid paths
        depthFirstSearch(0, 0, map, rows, cols, visited, count);
        printMap(map, count);
    }

    /**
     * Method to print the solution map.
     * 
     * @param map of String 2D array
     */
    public static void printMap(String[][] map, int[] count) {
        System.out.println("\n*** Processed map (Exhaustive Search) ***\n");
        // Print the map with the best path
        int order = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                ESNode ESNode = bestPath.getPath().get(order);
                if (ESNode.row == i && ESNode.col == j) {
                    if (ESNode.value == 0) {
                        map[i][j] = "+";
                    } else if (ESNode.value > 0) {
                        map[i][j] = "G";
                    }
                    if (order < bestPath.getPath().size() - 1) {
                        ++order;
                    }
                }
                System.out.print(map[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("\n*** Result (Exhaustive Search) ***\n");
        System.out.println("Route: " + bestPath.direction);
        System.out.println("Recursive counts: " + count[0]);
        System.out.println("Gold collected: " + bestPath.goldValue);
        System.out.println("Step counts: " + bestPath.stepCount());
    }

    public void run(String[][] map) throws InterruptedException {
        System.out.println("\nProcessing...");
        findAllPossiblePath(map);
    }

}