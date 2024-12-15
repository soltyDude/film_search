# Используем официальный образ Tomcat
FROM tomcat:10.1-jdk17

# Установка рабочей директории в контейнере
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Копируем ваш WAR-файл в директорию веб-приложений Tomcat
COPY target/kino_search-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Указываем порт, который будет слушать Tomcat
EXPOSE 8080

# Запускаем Tomcat
CMD ["catalina.sh", "run"]
