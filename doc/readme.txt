eclothing-original

1.PROJECT_DESCRIPTION
2.GENERAL_NOTES
3.LOCAL_DEVELOPMENT_SETUP

1.PROJECT_DESCRIPTION

eclothing-original is a clothing focused e-commerce backend application.
Used technologies: maven, Spring Boot, Hibernate, iyzico, mariadb (for local development)


2.GENERAL_NOTES

package structure:
    com.eg.controller: rest endpoint classes are stored here
    com.eg.dto: DTO classes are here, controller classes receive DTO return DTO
    com.eg.entity: @Entity annotated classes are stored here, ORM mapped classes
    com.eg.repo: JPA/Hibernate query methods are stored here, each @Entity class has its own Repo such as Product and ProductRepo
    com.eg.service: service classes are stored here, actual operations are done inside these classes

src/main/resources/application.properties
    project configuration file, important features:
        spring.jpa.hibernate.ddl-auto:
            create: drop existing tables create new tables
            none: don't change existing db tables

        spring.datasource.url:
            set db connection url

        spring.jpa.properties.hibernate.dialect:
            set db dialect

doc/ : sample postman requests and this readme.txt file stored here

product images are stored in eclothingImage folder, this folder will be created on project startup
directory structure

eclothingImage
    1/      //product with id 1
        0.jpg       //product can have multiple images
        1.jpg
    2/      //product with id 2
        0.jpg       //product can have multiple images
        1.jpg


3.LOCAL_DEVELOPMENT_SETUP

mysql or mariadb required
    create a schema called "eclothing"
    src/main/resources/application.properties set "spring.jpa.hibernate.ddl-auto=" to "create"
    start application from IDE:
        open Application.java, click play icon
        empty db tables will be created
        in console you should see:
            "2019-09-24 21:58:05.233  INFO 7235 --- [ main] com.eg.eclothing.Application : Started Application in 3.264 seconds (JVM running for 3.568)"
        program ready to use

    sample postman requests are stored in doc/ folder, import from postman and use
    request order: beacuse of db constraint integrity, data creation requests should be sent in order

    1- create_category
    2- create_base_product
    3- create_product
    4- create_stock

