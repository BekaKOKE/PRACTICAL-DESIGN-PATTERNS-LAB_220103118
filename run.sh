#!/bin/sh
javac -d out src/task*/*.java test/task*/*.java || exit 1
for i in 1 2 3 4 5; do
  echo "===== TASK $i ====="
  java -Duser.language=en -Duser.country=US -cp out task$i.Task${i}Runner
  echo
done
for i in 1 2 3 4 5; do
  java -Duser.language=en -Duser.country=US -cp out task$i.Task${i}Test || exit 1
done
