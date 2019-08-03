FROM openjdk:8 
ADD /target/bookify.war bookify.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bookify.war"]
