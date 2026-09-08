# Tennis Scoreboard

Веб-приложение для ведения счёта теннисного матча.

Проект реализован на Java с использованием Spring MVC, Hibernate и PostgreSQL. Пользователь может создать матч между двумя игроками, вести счёт по ходу игры и после завершения матча просматривать его в истории.

## Возможности

- создание нового теннисного матча;
- добавление игроков;
- подсчёт очков внутри гейма;
- обработка `Deuce` и `Advantage`;
- подсчёт геймов внутри сета;
- определение победителя сета;
- поддержка тай-брейка;
- определение победителя матча;
- автоматическое сохранение завершённых матчей в PostgreSQL;
- просмотр истории завершённых матчей;
- фильтрация матчей по имени игрока;
- постраничный вывод истории матчей;
- обработка некорректных параметров и отсутствующих матчей;
- серверный HTML-интерфейс на Thymeleaf.

## Технологии

### Backend

- Java 21
- Spring Framework 7.0.8
- Spring MVC
- Hibernate ORM 7.4.4.Final
- Hibernate Validator 9.1.2.Final
- PostgreSQL
- JDBC
- Maven

### Frontend

- Thymeleaf 3.1.5
- HTML
- CSS

### Web / Server

- Jakarta Servlet API 6.1
- Apache Tomcat 11

### Testing

- JUnit 5.11

## Архитектура

Приложение построено с разделением на несколько уровней:

```text
Controller
    ↓
Service
    ↓
DAO
    ↓
Hibernate
    ↓
PostgreSQL
```

Отдельно от слоя работы с базой реализована модель подсчёта счёта теннисного матча:

```text
MatchScore
    ↓
SetState
    ↓
GameState
```

Для тай-брейка используется отдельный компонент:

```text
SetState
    ↓
TieBreakState
```

Текущее состояние незавершённых матчей хранится в памяти приложения, а завершённые матчи сохраняются в PostgreSQL.

## Структура проекта

Основная структура:

```text
src
├── main
│   ├── java
│   │   └── org.mserz_o.springcourse
│   │       ├── config
│   │       ├── controller
│   │       ├── dao
│   │       ├── dto
│   │       ├── exception
│   │       ├── mapper
│   │       ├── model
│   │       │   ├── entity
│   │       │   ├── enums
│   │       │   └── memory
│   │       └── service
│   │
│   ├── resources
│   │   └── hibernate.properties.example
│   │
│   └── webapp
│       ├── WEB-INF
│       │   └── views
│       └── resources
│           └── css
│
└── test
    └── java
        └── org.mserz_o.springcourse
            └── model
                └── memory
```

### Основные компоненты

`controller`  
Обрабатывает HTTP-запросы и отвечает за навигацию между страницами приложения.

`service`  
Содержит бизнес-логику приложения и координирует работу между контроллерами, DAO и моделью матча.

`dao`  
Отвечает за взаимодействие с базой данных через Hibernate.

`model.entity`  
JPA-сущности, соответствующие таблицам PostgreSQL.

`model.memory`  
Модель текущего состояния теннисного матча.

`model.enums`  
Перечисления для состояний очков, геймов, сетов и матча.

`dto`  
Объекты для передачи данных между слоями приложения и представлениями.

`mapper`  
Преобразование Entity и объектов состояния матча в DTO.

`exception`  
Пользовательские исключения и централизованная обработка ошибок.

## Подсчёт теннисного счёта

Логика матча разделена на несколько уровней.

### Очки

Стандартная последовательность:

```text
0 → 15 → 30 → 40
```

При счёте `40:40` наступает `Deuce`.

После `Deuce`:

```text
Deuce
  ↓
Advantage
  ↓
Game Win
```

Если игрок, находящийся в Advantage, проигрывает следующее очко, счёт возвращается к `Deuce`.

### Сет

Для стандартного сценария игрок должен выиграть минимум 6 геймов с преимуществом минимум в 2 гейма.

Например:

```text
6:0
6:1
6:2
6:3
6:4
7:5
```

При счёте `6:6` запускается тай-брейк.

### Тай-брейк

Тай-брейк ведётся отдельной моделью `TieBreakState`.

Игроку необходимо набрать минимум 7 очков и иметь преимущество минимум в 2 очка:

```text
7:0
7:5
8:6
9:7
...
```

### Матч

Победитель матча определяется после необходимого количества выигранных сетов.

После победы завершённый матч сохраняется в базе данных, а активный матч удаляется из памяти приложения.

## База данных

Для работы приложения используется PostgreSQL.

Основные таблицы:

```text
Players
Matches
```

Связи:

```text
Players
   ↑
   │
Matches
 ├── player1_id
 ├── player2_id
 └── winner_id
```

Для создания базы данных можно использовать следующий SQL:

```sql
CREATE DATABASE "tennis-scoreboard-repository";
```

Структура таблиц должна соответствовать используемым Hibernate-сущностям.

## Настройка подключения к PostgreSQL

Файл с реальными настройками подключения:

```text
src/main/resources/hibernate.properties
```

не должен добавляться в Git, поскольку содержит credentials для базы данных.

В репозитории находится шаблон:

```text
src/main/resources/hibernate.properties.example
```

Создайте на его основе собственный файл:

```text
hibernate.properties
```

и укажите свои параметры:

```properties
hibernate.driver_class=org.postgresql.Driver
hibernate.connection.url=jdbc:postgresql://localhost:5432/tennis-scoreboard-repository
hibernate.connection.username=YOUR_USERNAME
hibernate.connection.password=YOUR_PASSWORD
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql=true
```

## Требования

Перед запуском необходимо установить:

- JDK 21 или совместимую JDK;
- Maven;
- PostgreSQL;
- Apache Tomcat 11.

Проверить Java:

```bash
java -version
```

Проверить Maven:

```bash
mvn -version
```

## Запуск проекта

### 1. Клонирование

```bash
git clone https://github.com/erkoshqq/tennis-scoreboard.git
```

Перейдите в директорию проекта:

```bash
cd tennis-scoreboard
```

### 2. Настройка PostgreSQL

Создайте базу данных:

```text
tennis-scoreboard-repository
```

Создайте:

```text
src/main/resources/hibernate.properties
```

на основе:

```text
src/main/resources/hibernate.properties.example
```

и укажите собственные credentials.

### 3. Запуск тестов

```bash
mvn clean test
```

### 4. Сборка WAR

```bash
mvn clean package
```

После успешной сборки WAR-файл будет находиться здесь:

```text
target/tennis-scoreboard.war
```

## Запуск через Tomcat

Скопируйте:

```text
target/tennis-scoreboard.war
```

в директорию:

```text
<TOMCAT_HOME>/webapps/
```

Запустите Tomcat.

После развёртывания приложение будет доступно по адресу:

```text
http://localhost:8080/tennis-scoreboard/
```

## Основные страницы

### Главная

```text
/
```

Главная страница приложения.

### Создание матча

```text
/matches/new-match
```

Позволяет указать имена двух игроков и начать новый матч.

### Страница текущего матча

```text
/matches/{uuid}
```

Отображает текущий счёт матча и позволяет добавлять очки игрокам.

### История матчей

```text
/matches
```

Отображает завершённые матчи.

Поддерживается фильтрация по имени игрока и постраничная навигация.

## Тестирование

В проекте присутствуют unit-тесты для основной логики подсчёта счёта:

- `GameStateTest`
- `SetStateTest`
- `TieBreakStateTest`
- `MatchScoreTest`

Запуск:

```bash
mvn clean test
```

Текущий набор тестов покрывает:

- переходы между теннисными очками;
- `Deuce`;
- `Advantage`;
- завершение гейма;
- завершение сета;
- тай-брейк;
- завершение матча.

## Сборка проекта

Для полной проверки и сборки проекта используется:

```bash
mvn clean package
```

Команда выполняет:

1. очистку предыдущей сборки;
2. компиляцию исходного кода;
3. компиляцию тестов;
4. запуск тестов;
5. сборку WAR-файла.

Результат:

```text
target/tennis-scoreboard.war
```

## Обработка ошибок

В приложении используется централизованная обработка исключений через `@ControllerAdvice`.

Обрабатываются, в частности:

- отсутствующие матчи;
- некорректные UUID;
- некорректные параметры запросов;
- некорректно указанная сторона игрока.

Для пользователя отображается отдельная страница ошибки.

## Безопасность конфигурации

Файл:

```text
hibernate.properties
```

содержащий реальные данные подключения к PostgreSQL, не должен попадать в Git.

Для этого используется:

```text
.gitignore
```

В репозитории хранится только:

```text
hibernate.properties.example
```

с примером конфигурации без реальных credentials.

## Особенности проекта

Проект намеренно реализован без Spring Boot.

Конфигурация Spring выполняется через Java Configuration:

```text
SpringConfig
WebMVCDispatcherServletInitializer
```

Приложение собирается в стандартный WAR и разворачивается на Apache Tomcat.

Для серверного рендеринга HTML используется Thymeleaf.

## Статус проекта

Проект завершён в рамках учебного задания.

На текущем этапе:

- бизнес-логика подсчёта теннисного счёта реализована;
- unit-тесты проходят успешно;
- приложение собирается в WAR;
- WAR успешно разворачивается на Tomcat;
- PostgreSQL используется для хранения завершённых матчей;
- основной пользовательский сценарий протестирован локально.

Следующий этап развития проекта — развёртывание приложения на удалённом Linux-сервере.

## Учебный контекст

Проект выполнен в рамках практики по Java Backend / Spring и предназначен для изучения:

- Spring MVC;
- Dependency Injection;
- Hibernate;
- PostgreSQL;
- Maven;
- Jakarta Servlet;
- Thymeleaf;
- работы с HTTP;
- MVC-архитектуры;
- DTO и Mapper;
- обработки исключений;
- транзакций;
- unit-тестирования;
- сборки и развёртывания WAR-приложений.

## Автор

**erkoshqq**

GitHub:

https://github.com/erkoshqq

---

Если проект оказался полезен или интересен — ⭐ репозиторию на GitHub будет приятно.