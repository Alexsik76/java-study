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

 