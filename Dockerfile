# 애플리케이션을 컨테이너 이미지로 만들기 위한 레시피 # 2025-05-13 - start
# 어떤 환경에서 어떤 명령어를 실행해서 어떤 결과물을 컨테이너 안에 넣을지 정의하는 파일이다
FROM openjdk:17-jdk-alpine
COPY build/libs/*.jar app.jar

#컨테이너가 실행될 때 자바 애플리케이션을 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]

# 젠킨스 TEST 진행
