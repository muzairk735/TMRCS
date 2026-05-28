package com.telemedicine.ui;

import java.util.Scanner;

/**
 * Stateless console utility methods shared across all menu handler classes.
 *
 * <p>Every method is {@code static} — there is no mutable state and no reason
 * to instantiate this class. Handler classes call these directly, e.g.
 * {@code UIHelper.clearScreen()} or {@code UIHelper.getMenuInput(...)}.</p>
 *
 * <p>Responsibilities covered here:</p>
 * <ul>
 *   <li>Validated integer input ({@link #getMenuInput}, {@link #parseIntInput})</li>
 *   <li>String formatting ({@link #truncate})</li>
 *   <li>Console control ({@link #clearScreen}, {@link #pauseScreen})</li>
 * </ul>
 *
 * <p>Intentionally contains no domain logic — it knows nothing about patients,
 * doctors, or appointments.</p>
 */
public class UIHelper {

    // -------------------------------------------------------------------------
    // Input helpers
    // -------------------------------------------------------------------------

    /**
     * Prints {@code prompt} then reads a line from {@code scanner}, repeating
     * until the user types a valid integer. Never returns without a usable value.
     *
     * @param prompt  text to display before the cursor (pass {@code ""} to skip)
     * @param scanner the shared application scanner
     * @return the parsed integer entered by the user
     */
    public static int getMenuInput(String prompt, Scanner scanner) {
        while (true) {
            if (!prompt.isEmpty()) System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("✗ Please enter a valid number.");
            }
        }
    }

    /**
     * Reads one line from {@code scanner} and attempts to parse it as an integer.
     * Unlike {@link #getMenuInput}, this does <em>not</em> re-prompt on failure —
     * it returns {@code null} so the caller can decide how to handle a bad value.
     *
     * @param scanner the shared application scanner
     * @return the parsed integer, or {@code null} if the input was not numeric
     */
    public static Integer parseIntInput(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // String formatting
    // -------------------------------------------------------------------------

    /**
     * Truncates {@code s} to at most {@code max} characters, appending an
     * ellipsis ({@code …}) when the string is shortened. Used to keep
     * dashboard welcome lines inside the fixed-width box borders.
     *
     * @param s   the string to shorten (may be {@code null})
     * @param max maximum number of characters in the result
     * @return the original string if it fits, otherwise a truncated version
     */
    public static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    // -------------------------------------------------------------------------
    // Console control
    // -------------------------------------------------------------------------

    /**
     * Attempts to clear the visible console using the OS-appropriate command.
     * Falls back to printing 50 blank lines if the system call fails (e.g.
     * when running inside an IDE console that does not support ANSI codes).
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // ANSI escape: move cursor to top-left, then erase entire screen
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    /**
     * Prints a "Press Enter to continue..." prompt and blocks until the user
     * hits Enter. Gives the user time to read output before the screen is cleared.
     *
     * @param scanner the shared application scanner
     */
    public static void pauseScreen(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
