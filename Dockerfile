FROM openjdk:20-slim

WORKDIR /app

# Устанавливаем необходимые утилиты
RUN apt-get update && apt-get install -y unzip

# Копируем WAR файл и разархивируем его
COPY target/kino_search-1.0-SNAPSHOT.war /app/kino_search.war
RUN mkdir /app/classes && unzip /app/kino_search.war -d /app/classes

# Указываем точку входа для выполнения HibernateTest
ENTRYPOINT ["java", "-cp", "/app/classes/WEB-INF/classes:/app/classes/WEB-INF/lib/*:/app/postgresql-42.6.0.jar", "com.example.kino_search.testeClaces.HibernateTest"]
