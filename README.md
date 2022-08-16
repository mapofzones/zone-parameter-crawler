# zone-parameter-crawler

Status of Last Deployment:<br>
<img src="https://github.com/mapofzones/zone-parameter-crawler/workflows/Java%20CI/badge.svg"><br>

Running directly:
* java 11
* maven

Running in a container:
* Docker

## Usage

Running directly:
* `mvn package -DskipTests` or `mvn package`
* `java -jar /opt/app.jar --spring.profiles.active=prod`

Config parameters:
* `SYNC_TIME` - time between iterations in '--env'
* `DB_URL` - URL of DB
* `DB_USER` - DB username
* `DB_PASS` - DB password

Running in a container:
* `docker build -t zone-parameter-crawler:v1 .`
* `docker run --env SYNC_TIME="120s" --env DB_URL=jdbc:postgresql://<ip>:<port>/<db> --env DB_USER=<db_user> --env DB_PASS=<db_user_pass> -it -d --network="host" zone-parameter-crawler:v1`