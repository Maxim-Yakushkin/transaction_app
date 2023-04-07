package com.yakushkin.transaction_app.sql;

public class AccountSql {

    public static final String FIND_BY_USER_ID = """
            SELECT
                id,
                balance,
                currency
            FROM account
            WHERE user_id = ?
             """;
}
