public class ExcelUtil {

    /**
     * Escapes untrusted input to prevent it from being interpreted as a formula in Excel.
     * @param input The untrusted input string.
     * @return The escaped string.
     */
    public static String escapeExcelInput(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Check if the first character is one of the characters that can start a formula
        char firstChar = input.charAt(0);
        if (firstChar == '=' || firstChar == '+' || firstChar == '-' || firstChar == '@') {
            return "'" + input;
        }

        return input;
    }

}