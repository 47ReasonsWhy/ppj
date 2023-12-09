import java.io.*;

public class Test {
    public static void main(String[] args) {
        File testDir = new File("examples");
        //File testDir = new File("studosi_fer_PPJ_master_labosi_lab-3_testovi_2011-12_1-t");
        File[] testFolders = testDir.listFiles(File::isDirectory);
        if (testFolders == null) {
            System.out.println("Test directories not found");
            return;
        }

        // loop through all test folders
        for (File testFolder : testFolders) {

            // read input and expected output files
            String input = readFile(testFolder.getPath() + "/test.in");
            String expectedOutput = readFile(testFolder.getPath() + "/test.out");

            // run your code with the input and get the actual output
            String actualOutput = runYourCode(input);

            // compare expected and actual output
            if (expectedOutput.equals(actualOutput)) {
                System.out.println(testFolder.getName() + " passed\n");
            } else {
                System.out.println(testFolder.getName() + " failed");
                System.out.println("Expected output:\n" + expectedOutput);
                System.out.println("Actual output:\n" + actualOutput + "\n");
            }
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
            System.err.println("Error reading file " + filePath);
            System.err.println(e.getMessage());
            System.exit(1);
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

        SemantickiAnalizator.main(new String[]{});

        System.setIn(stdin);
        System.setOut(stdout);

        return outputStream.toString().replace("\r\n", "\n");
    }
}
