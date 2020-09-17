# Bibtek

The project is a three layered application consisting of the following layers:

- **domain** (*[bibtek.core](src/main/java/bibtek/core)*) - classes to represent data models used in the app
- **persistance** (*[bibtek.json](src/main/java/bibtek/json)*) - handles storing and loading of user generated data
- **user interface** (*[bibtek.ui](src/main/java/bibtek/ui)*) - handles display to screen and user input



## Source code structure

- **src/main/java** application code
- **src/main/resources** mainly FXML-files and app icon
- **src/test/java** for testing



## Maven setup

The project utilizes the build tool Maven for handling things like dependencies and test initialization.

Dependencies include:

- **javafx-fxml** - JavaFX
- **junit-jupiter-api, junit-jupiter-engine, mockito-core** - testing with JUnit5
- **testfx-core, testfx-junit5** - testing with TestFX
- **gson** - JSON serialization and deserialization 

in addition to the following plugins:

- **maven-compiler-plugin** - Java setup
- **maven-surefire-plugin** - testing
- **javafx-maven-plugin** - running JavaFX
- **maven-checkstyle-plugin, spotbugs-maven-plugin** - code quality check
- **jacoco-maven-plugin** - code coverage