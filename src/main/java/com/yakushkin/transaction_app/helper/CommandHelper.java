package com.yakushkin.transaction_app.helper;

import java.util.Arrays;

public enum CommandHelper {

    ADD_USER("-u", MessageHelper.CREATE_USER_COMMAND_MESSAGE),
    ADD_ACCOUNT("-a", MessageHelper.CREATE_ACCOUNT_COMMAND_MESSAGE),
    ADD_TRANSACTION("-t", MessageHelper.CREATE_TRANSACTION_COMMAND_MESSAGE);

    private final String command;
    private final String description;

    CommandHelper(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public static CommandHelper findByCommand(String command) {
        return Arrays.stream(CommandHelper.values())
                .filter(commandHelper -> commandHelper.getCommand().equals(command))
                .findFirst()
                .orElse(null);
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }
}
