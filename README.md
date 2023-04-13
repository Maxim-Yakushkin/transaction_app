### NOTES!

1. Для хранения данных используется SQLite
2. БД для основного проекта (__transaction.db__) раположена по пути: _src/main/resources/database/transaction.db_
3. Структура и данные для инициализации (не обязательны) для __transaction.db__ описаны в
   sql-файле по пути: _src/main/resources/database/init_db.sql_ (__НУЖНО ЗАПУСТИТЬ ПЕРЕД СТАРТОМ ПРИЛОЖЕНИЯ__)
4. БД для интеграционных тестов (IT) (__test_database.db__) раположена по пути: _src/test/resources/test_database.db_
5. Структура для __test_database.db__ описана в sql-файле по пути: _src/test/resources/init_test_db.sql_
   (__НЕ НУЖНО ЗАПУСКАТЬ. БД СОЗДАЕТСЯ И УДАЛЯЕТСЯ АВТОМАТИЧЕСКИ ПРИ СТАРТЕ И ОКОНЧАНИИ ТЕСТОВ__)