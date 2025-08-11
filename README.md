# Near&Far

## Introduction

Near&Far is an innovative application that helps users find the perfect residential location tailored to their
preferences. It allows users to search for housing based on a selected budget and preferred mode of transportation.
Additionally, users can define the types of areas they want nearby, such as parks or schools.

### Key Features:

- Budget selection based on:
    - Distance (meters)
    - Time (seconds)
    - Fuel (liters)
    - Energy (kWh – for electric vehicles)
- Support for multiple transportation modes:
    - Walking
    - Driving
    - Public transportation
- Search for specific nearby areas (e.g., parks)
- Stores user requests and search results in a database

## APIs Used

The application utilizes the following APIs provided by TomTom:

- **Routing API** – to define routes and budgets
- **Search API** – to search for points of interest and areas on the map

## How to Start the Application

1. **Prerequisites**:
    - Installed [Docker](https://www.docker.com/) and [Docker Compose](https://docs.docker.com/compose/).
    - Port 8080 must be free (the application listens on this port).

2. **Start the application**:
   In the project's root directory, run the following command:
   ```bash
    sudo apt-get update
    sudo apt-get install docker-model-plugin
    docker model pull ai/llama3.2:3B-Q4_0 
   
   docker-compose up
   ```
   This command will start both the database and the Spring Boot application.

3. **SwaggerUI**:
   Once the application is running, the API documentation (SwaggerUI) will be available at:
   [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

4. **Stop the application**:
   To stop the application, use the command:
   ```bash
   docker-compose down

## Developers Section

### Team Member

- **Bartłomiej Dygasiński**
    - Role: Fullstack Developer
    - Responsibilities: Developing MOK module
    - Contact
        - Email: bartlomiej.dygasinski@tomtom.com
        - GitHub: [BartekDygasinski-TomTom](https://github.com/BartekDygasinski-TomTom)
        - Slack: [Bartek Dygasiński](https://tomtomslack.slack.com/team/U0938BKQCTA)
- **Mateusz Giełczyński**
    - Role: Fullstack Developer
    - Responsibilities: Developing Routing API integration
    - Contact
        - Email: mateusz.gielczynski@tomtom.com
        - GitHub: [MateuszGielczynski-TomTom](https://github.com/MateuszGielczynski-TomTom)
        - Slack: [Mateusz Giełczynski](https://tomtomslack.slack.com/team/U092UQLHEU9)
- **Marcin Targoński**
    - Role: Fullstack Developer
    - Responsibilities: Developing Search API integration
    - Contact
        - Email: marcin.targonski@tomtom.com
        - GitHub: [MarcinTargonski-TomTom](https://github.com/MarcinTargonski-TomTom)
        - Slack: [Marcin Targoński](https://tomtomslack.slack.com/team/U0921933NLX)

### Enums

#### Budget Types (BudgetType):

```java

@AllArgsConstructor
@Getter
public enum BudgetType {
    DISTANCE("distanceBudgetInMeters"),
    TIME("timeBudgetInSec"),
    ENERGY("energyBudgetInkWh"),
    FUEL("fuelBudgetInLiters");

    private final String queryParamName;
}
```

#### Travel Modes (TravelMode):

```java
public enum TravelMode {
    CAR, BUS
}
```

## Links

- **SwaggerUI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **TomTom APIs**:
    - [Routing API](https://developer.tomtom.com/routing-api/documentation)
    - [Search API](https://developer.tomtom.com/search-api/documentation)

## Internal Components

- **Database**: We use PostgreSQL to store search queries and results.
- **Docker Compose**: The `docker-compose.yml` file handles the startup of the database and the Spring Boot application.
- **Spring Boot**: The backend is implemented using the Spring Boot framework.
