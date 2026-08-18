import java.io.*;
import java.util.*;

public class JobAss {

    static int[][] costMatrix;
    static int numWorkers;
    static int minTotHours;
    static List<int[]> bestAssignments = new ArrayList<>(); 
    static int permutationCount = 0;
    static char[] jobLabels;

    public static void main(String[] args) {
        System.out.println("CSC645 GROUP PROJECT: JOB MAN ALLOCATION PROBLEM");
        
        System.out.println("\n+============================================================================+" +
                           "\nPREPARED BY  : AKHMAL HUSSIN | IMRAN AUFA | DANISH HAZIM | AIMAN FUZI" +
                           "\nSTUDENT ID   : 2025124819 | 2025160393 | 2025165825 | 2025118501" +
                           "\nGROUP        : A4CDCS230 5B" +
                           "\nPREPARED FOR : DR. NURSYAHIDAH BINTI ALIAS" +
                           "\n+============================================================================+\n");

        // Step 1: Read input data from file (auto-detects dimension)
        readInput("input.txt");
        
        System.out.println("===== INPUT COST MATRIX =====");
        for (int i = 0; i < numWorkers; i++) {
            for (int j = 0; j < numWorkers; j++) {
                System.out.printf("%-6d", costMatrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();

        // Step 2: Initialize variables
        minTotHours = Integer.MAX_VALUE;

        // Build job labels dynamically A, B, C, ... Z, AA, AB, ...
        jobLabels = new char[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            jobLabels[i] = (char) ('A' + i);
        }

        // Generate base jobs array [0, 1, 2, ..., n-1]
        int[] jobs = new int[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            jobs[i] = i;
        }

        // Step 3: Print header for all permutations table
        System.out.println("\n===== ALL PERMUTATIONS =====");
        System.out.println();
        System.out.printf("%-6s ", "No.");
        for (int i = 0; i < numWorkers; i++) {
            System.out.printf("%-8s", "Man " + (i + 1));
        }
        System.out.printf("%-12s%n", "Total Hours");
        System.out.println("-".repeat(6 + (numWorkers * 8) + 12));

        // Step 4: Generate all permutations, display each and track the best
        generatePermutations(jobs, 0);

        System.out.println("-".repeat(6 + (numWorkers * 8) + 12));
        System.out.println("Total permutations checked: " + permutationCount);

        // Step 5: Display the final concluded best result(s)
        System.out.println();
        displayResults();
    }

    // Reads the size cost matrix from input.txt 
    static void readInput(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String firstLine = br.readLine().trim();
            String[] tokens = firstLine.split("\\s+");
            numWorkers = tokens.length;
            br.close();

            costMatrix = new int[numWorkers][numWorkers];
            Scanner sc = new Scanner(new File(filename));
            for (int i = 0; i < numWorkers; i++) {
                for (int j = 0; j < numWorkers; j++) {
                    costMatrix[i][j] = sc.nextInt();
                }
            }
            sc.close();
            System.out.println("Input read successfully. Detected matrix size: " + numWorkers + "x" + numWorkers + "\n");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: input.txt not found. Please place input.txt in the same folder as this program.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("ERROR: Failed to read input.txt. " + e.getMessage());
            System.exit(1);
        }
    }

    // Recursive permutation generator — tries every possible job assignment
    static void generatePermutations(int[] jobs, int start) {
        if (start == numWorkers) {
            permutationCount++;
            int currHours = calcTotHours(jobs);

            System.out.printf("%-6d ", permutationCount);
            for (int i = 0; i < numWorkers; i++) {
                System.out.printf("%-8s", jobLabels[jobs[i]]);
            }
            System.out.printf("%-12d%n", currHours);

            if (currHours < minTotHours) {
                minTotHours = currHours;
                bestAssignments.clear(); 
                bestAssignments.add(Arrays.copyOf(jobs, numWorkers)); 
            } else if (currHours == minTotHours) {
                bestAssignments.add(Arrays.copyOf(jobs, numWorkers)); 
            }
            return;
        }

        for (int i = start; i < numWorkers; i++) {
            swap(jobs, start, i);                      
            generatePermutations(jobs, start + 1);     
            swap(jobs, start, i);                      
        }
    }

    // Calculates total hours for a given job assignment
    static int calcTotHours(int[] jobs) {
        int total = 0;
        for (int worker = 0; worker < numWorkers; worker++) {
            total += costMatrix[worker][jobs[worker]];
        }
        return total;
    }

    // Swaps two elements in an array
    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Displays all concluded optimal assignments and minimum total hours
    static void displayResults() {
        System.out.println("\n===== CONCLUDED OPTIMAL ASSIGNMENT (Brute Force) =====");
        System.out.println("Minimum Total Hours: " + minTotHours);
        System.out.println("Total optimal combinations found: " + bestAssignments.size());
        
        int choiceNum = 1;
        for (int i = 0; i < bestAssignments.size(); i++) {
            int[] assignment = bestAssignments.get(i);
            System.out.println("\n------------------------------------");
            System.out.println("OPTIMAL COMBINATION #" + choiceNum);
            System.out.println("------------------------------------");
            System.out.printf("%-10s %-10s %-10s%n", "Worker", "Job", "Hours");
            System.out.println("------------------------------------");

            for (int worker = 0; worker < numWorkers; worker++) {
                int job = assignment[worker];
                System.out.printf("%-10s %-10s %-10d%n",
                    "Man " + (worker + 1),
                    String.valueOf(jobLabels[job]),
                    costMatrix[worker][job]);
            }
            choiceNum++;
        }
        System.out.println("------------------------------------");
    }
}