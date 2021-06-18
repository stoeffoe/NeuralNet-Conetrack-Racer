# Opdracht4

### Compile
```
mvn package
```

### Compile including dependencies
```
mvn clean compile assembly:single
```

### Run
```
java -cp ".\target\Opdracht4-0.1.jar;.\target\dependency\*" AutoCoureur.App
```

### Compile and Run
```
mvn package; java -cp ".\target\Opdracht4-0.1.jar;.\target\dependency\*" AutoCoureur.App
```

### Run python server manually
```
py .\src\main\python\AutoCoureur\lidar_socketpilot_world.py
```

### Conda
If you want to use conda

replace this line in Car:
```
"cmd /c start pythonServer.bat " 
```
with this line:
```
"cmd /c conda activate Tinlab_opdracht_4 && start pythonServer.bat "
```
