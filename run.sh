docker-compose down
docker image rm movie-booking-app_consumer:latest
docker image rm movie-booking-app_web:latest
cd ./web
mvn clean package
cd ../consumer
mvn clean package
cd ../
docker-compose up
