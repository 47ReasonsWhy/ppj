import java.io.*;

public class TestExample {
    public static void main(String[] args) {
        // change this to the path of your test directory
        File testDir = new File("../langdefs");

        File folder;
        try {
            folder = new File(testDir.getPath() + "/47pr1");
        } catch (NullPointerException e) {
            System.err.println("Test directory not found");
            return;
        }
        if (!folder.isDirectory()) {
            System.err.println("Test directory not found");
            return;
        }

        // read input and expected output files
        String input = readFile(folder.getPath() + "/test.in");
        String expectedOutput = readFile(folder.getPath() + "/test.out");

        // run your code with the input and get the actual output
        String actualOutput = runYourCode(input);

        // compare expected and actual output
        if (expectedOutput.equals(actualOutput)) {
            System.out.println("Test passed");
        } else {
            System.out.println("Test failed");
            System.out.println("Expected output:\n" + expectedOutput);
            System.out.println("Actual output:\n" + actualOutput);
        }
    }

    private static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return content.toString();
    }

    private static String runYourCode(String input) {
        InputStream stdin = System.in;
        PrintStream stdout = System.out;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        System.setIn(inputStream);
        System.setOut(new PrintStream(outputStream));

        //SA.main(new String[]{});

        System.setIn(stdin);
        System.setOut(stdout);

        return outputStream.toString().replace("\r\n", "\n");
    }
}