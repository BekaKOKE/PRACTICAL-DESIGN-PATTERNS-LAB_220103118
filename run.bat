@echo off
if not exist out mkdir out
dir /s /b src\*.java test\*.java > sources.txt
javac -d out @sources.txt
del sources.txt
for %%i in (1 2 3 4 5) do (
  echo ===== TASK %%i =====
  java -Duser.language=en -Duser.country=US -cp out task%%i.Task%%iRunner
  echo.
)
for %%i in (1 2 3 4 5) do java -Duser.language=en -Duser.country=US -cp out task%%i.Task%%iTest
