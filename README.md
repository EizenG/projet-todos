
# Todo
basic todo project for teaching purpose only 

# Requirements
<ul>
 <li>Maven </li>
  <li>Java 17</li>
</ul>

# Run unit tests
```sh
mvn test -Put
```

# Run integration tests
```sh
mvn test -Pit
```
# Dependencies analysis
```sh
mvn clean verify -DskipTests -Pcve
```

# Run the microservice
 --- 
```sh
mvn spring-boot:run
```

## Acces to endpoints
http://localhost:8080/cicd/api/todos


## Contact

Dr. SENE - <a href="mailto:senei@ept.sn">senei@ept.sn</a>
