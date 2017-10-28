    1. REPOSITORY

        - https://github.com/marcusviniciusfs/usertracking.git;

    2. BUILD

        - Execute mvn install to build it;
        - The distribution zip will be provided in target/usertracking-<VERSION>-bin.zip directory;

    3. INSTALLATION REQUIREMENTS

        - Linux or Windows;
        - Java 1.7 or superior (Oracle ou OpenJDK);
        - Postgre Database;

    4. INSTALLATION

        - There are two options to create the database: manually following the steps 'a', 'b', 'c', 'd' and 'e' or automatically;

        - If you prefer the second option, go to 'e' step configuring the database properties and than execute the command
          java -jar usertracking-<VERSION>.jar with the parameter -m at the prompt command and the application will uses the
          liquibase plugin to create a database with default configuration. Ex: <java -jar usertracking-<VERSION>.jar -m>

        a. Unzip the distribution zip into a directory the user's choice;

        b. Create a database at chosen BD;

        c. Execute the creation script of the BD schema available at directory <sql/create.sql>;

        d. Execute the specific script available at directory <sql/postgre.sql>;

        e. Configure the following options: Connection URL, Driver JDBC, User and Password. Sample:

            - hibernate.connection.url=jdbc:postgresql://127.0.0.1/usertracking
            - hibernate.connection.username=postgres
            - hibernate.connection.password=postgres
            - hibernate.connection.driver_class=org.postgresql.Driver

    5. INITIALIZATION

        - Execute in the root installation directory: <java -jar usertracking-<VERSION>.jar start>

    6. TEST

        - Once the application is up, you can edit the 'test_usertracking' shell script with the end point
          http://${DEFAULT_HOSTNAME}:${DEFAULT_PORT}/service/conta ct to insert a test contact;