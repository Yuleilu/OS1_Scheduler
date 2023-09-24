import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Process {
    int processNumber;
    double submitTime;
    double runTime;
    double startingTime;
    double finalTime;
    double waitTime;
    double turnaroundTime;

    public Process(int processNumber, double submitTime, double runTime) {
        this.processNumber = processNumber;
        this.submitTime = submitTime;
        this.runTime = runTime;
    }
}

public class Scheduler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();

        System.out.print("Please input the total number of processes: ");
        int totalProcesses = scanner.nextInt();

        for (int i = 0; i < totalProcesses; i++) {
            System.out.print("Please input the process number, submit time, and run time (e.g., 1 9.0 0.2): ");
            int processNumber = scanner.nextInt();
            double submitTime = scanner.nextDouble();
            double runTime = scanner.nextDouble();
            processes.add(new Process(processNumber, submitTime, runTime));
        }

        while (true) {
            System.out.print("What kind of algorithm do you want? Please input 1 for FCFS, 2 for SJF, or 0 to exit: ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                break;
            }

            switch (choice) {
                case 1:
                    runFCFS(processes);
                    break;
                case 2:
                    runSJF(processes);
                    break;
                default:
                    System.out.println("You have input a wrong number, please input again.");
            }
        }
    }

    private static void runFCFS(ArrayList<Process> processes) {
        Collections.sort(processes, Comparator.comparingDouble(p -> p.submitTime));
        double currentTime = 0;
        double totalTurnaroundTime = 0;

        System.out.println(" submit     run     starting   final    wait  turnaround");

        for (Process process : processes) {
            if (process.submitTime > currentTime) {
                currentTime = process.submitTime;
            }

            process.startingTime = currentTime;
            process.finalTime = process.startingTime + process.runTime;
            process.waitTime = process.startingTime - process.submitTime;
            process.turnaroundTime = process.finalTime - process.submitTime;

            System.out.printf("%d    %.1f       %.1f      %.1f       %.1f       %.1f     %.1f%n",
                    process.processNumber, process.submitTime, process.runTime, process.startingTime,
                    process.finalTime, process.waitTime, process.turnaroundTime);

            currentTime = process.finalTime;
            totalTurnaroundTime += process.turnaroundTime;
        }
        
        double averageTurnaroundTime = totalTurnaroundTime / processes.size();
        String formattedAverageTurnaroundTime = String.format("%.3f", averageTurnaroundTime);
        System.out.println("The average turnaround time is " + formattedAverageTurnaroundTime);

        
    }

    private static void runSJF(ArrayList<Process> processes) {
        Collections.sort(processes, Comparator.comparingDouble(p -> p.submitTime));
        double currentTime = 0;
        double totalTurnaroundTime = 0;
        int num=processes.size();
        System.out.println(" submit     run     starting   final    wait  turnaround");

        while (!processes.isEmpty()) {
            
            Process shortestJob = null;
            for (Process process : processes) {
                if (process.submitTime <= currentTime) {
                    if (shortestJob == null || process.runTime < shortestJob.runTime) {
                        shortestJob = process;
                    }
                } else {
                    break;
                }
            }

            if (shortestJob != null) {
                processes.remove(shortestJob);
                shortestJob.startingTime = currentTime;
                shortestJob.finalTime = currentTime + shortestJob.runTime;
                shortestJob.waitTime = shortestJob.startingTime - shortestJob.submitTime;
                shortestJob.turnaroundTime = shortestJob.finalTime - shortestJob.submitTime;

                System.out.printf("%d    %.1f       %.1f      %.1f       %.1f       %.1f     %.1f%n",
                        shortestJob.processNumber, shortestJob.submitTime, shortestJob.runTime,
                        shortestJob.startingTime, shortestJob.finalTime, shortestJob.waitTime, shortestJob.turnaroundTime);

                currentTime = shortestJob.finalTime;
                totalTurnaroundTime += shortestJob.turnaroundTime;

            } else {
                currentTime = processes.get(0).submitTime;
            }
        
        }
        
        double averageTurnaroundTime = totalTurnaroundTime / num;
        String formattedAverageTurnaroundTime = String.format("%.3f", averageTurnaroundTime);
        System.out.println("The average turnaround time is " + formattedAverageTurnaroundTime);
    }
}