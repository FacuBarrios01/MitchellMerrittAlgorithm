import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MitchellMerritt {

    public static void main(String args[]) throws IOException{
        // Check for correct arguments
        int numProcesses;
        File inputFile = null;
        Scanner reader = null;
        String line;
        if (args.length < 1) {
            System.out.println("No dataset provided");
            System.exit(0);
        } else if (args.length > 1) {
            System.out.println("Too many arguments");
            System.exit(0);
        }
        // Parse number of processes and resources
        inputFile = new File(args[0]);
        if (inputFile != null)
            reader = new Scanner(inputFile);

        // Get number of processes
        line = reader.nextLine();
        if (!line.contains("Number of processes:")) {
            System.out.println(
                    "First line must contain \"Number of processes:\" immediately followed by a integer value ex. \"Number of processes:5\"");
            System.exit(0);
        }
        numProcesses = Integer.parseInt(line.split(":")[1].trim());

        reader.close();
    }
}