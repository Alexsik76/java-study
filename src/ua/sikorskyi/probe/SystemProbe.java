package ua.sikorskyi.probe;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.Locale;

public class SystemProbe {

    private static final long BYTES_IN_MEGABYTE = 1024L * 1024;
    private static final int LABEL_WIDTH = 35;
    private static final String ROW_FORMAT = "%-" + LABEL_WIDTH + "s %s%n";

    private final String vendorName;
    private final String javaVersion;
    private final String osName;
    private final String osArch;
    private final int processorCount;
    private final long startupMemory;
    private final long maxMemory;
    private final long usedMemory;
    private final String allArgs;

    public SystemProbe(String[] args) {
        this.vendorName = System.getProperty("java.vendor");
        this.javaVersion = System.getProperty("java.version");
        this.osName = System.getProperty("os.name");
        this.osArch = System.getProperty("os.arch");

        Runtime runtime = Runtime.getRuntime();
        this.processorCount = runtime.availableProcessors();

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryBean.getHeapMemoryUsage();

        this.startupMemory = memoryUsage.getInit();
        this.maxMemory = memoryUsage.getMax();
        this.usedMemory = memoryUsage.getUsed();

        this.allArgs = args.length > 0 ? String.join("; ", args) : "No args";
    }

    private String formatMemory(long bytes) {
        if (bytes < 0) {
            return "undefined";
        }
        return String.format(Locale.US, "%.2f", (double) bytes / BYTES_IN_MEGABYTE);
    }

    public void printSystemInfo() {
        System.out.printf(ROW_FORMAT, "java.vendor:", vendorName);
        System.out.printf(ROW_FORMAT, "java.version:", javaVersion);
        System.out.printf(ROW_FORMAT, "os.name:", osName);
        System.out.printf(ROW_FORMAT, "os.arch:", osArch);
        System.out.printf(ROW_FORMAT, "Available processors (cores):", processorCount);
        System.out.printf(ROW_FORMAT, "Initial memory (MB):", formatMemory(startupMemory));
        System.out.printf(ROW_FORMAT, "Maximum memory (MB):", formatMemory(maxMemory));
        System.out.printf(ROW_FORMAT, "Used memory (MB):", formatMemory(usedMemory));
        System.out.printf(ROW_FORMAT, "Arguments:", allArgs);
    }

    public static void main(String[] args) {
        SystemProbe probe = new SystemProbe(args);
        probe.printSystemInfo();
    }
}