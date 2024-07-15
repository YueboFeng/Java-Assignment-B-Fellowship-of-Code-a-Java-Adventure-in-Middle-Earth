package utils;

import java.util.Scanner;

/**
 * This class receives user's input.
 *
 * @author Yuebo Feng
 * @version ver 2.0.0
 */
public class Input {
    public static final Scanner SCANNER = new Scanner(System.in);

    public static int readInt() {
        try {
            return Integer.parseInt(SCANNER.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    public static String readString() {
        return SCANNER.nextLine();
    }

    public static void readEnter(String msg) {
        if (msg != null) System.out.println(msg);
        SCANNER.nextLine();
    }

    public static boolean readYesNo(String msg) {
        while (true) {
            System.out.print(msg);
            String input = readString().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            }
            System.out.println("Invalid input. Please enter 'y' or 'n'.");
        }
    }

    public static void readEnter() {
        readEnter("Press ENTER to continue...");
    }
}
