import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class GreedyGnomesClient {
    public static GreedyGnomesClient app = new GreedyGnomesClient();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);  
        try {
            if (args.length != 1) {
                Utility.printError("ARGUMENTS_INVALID", null);
            } else {
                do {
                    String[][] map = Utility.parseMap(args[0]);
                    int choice = printMenuAndEnterInput(scanner);
                    app.runProgram(choice, map);
                } while (askToContinue(scanner));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        scanner.close();
        quit();
    }

    public static void quit() {
        System.out.println("Application shutdown.");
        System.exit(0);
    }

    public void runProgram(int choice, String[][] map) throws Exception {
        switch (choice) {
            default -> {
                System.out.println("Invalid option. Please run again");
            }
            case 1 -> {
                long startTime = System.nanoTime();
                ExhaustiveSearch exhaustiveSearch = new ExhaustiveSearch();
                exhaustiveSearch.run(map);
                long stopTime = System.nanoTime();
                long time = stopTime - startTime;
                System.out.println("Execution time: " + time + "(nanosecond)");

            }
            case 2 -> {
                long startTime2 = System.nanoTime();
                DynamicProgramming dynamicProgramming = new DynamicProgramming();
                dynamicProgramming.run(map);
                long stopTime2 = System.nanoTime();
                long time2 = stopTime2 - startTime2;
                System.out.println("Execution time: " + time2 + "(nanosecond)");
            }
        }
    }

    public static int printMenuAndEnterInput(Scanner scanner) {
        System.out.println("\nPlease select method you want to run:");
        System.out.println("1. Exhaustive Search.");
        System.out.println("2. Dynamic Programming.");
        return getChoice(scanner);
    }

    public static int getChoice(Scanner scanner) {
        int userChoice;
        do {
            System.out.print("Please enter a valid option (1 or 2): ");
            while (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid option (1 or 2) :");
                scanner.next();
            }
            userChoice = scanner.nextInt();
        } while (userChoice != 1 && userChoice != 2);
        return userChoice;
    }

    public static boolean askToContinue(Scanner scanner) {
        System.out.print("\nPress Y/y to rerun to program (You can update your input content file if needed): ");
        String next = scanner.next();
        return (next.equals("Y") || next.equals("y"));
    }

}