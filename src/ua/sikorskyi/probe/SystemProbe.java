package ua.sikorskyi.probe;

public class SystemProbe {
    public static void main(String[] args){
        String osArch = System.getProperty("os.arch");
        System.out.println("os.arch: " + osArch);
    }
}
