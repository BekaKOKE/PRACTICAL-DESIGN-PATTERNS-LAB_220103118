# Practical Design Patterns Lab

Student ID: 220103118

Plain Java (JDK 8+), no external libraries.

| Task | Patterns | Package |
|------|----------|---------|
| 1. Secure Data Exporter | Strategy + Decorator | `src/task1` |
| 2. Legacy Stock Feed with Cache | Adapter + Proxy | `src/task2` |
| 3. Alert & Incident Engine | Bridge + Strategy | `src/task3` |
| 4. Currency Converter & Fees | Adapter + Decorator | `src/task4` |
| 5. Cloud Storage Security Gate | Bridge + Proxy | `src/task5` |

Each package has its `TaskNRunner` (the given verification runner). Extra tests are in `test/taskN/TaskNTest.java`.

## How to run

```
javac -d out src/task*/*.java test/task*/*.java
java -cp out task1.Task1Runner
java -cp out task1.Task1Test
```

or just `./run.sh` (Linux/macOS/Git Bash) / `run.bat` (Windows).

If your system locale uses a comma as decimal separator, add `-Duser.language=en -Duser.country=US` to the `java` command so prices print like `$183.00`.

## Notes

- Task 1: decorators are applied from the inner one to the outer one, so `Base64(HeaderFooter(BaseFileExporter))` encodes the header, JSON and footer together (same as the Base64 string in the expected output). The PDF says `Writing 236 bytes`, but that Base64 string is actually 360 characters long, so the program prints 360.
- Task 2: the cache timestamp is saved after the upstream call, so the cache age doesn't include the 250 ms network delay.
- Task 4: customs duty is rounded with `BigDecimal` (HALF_UP), because `145 * 1.085` in double gives `157.32499...` and would round to 157.32 instead of 157.33.
- Task 5: the proxy only allows known roles (ADMIN, MEMBER, ENGINEER). GUEST, empty or unknown roles are blocked.
