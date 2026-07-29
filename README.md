# Завдання 1.1 — JDK, JVM, компіляція та запуск

**Етап:** 1. Java Core
**Тема:** 1. Встановлення JDK, будова JVM/JRE/JDK, компіляція та запуск
**Складання:** вручну, без Maven

---
## Налаштування середовища

### JDK було встановлено раніше, тому перевірка:

```bash
alex@fedora:~/dev/java-study/task1$ java -version
openjdk version "21.0.11" 2026-04-21 LTS
OpenJDK Runtime Environment Temurin-21.0.11+10 (build 21.0.11+10-LTS)
OpenJDK 64-Bit Server VM Temurin-21.0.11+10 (build 21.0.11+10-LTS, mixed mode, sharing)

alex@fedora:~/dev/java-study/task1$ javac -version
javac 21.0.11

alex@fedora:~/dev/java-study/task1$ echo $JAVA_HOME
/home/alex/.sdkman/candidates/java/current
```

### Структура каталогів:

```bash 
alex@fedora:~/dev/java-study/task1$ tree
.
├── dist
├── out
│   └── ua
│       └── sikorskyi
│           └── probe
│               └── SystemProbe.class
├── README.md
├── src
│   └── ua
│       └── sikorskyi
│           └── probe
│               └── SystemProbe.java
└── task-1.1-jdk-jvm.md

10 directories, 4 files
```

## Програмування:

### Найпростіша перша версія програми:

```java
package ua.sikorskyi.probe;


public class SystemProbe {
    public static void main(String[] args)
        throws Exception {
            String p = System.getProperty("os.arch");
            System.out.println("os.arch: " + p);
        }
}
```

### Компіляція

```bash
javac -d out $(find src -name "*.java")
```

або більш явно

```bash
 javac -d out src/ua/sikorskyi/probe/SystemProbe.java
```

### Запуск

```bash
alex@fedora:~/dev/java-study/task1$ java -cp out ua.sikorskyi.probe.SystemProbe
os.arch: amd64
```

ключ ```-cp``` означає "-cp <class search path of directories and zip/jar files>" - де шукати класи (усі, зокрема залежності).

### Створення архіву

```jar cvfm dist/system-probe.jar manifest.txt -C out .```

ключі:
 - с - створити;
 - v - докалдний вивід;
 - f - явно вказати ім'я jar файлу;
 - m - використовувати файл маніфесту;
 - С - перейти у вказану папку і зберегти в архіві структуру

 ### Запуск з архіву

 ``` alex@fedora:~/dev/java-study/task1$ java -jar dist/system-probe.jar арг1 арг2```

 ### Дослідженя коду

 ```
 javap -c -p out/ua/sikorskyi/probe/SystemProbe.class
Compiled from "SystemProbe.java"
public class ua.sikorskyi.probe.SystemProbe {
  java.lang.String vendorName;

  java.lang.String vendorVersion;

  java.lang.String osName;

  java.lang.String osArch;

  int coreNumber;

  long startupMemory;

  long maxMemory;

  long usedMemory;

  java.lang.String allArgs;

  public ua.sikorskyi.probe.SystemProbe(java.lang.String[]);
    Code:
       0: aload_0
       1: invokespecial #20                 // Method java/lang/Object."<init>":()V
       4: aload_0
       5: ldc           #23                 // String Не передано
       7: putfield      #25                 // Field allArgs:Ljava/lang/String;
      10: aload_0
      11: ldc           #27                 // String java.vendor
      13: invokestatic  #29                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      16: putfield      #35                 // Field vendorName:Ljava/lang/String;
      19: aload_0
      20: ldc           #37                 // String java.version
      22: invokestatic  #29                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      25: putfield      #39                 // Field vendorVersion:Ljava/lang/String;
      28: aload_0
      29: ldc           #41                 // String os.name
      31: invokestatic  #29                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      34: putfield      #43                 // Field osName:Ljava/lang/String;
      37: aload_0
      38: ldc           #45                 // String os.arch
      40: invokestatic  #29                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      43: putfield      #47                 // Field osArch:Ljava/lang/String;
      46: invokestatic  #49                 // Method java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
      49: astore_2
      50: aload_0
      51: aload_2
      52: invokevirtual #55                 // Method java/lang/Runtime.availableProcessors:()I
      55: putfield      #59                 // Field coreNumber:I
      58: invokestatic  #61                 // Method java/lang/management/ManagementFactory.getMemoryMXBean:()Ljava/lang/management/MemoryMXBean;
      ```
      javap -c -p out/ua/sikorskyi/probe/SystemProbe.class
Compiled from "SystemProbe.java"
public class ua.sikorskyi.probe.SystemProbe {
  java.lang.String vendorName;

  java.lang.String vendorVersion;

  java.lang.String osName;

  java.lang.String osArch;

  int coreNumber;

  long startupMemory;

  long maxMemory;

  long usedMemory;

  java.lang.String allArgs;

  public ua.sikorskyi.probe.SystemProbe(java.lang.String[]);
    Code:
       0: aload_0
       1: invokespecial #20                 // Method java/lang/Object."<init>":()V
       4: aload_0
       5: ldc           #23                 // String Не передано
       7: putfield      #25                 // Field allArgs:Ljava/lang/String;
      10: aload_0
      11: ldc           #27                 // String java.vendor
      13: invokestatic  #29                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      16: putfield      #35                 // Field vendorName:Ljava/lang/String;
      19: aload_0
      20: ldc           #37                 // String java.version
      22: invokestatic  #29                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      25: putfield      #39                 // Field vendorVersion:Ljava/lang/String;
      28: aload_0
      29: ldc           #41                 // String os.name
      31: invokestatic  #29                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      34: putfield      #43                 // Field osName:Ljava/lang/String;
      37: aload_0
      38: ldc           #45                 // String os.arch
      40: invokestatic  #29                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      43: putfield      #47                 // Field osArch:Ljava/lang/String;
      46: invokestatic  #49                 // Method java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
      49: astore_2
      50: aload_0
      51: aload_2
      52: invokevirtual #55                 // Method java/lang/Runtime.availableProcessors:()I
      55: putfield      #59                 // Field coreNumber:I
      58: invokestatic  #61                 // Method java/lang/management/ManagementFactory.getMemoryMXBean:()Ljava/lang/management/MemoryMXBean;
```

```invokevirtual``` - це вказівка виконати метод за певним номером у пулі констант.


### Приклад виводу програми

```bash
alex@fedora:~/dev/java-study/task1$ java -jar dist/system-probe.jar арг1 арг2
java.vendor:                        Eclipse Adoptium
java.version:                       21.0.11
os.name:                            Linux
os.arch:                            amd64
Available processors (cores):       16
Initial memory (MB):                1008
Maximum memory (MB):                16056
Used memory (MB):                   0
Arguments:                          арг1; арг2
```
