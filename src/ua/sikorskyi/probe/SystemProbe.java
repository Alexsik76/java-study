package ua.sikorskyi.probe;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class SystemProbe {

    String vendorName;
    String vendorVersion;
    String osName;
    String osArch;
    int coreNumber;
    long startupMemory;
    long maxMemory;
    long usedMemory;
    String allArgs = "Не передано";

    public SystemProbe(String[] args) {
        this.vendorName = System.getProperty("java.vendor");
        this.vendorVersion = System.getProperty("java.version");
        this.osName = System.getProperty("os.name");
        this.osArch = System.getProperty("os.arch");

        Runtime runtime = Runtime.getRuntime();
        this.coreNumber = runtime.availableProcessors();

        MemoryMXBean manager = ManagementFactory.getMemoryMXBean();
        this.startupMemory = manager.getHeapMemoryUsage().getInit() / (1024 * 1024);
        this.maxMemory = manager.getHeapMemoryUsage().getMax() / (1024 * 1024);
        this.usedMemory = manager.getHeapMemoryUsage().getUsed() / (1024 * 1024);

        if (args.length > 0) {
            this.allArgs = String.join("; ", args);
        }
    }

    public void printSystemInfo() {
        System.out.printf("%-35s %s%n", "java.vendor:", vendorName);
        System.out.printf("%-35s %s%n", "java.version:", vendorVersion);
        System.out.printf("%-35s %s%n", "os.name:", osName);
        System.out.printf("%-35s %s%n", "os.arch:", osArch);
        System.out.printf("%-35s %d%n", "Available processors (cores):", coreNumber);
        System.out.printf("%-35s %d%n", "Initial memory (MB):", startupMemory);
        System.out.printf("%-35s %d%n", "Maximum memory (MB):", maxMemory);
        System.out.printf("%-35s %d%n", "Used memory (MB):", usedMemory);
        System.out.printf("%-35s %s%n", "Arguments:", allArgs);
    }

    public static void main(String[] args) {
        SystemProbe probe = new SystemProbe(args);
        probe.printSystemInfo();
    }
}