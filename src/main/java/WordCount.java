import picocli.CommandLine;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Stream;

@Command(
        name = "Word Count",
        description = "It prints the counts of different things in the file"
)

public class WordCount implements Runnable {

    @Parameters(description = "The file to count ben gazöz çöişüğ")
    public List<String> filenames;


    @Option(names = {"-c", "--countBytes"}, description = "Count the number of Bytes in the file")
    private Boolean countBytes = false;

    @Option(names = {"-w", "countWords"}, description = "Count the number of Words in the file")
    private Boolean countWords = false;

    @Option(names = {"-l", "--countLines"}, description = "Count the number of lines in the file")
    private Boolean countLines = false;

    public static long countBytes(Path path) throws IOException {
        return Files.size(path);
    }

    public static long countWordsUsingStreams(Path path) {
        long lines = 0;
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.map(StringTokenizer::new)
                    .mapToInt(StringTokenizer::countTokens).sum();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static long countWords(Path path) throws IOException {
        long result = 0;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            for (; ; ) {
                String line = reader.readLine();
                if (line == null)
                    break;

                result = new StringTokenizer(line).countTokens();
            }
        }
        return result;
    }

    public static long countLines(Path path) {
        long lines = 0;
        try (Stream<String> stream = Files.lines(path)) {
            lines = stream.count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }

    public static void main(String[] args) {

        System.out.println("\u20BA");
        new CommandLine(new WordCount()).execute(args);
    }

    @Override
    public void run() {
        try {

            System.out.println(filenames);
            //not in a try-resource block because path is not auto-closable
            Path path = Paths.get(filenames.get(0));
            //to print all details if no command is specified
            if (!countBytes & !countLines & !countWords) {
                System.out.println(countLines(path) + " " + countBytes(path) + " " + countWords(path) + " " + filenames.get(0));

            } else {
                System.out.println((countLines ? countLines(path) : "") + (countWords ? countWords(path) + " " : "") + (countBytes ? countBytes(path) : "") + " " + filenames.get(0));
            }

        } catch (IOException e) {
            //put in real error messages
            System.out.println(e.toString());
        }
    }

}
