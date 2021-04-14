call cls 
call gradlew build
call docker build -t arunkumarshahi/person-reactive-service .  
call docker-compose up -d 
call docker ps

call docker ps -aqf "name=kafka-stream_kafka-stream_1"


