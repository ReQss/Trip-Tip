# Spring Boot with ReactJS using WebPack and Yarn. #

### Summary ###

For now this repository contains an example Spring-Boot / ReactJS project.

### Running ###

To run the front end, cd into ./frontend and run

```yarn start```

By default, this will start the front end at http://localhost:3000. If you want to
change the default port, modify package.json and change the start script section to

``` start: export PORT=3006 && react-scripts start```

To run the back end, from the project root directory, run

```mvn spring-boot:run```

By default, this will start the backend server at http://localhost:8080.

To change the port, modify src/main/resources/application.properties to include server.port=xxxx where xxxx is your desired port number.

### Installation ###

In the source directory, run

```mvn install -DskipTests```

In the example-frontend directory run

``` yarn install```

You might have to install the appropriate versions of npm or mvn.

### Versions ###
- Npm 4.6.1 
- Maven 3.3.9  
- JDK 1.8.0_92


