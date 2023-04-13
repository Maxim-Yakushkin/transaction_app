package com.yakushkin.transaction_app.sql;

public class AccountSqlQuery {

    public static final String FIND_BY_USER_ID = """
            SELECT
                id,
                user_id,
                balance,
                currency
            FROM account
            WHERE user_id = ?
             """;
    public static final String FIND_BY_USER_ID_AND_CURRENCY = """
            SELECT *
            FROM account
            WHERE user_id = ? AND currency = ?
            """;
}
