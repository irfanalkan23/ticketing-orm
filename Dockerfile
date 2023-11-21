#"when we run with maven and jdk image" --> amd64/maven:3.8.6-openjdk-11
FROM amd64/maven:3.8.6-openjdk-11
WORKDIR usr/app
#copy all files of the app (whole project) and put in the current directory (/usr/app)
COPY  .  .
ENTRYPOINT ["mvn","spring-boot:run"]