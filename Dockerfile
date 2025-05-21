FROM openjdk:21-jdk

# กำหนดชื่อไฟล์ JAR ของแอป restaurant
ARG LOCAL_APP_FILE=restaurant-0.0.1-SNAPSHOT.jar

# สร้างโฟลเดอร์แอป
RUN mkdir -p /home/app

# คัดลอก JAR เข้าไปใน container
COPY target/${LOCAL_APP_FILE} /home/app/app.jar

# ตั้ง working directory
WORKDIR /home/app

# เปิดพอร์ต 8080 (หรือพอร์ตที่แอปคุณรันอยู่)
EXPOSE 8080

# รันแอป
ENTRYPOINT ["java", "-jar", "app.jar"]
