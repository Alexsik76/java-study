# Призначення застосунку

SystemProbe — це консольний діагностичний застосунок на Java, призначений для збору та відображення базової інформації про поточне середовище виконання JVM і системи. Програма виводить назву постачальника та версію Java, назву й архітектуру операційної системи, кількість доступних ядер процесора, показники використання оперативної пам'яті heap (початковий, максимальний та використаний обсяги у мегабайтах), а також передані аргументи командного рядка.

# Складання та запуск

1. **Компіляція:**
   ```bash
   javac -d out src/ua/sikorskyi/probe/SystemProbe.java
   ```

2. **Запуск з байт-коду:**
   ```bash
   java -cp out ua.sikorskyi.probe.SystemProbe арг1 арг2
   ```
   *Ключ `-cp` (`-classpath`) вказує шлях до каталогів або ZIP/JAR файлів із скомпільованими класами.*

3. **Збирання JAR-архіву:**
   ```bash
   jar cvfm dist/system-probe.jar manifest.txt -C out .
   ```
   *Ключі `jar`: `c` — створити новий архів; `v` — докладний вивід; `f` — вказати ім'я JAR-файлу; `m` — включити файл маніфесту; `-C` — перейти у вказану директорію для збереження структури пакунків.*

4. **Запуск з JAR-архіву:**
   ```bash
   java -jar dist/system-probe.jar арг1 арг2
   ```
   *Ключ `-jar` вказує JVM виконувати програму безпосередньо з JAR-файлу, точка входу в який визначена в маніфесті.*

# Приклад виводу

```
java.vendor:                        Eclipse Adoptium
java.version:                       21.0.11
os.name:                            Linux
os.arch:                            amd64
Available processors (cores):       16
Initial memory (MB):                1008.00
Maximum memory (MB):                16056.00
Used memory (MB):                   0.00
Arguments:                          арг1; арг2
```

*Примітка: `Used memory: 0.00` не свідчить про порожню купу. `MemoryMXBean` підсумовує статистику пулів пам'яті збирача G1, яка оновлюється при виділенні областей; на момент вимірювання виділень ще не було. Показник `Runtime.totalMemory() - freeMemory()` у той самий момент дає близько 4 МБ. Із послідовним збирачем (`-XX:+UseSerialGC`) значення ненульове одразу.*


# Дослідження байт-коду

Фрагмент байт-коду конструктора `SystemProbe`:

```bytecode
Compiled from "SystemProbe.java"
public class ua.sikorskyi.probe.SystemProbe {
  private static final long BYTES_IN_MEGABYTE;

  private static final int LABEL_WIDTH;

  private static final java.lang.String ROW_FORMAT;

  private final java.lang.String vendorName;

  private final java.lang.String javaVersion;

  private final java.lang.String osName;

  private final java.lang.String osArch;

  private final int processorCount;

  private final long startupMemory;

  private final long maxMemory;

  private final long usedMemory;

  private final java.lang.String allArgs;

  public ua.sikorskyi.probe.SystemProbe(java.lang.String[]);
    Code:
       0: aload_0
       1: invokespecial #3                  // Method java/lang/Object."<init>":()V
       4: aload_0
       5: ldc           #9                  // String java.vendor
       7: invokestatic  #11                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      10: putfield      #17                 // Field vendorName:Ljava/lang/String;
      13: aload_0
      14: ldc           #21                 // String java.version
      16: invokestatic  #11                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      19: putfield      #23                 // Field javaVersion:Ljava/lang/String;
      22: aload_0
      23: ldc           #26                 // String os.name
      25: invokestatic  #11                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      28: putfield      #28                 // Field osName:Ljava/lang/String;
      31: aload_0
      32: ldc           #31                 // String os.arch
      34: invokestatic  #11                 // Method java/lang/System.getProperty:(Ljava/lang/String;)Ljava/lang/String;
      37: putfield      #33                 // Field osArch:Ljava/lang/String;
      40: invokestatic  #36                 // Method java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
      43: astore_2
      44: aload_0
      45: aload_2
      46: invokevirtual #42                 // Method java/lang/Runtime.availableProcessors:()I
      49: putfield      #46                 // Field processorCount:I
      52: invokestatic  #50                 // Method java/lang/management/ManagementFactory.getMemoryMXBean:()Ljava/lang/management/MemoryMXBean;
      55: astore_3
```

Порівняльний аналіз інструкцій виклику методів:
* **`invokevirtual`**: Виконує виклик екземплярного методу (в даному випадку `Runtime.availableProcessors`) з динамічним зв'язуванням. Конкретна реалізація методу визначається під час виконання (runtime) за фактичним типом об'єкта на стеку, що реалізує поліморфізм.
* **`invokestatic`**: Використовується для виклику статичних методів класів (`System.getProperty`, `Runtime.getRuntime`). Для нього застосовується статичне зв'язування на етапі компіляції, оскільки для виклику не потрібен екземпляр об'єкта.
* **`invokespecial`**: Викликає приватні методи, конструктори (`super()` / `Object."<init>"`) або методи суперкласу. Зв'язування відбувається статично без динамічного диспетчеризування віртуальних методів.
