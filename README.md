# API de loja

#### Autor
[Erique Rocha](https://github.com/EriqueRocha)

## Apresentação:
Esta é uma aplicaão de back end completa e funcional, integrada com a plataforma Asaas para gerar cobranças em todas as formas de pagamento parcelado ou não

### Tecnologias:
* Java 17
* SpringBoot 2.7.4
* Spring Security
* JWT
* Password Encoder
* PostgreSQL
* Autoridades
* SpringDataJpa
* Hibernate
* SpringWeb
* Swagger OpenAPI
* Global Exception Handlers
* Flyway

### Autenticação:
Neste caso, a Autenticação para provar quem é o usuário é feita por login(email) e senha

### Autoridade (Roles):
Define a qual grupo o perfil pretence, neste caso, usuário e administrador e cada perfil terásuas autorizações de acordo com seu grupo

Grupos/perfis utilizados:
```
username = user, senha = UserSenha, role = USER

username = admin, senha = ManagerSenha, role = MANAGER
```

>Ver migration `V01_02__insert_data_tab_usuario.sql`

### Dependências:
```XML
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
	<groupId>org.flywaydb</groupId>
	<artifactId>flyway-core</artifactId>
</dependency>

<dependency>
	<groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
	<scope>runtime</scope>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-test</artifactId>
	<scope>test</scope>
</dependency>

<dependency>
	<groupId>org.springdoc</groupId>
	<artifactId>springdoc-openapi-ui</artifactId>
	<version>1.6.4</version>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<dependency>
	<groupId>commons-io</groupId>
	<artifactId>commons-io</artifactId>
	<version>2.8.0</version>
</dependency>

<dependency>
  <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.7.0</version>
</dependency>
```

### SpringDataJpa:
```XML
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
Implementa uma camada de acesso aos dados de forma facilitada, faça seus métodos personalizados e o Spring fornecerá a implementação

### Flyway:
```XML
<dependency>
	<groupId>org.flywaydb</groupId>
	<artifactId>flyway-core</artifactId>
</dependency>
```
ajuda com a migração de banco de dados, no caso, está sendo utilizado para que a aplicação inicie com um administrador e um usuário cadastrado

### PostgreSQL:
```XML
<dependency>
	<groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
	<scope>runtime</scope>
</dependency>
```
drive para a utilização do SGBD PostgreSQL

### Springdoc openapi ui:
```XML
<dependency>
	<groupId>org.springdoc</groupId>
	<artifactId>springdoc-openapi-ui</artifactId>
	<version>1.6.4</version>
</dependency>
```
biblioteca que ajuda a automatizar a geração de documentação da API usando projetos de inicialização do spring

### Spring validation:
```XML
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
uma especificação Java que tem a finalidade de lidar com as validações de dados de um modo centralizado, tendo em vista que elas são integradas ao código a partir de anotações

### commons io:
```XML
<dependency>
	<groupId>commons-io</groupId>
	<artifactId>commons-io</artifactId>
	<version>2.8.0</version>
</dependency>
```
A biblioteca Apache Commons IO contém classes de utilitários, implementações de fluxo, filtros de arquivos, Comparadores de arquivos e muito mais

### Spring security:
```XML
<dependency>
  <groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
O Spring Security é uma estrutura que fornece autenticação, autorização e proteção. Com suporte de primeira classe para proteger aplicativos imperativos e reativos, é o padrão de proteção para aplicativos baseados em Spring

### Json web token:
```XML
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt</artifactId>
	<version>0.7.0</version>
</dependency>
```
JSON Web Token (JWT) é um padrão que define uma maneira compacta e independente para transmitir informações com segurança entre as partes como um objeto JSON

### Arquivo usado para configuração do docker:
este Arquivo é usado para criar a imagem Docker
>Ver `Dockerfile`
```Dockerfile
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk maven -y
COPY . .

RUN mvn package

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/loja-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
```
