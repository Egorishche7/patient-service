# Patient Service

## Описание

Этот проект представляет собой REST-сервис для управления сущностями `Patient`.
Проект включает CRUD-методы для работы с пациентами и использует авторизацию по
протоколу OAuth2 с использованием Keycloak.

## Функциональность

- **GET /patients**: Получить список всех пациентов (доступно для ролей `Practitioner` и `Patient`)
- **GET /patients/{id}**: Получить информацию о пациенте по ID (доступно для ролей `Practitioner` и `Patient`)
- **POST /patients**: Создать нового пациента (доступно для роли `Practitioner`)
- **PUT /patients/{id}**: Обновить информацию о пациенте (доступно для роли `Practitioner`)
- **DELETE /patients/{id}**: Удалить пациента по ID (доступно для роли `Practitioner`)

## Используемые технологии

- Spring Boot
- Spring Security
- Spring Data JPA
- Keycloak (OAuth2)
- Swagger/OpenAPI

## Требования

- Java 22
- Keycloak сервер
- СУБД MySQL

## Установка и запуск

### Шаг 1: Настройка Keycloak

1. Запустите Keycloak сервер:  
   - Windows:
   ```sh
   kc.bat start-dev
   ```
   - Linux/macOS:
   ```sh
    kc.sh start-dev
   ```
2. Настройте Keycloak с необходимыми клиентами и ролями:
    - Создайте Realm (например, `PatientService`)
    - Создайте клиента (например, `patient-service`)
    - Создайте роли `Practitioner` и `Patient`
    - Создайте пользователей и назначьте им соответствующие роли
    - Создайте User Realm Roles Mapper со свойством Token Claim Name `spring_roles` 

### Шаг 2: Настройка базы данных

1. Убедитесь, что у вас установлен и запущен MySQL на вашем компьютере
2. Создайте базу данных (например, с именем `patient-service`)
3. Таблицы будут созданы автоматически после запуска приложения

### Шаг 3: Настройка конфигурационного файла

1. Откройте файл src/main/resources/application.yml
2. Обновите настройки соединения с БД:
   ```
   datasource:
     url: jdbc:mysql://localhost/patient-service
     username: root
     password: 111111
   ```
3. Обновите настройки Keycloak:
   ```
   security:
     oauth2:
       resourceserver:
         jwt:
           issuer-uri: http://localhost:8080/realms/PatientService
       client:
         provider:
           keycloak:
             issuer-uri: http://localhost:8080/realms/PatientService
             user-name-attribute: preferred_username
         registration:
           keycloak:
             client-id: patient-service
             client-secret: gJY6RNJdsMPHHnCyZAjpDLjs14HW1NBW
             scope: openid
   ```
4. Укажите username и password пользователя, у которого есть роль Practitioner:
   ```
   practitioner:
     username: practitioner
     password: practitioner
   ```

### Шаг 4: Запуск приложения

1. Клонируйте репозиторий:
    ```sh
    git clone https://github.com/Egorishche7/patient-service
    ```
2. Перейдите в корневую папку проекта и запустите Spring Boot приложение:
   ```sh
   mvn spring-boot:run
   ```

## Использование приложения

### Генерация данных

В проекте предусмотрено консольное приложение добавления через API 100 сгенерированных сущностей `Patient`.
Это приложение автоматически запускается при старте сервера.

### Авторизация по протоколу OAuth2

Для выполнения запросов к API необходимо получить access_token с помощью Keycloak.
1. Пример отправки запроса к серверу для получения токена:
   ```
   POST http://localhost:8080/realms/PatientService/protocol/openid-connect/token
   Content-Type: application/x-www-form-urlencoded
   
   client_id=patient-service&
   client_secret=gJY6RNJdsMPHHnCyZAjpDLjs14HW1NBW&
   username=practitioner&
   password=practitioner&
   grant_type=password
   ```
2. Используйте полученный токен для выполнения запросов к API, например:
   ```
   GET http://localhost:8081/patients
   Authorization: Bearer <access_token> 
   ```

### Swagger UI

1. Убедитесь, что Spring Boot приложение запущено
2. Swagger UI доступен по адресу: `http://localhost:8081/swagger-ui/index.html`
3. В интерфейсе Swagger UI вы можете ознакомиться с документацией API и выполнить запросы

### Коллекция Postman

В репозитории находится коллекция Postman для тестирования API.
Вы можете импортировать ее и использовать для выполнения запросов.

1. Откройте Postman
2. Нажмите на кнопку Import
3. Выберите Postman-коллекцию postman/Patient Service API.postman_collection.json
4. Коллекция появится в вашем списке коллекций
