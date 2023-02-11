# Opdracht4
A demo of the project can be found [here](https://www.youtube.com/watch?v=NqzNcWjCp84).

## Prerequisites
- Java 8 or later
- Maven 3.x or later
- Python 3.x
- Required python packages, listed in requirements.txt. To install run the following command:
``` 
python -m pip install -r .\requirements.txt 
```

## Usage

### Compilation

Compile the maven project including all depencies:

```
mvn clean compile assembly:single
```

### Run

Run the entire project using:

```
java -cp .\target\Opdracht4-0.1.jar AutoCoureur.App
```

### Compile and Run

```
mvn clean compile assembly:single&java -cp .\target\opdracht4-1.0-jar-with-dependencies.jar AutoCoureur.App
```

### Run python car simulator server manually

To manually run the python car simulator server without the neuralnet, use the following command:

```
python .\src\main\python\CarSimulator\lidar_socketpilot_world.py [PORTNUM]
```

Replace [PORTNUM] with the port number for the server.

