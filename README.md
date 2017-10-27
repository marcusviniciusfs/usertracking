    REPOSITORY

    - https://github.com/marcusviniciusfs/usertracking.git;

    BUILD

    - Execute mvn install to start it;
    - O zip de distribuição será gerado em target/pacsng-<VERSÃO>-bin.zip

    INSTALLATION REQUIREMENTS
    Linux ou Windows

    Java 1.7 ou superior (Oracle ou OpenJDK)

    Um dos seguintes BDs:

    Microsoft SQL Server
    Oracle
    Postgre
    Apenas para Windows: Visual C++ Redistributable Packages for Visual Studio 2013
    (instale a versão x64 se for utilizar Java 64 bits, ou a versão x86 se for utilizar Java 32 bits)

    INSTALLATION
    Descompacte o zip de distribuição em um diretório a escolha do usuário

    Crie um database no BD escolhido

    Execute o script de criação do schema de BD disponível em sql/create.sql

    Execute o script específico para o BD escolhido no diretório sql (sql/postgre.sql, por exemplo)

    Renomeie o arquivo hibernate-sample.properties localizado na raiz da instalação para hibernate.properties e configure as seguintes opções:

    URL de conexão
    Usuário e senha
    Driver JDBC
    Apenas para o Postgre, é necessário configurar um dialeto personalizado: hibernate.dialect=com.pixeon.util.CustomPostgreSQLDialect
    Exemplo para o Postgre:

    hibernate.connection.url=jdbc:postgresql://127.0.0.1/pacsng
    hibernate.connection.username=postgres
    hibernate.connection.password=postgres
    hibernate.connection.driver_class=org.postgresql.Driver
    hibernate.dialect=com.pixeon.util.CustomPostgreSQLDialect
    hibernate.default_batch_fetch_size=100
    hibernate.hbm2ddl.auto=validate
    hibernate.connection.provider_class=org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider
