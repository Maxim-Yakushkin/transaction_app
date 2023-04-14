### NOTES!

1. Основные CRUD операции выполняются неявно с помощью уже реализованного функционала Spring Boot JPA.
   Специфичные SQL-запросы выполняются с помощью JDBC и описаны здесь --> __com.yakushkin.transaction_app.sql.AccountSqlQuery__ 
2. Для хранения данных используется SQLite
3. БД для основного проекта (__transaction.db__) раположена по пути: _src/main/resources/database/transaction.db_
4. Структура и данные для инициализации __transaction.db__ описаны в
   sql-файле по пути: _src/main/resources/database/init_db.sql_ (__DDL часть - обязательна, DML часть - необязатльна__)
5. БД для интеграционных тестов (IT) (__test_database.db__) раположена по пути: _src/test/resources/test_database.db_
6. Структура для __test_database.db__ описана в sql-файле по пути: _src/test/resources/init_test_db.sql_
   (__НЕ НУЖНО ЗАПУСКАТЬ. СТРУКТУРА БД СОЗДАЕТСЯ И УДАЛЯЕТСЯ АВТОМАТИЧЕСКИ ПРИ СТАРТЕ И ОКОНЧАНИИ ТЕСТОВ__)
7. Перед запуском интеграционных тестов (__src/test/java/com/yakushkin/transaction_app/service/integration__)
   нужно закомментировать запуск основного контроллера здесь --> com/yakushkin/transaction_app/TransactionApplication.java:22
   <br>P.S.: временный обходной путь, чтобы не усложнять каждый тест функционалом по вводу данных с клавиатуры