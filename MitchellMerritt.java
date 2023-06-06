import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class MitchellMerritt {
    private static int adjMatrix[][];
    private static Map<Integer, Process> allProcess = new HashMap<Integer, Process>();
    private static int numProcesses;
    private static List<Integer> existingValues = new ArrayList<>();

    // Add edge from i to j
    public static void addEdge(int i, int j) {
        adjMatrix[i][j] = 1;
    }

    // Remove edge from i to j
    public static void removeEdege(int i, int j) {
        adjMatrix[i][j] = 0;
    }

    public static void printMatrix() {
        System.out.println("System state: ");
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numProcesses; j++) {
                System.out.print(adjMatrix[i][j]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    //
    /**
     * 
     * @param processId
     * @return Returns the processes that are blocking the process with the
     *         processId
     */
    public static List<Integer> getProcessDependencies(int processId) {
        List<Integer> dependencies = new ArrayList<Integer>();
        for (int i = 0; i < numProcesses; i++) {
            if (adjMatrix[processId][i] == 1)
                dependencies.add(i);
        }
        return dependencies;
    }

    /**
     * 
     * @param processId
     * @return Returns processes that are blocked by the process with the processId
     */
    public static List<Integer> getBlockedByProcess(int processId) {
        List<Integer> dependencies = new ArrayList<Integer>();
        for (int i = 0; i < numProcesses; i++) {
            if (adjMatrix[i][processId] == 1)
                dependencies.add(i);
        }
        return dependencies;
    }

    /**
     * 
     * @param i process been blocked
     * @param j blocker process
     */
    public static void blockStep(int i, int j) {
        Process initiatorProcess = allProcess[i];
        Process blockerProcess = allProcess[j];
        int newLabel = Math.max(initiatorProcess.getPublicId(), blockerProcess.getPublicId()) + 1;
        initiatorProcess.setPrivateId(newLabel);
        initiatorProcess.setPublicId(newLabel);
    }

    public static void tramsmitStep() {
        for (int i = 0; i < numProcesses; i++) {
            Process process = allProcess[i];
            int processId = i;
            List<Integer> blockerProcesses = getProcessDependencies(processId);
            blockerProcesses.forEach(blockerId -> {
                Process blockerProcess = allProcess[blockerId];
                if (blockerProcess.getPublicId() > process.getPublicId()) {
                    process.setPublicId(processId);
                }
            });
        }
    }

    /**
     * If there is a cycle detected, returns true, if not, it return false
     * 
     * @return cycle detected
     */
    public static boolean detectStep() {
        int result = -1;
        for (int i = 0; i < numProcesses; i++) {
            Process process = allProcess[i];
            int processId = process.getPrivateId();
            List<Integer> blockerProcesses = getProcessDependencies(processId);
            for (int blockerId : blockerProcesses) {
                Process blockerProcess = allProcess[blockerId];
                if (blockerProcess.getPublicId() == process.getPublicId()) {
                    result = processId;
                    System.out.println("Block detected by process " + result);
                    System.out.println("Process " + process.getPrivateId() + " public id= " + process.getPublicId());
                    System.out.println("Blocker process " + blockerProcess.getPrivateId() + " public id= "
                            + blockerProcess.getPublicId());
                    System.out.print("\n");
                }
            }
        }
        return (result != -1) ? true : false;
    }

    // Creation of processes with random id's in the range [1 - 50]
    public static void createProcess(int processId) {
        Random random = new Random();
        int newProcessLabels;

        do {
            newProcessLabels = random.nextInt(50) + 1;
        } while (existingValues.contains(newProcessLabels));
        // Add existing values to the list
        existingValues.add(newProcessLabels);
        allProcess[processId] = new Process(newProcessLabels);
    }

    public static void main(String args[]) throws IOException {
        // Check for correct arguments
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

        // Now that we know the dimensio of our system we give a size to our varaibles
        adjMatrix = new int[numProcesses][numProcesses];
        allProcess = new Process[numProcesses];

        // Ingest our initial adj. matrix state
        int adjMatrixIndex = 0;
        String edges[];
        while (reader.hasNextLine()) {
            tramsmitStep();
            line = reader.nextLine();
            // check for end of section
            if (line.contains("--"))
                break;

            edges = line.split(" ");
            for (int i = 0; i < edges.length; i++) {
                adjMatrix[adjMatrixIndex][i] = Integer.parseInt(edges[i]);

                // Create processes if they were not already initialized
                if (Objects.isNull(allProcess[adjMatrixIndex]))
                    createProcess(adjMatrixIndex);
                if (Objects.isNull(allProcess[i]))
                    createProcess(i);

                if (adjMatrix[adjMatrixIndex][i] == 1)
                    blockStep(adjMatrixIndex, i);
            }
            adjMatrixIndex++;
        }

        // Print initial state and execute transmition and detect steps
        printMatrix();
        tramsmitStep();
        detectStep();

        // Ingest new edges,
        // print matrix state
        // and print detectec cycles in case there are any
        while (reader.hasNextLine()) {
            line = reader.nextLine();
            String processesIds[] = line.split("-");
            int processId = Integer.parseInt(processesIds[0].trim());
            int blockerId = Integer.parseInt(processesIds[1].trim());
            addEdge(processId, blockerId);
            blockStep(processId, blockerId);
            printMatrix();
            if (detectStep())
                break;
        }
        reader.close();
        System.out.println("Execution completed");
    }
}