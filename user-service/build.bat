@echo off
echo Building Docker image...
docker build -t user-service:latest .

echo Running Docker container...
docker run -e SPRING_PROFILES_ACTIVE=prod -p 8082:8082 user-service:latest

echo Docker container is running on port 8082