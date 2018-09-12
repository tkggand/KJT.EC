package com.kjt.ec.data.configuration;

import com.kjt.ec.data.expressions.Expression;

import java.util.List;

public class DataCommandSection {

    private int timeout;
    private Expression commandText;
    private String commandName;
    private String commandType;
    private String database;
    private List<ParameterSection> Parameters;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Expression getCommandText() {
        return commandText;
    }

    public void setCommandText(Expression commandText) {
        this.commandText = commandText;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public List<ParameterSection> getParameters() {
        return Parameters;
    }

    public void setParameters(List<ParameterSection> parameters) {
        Parameters = parameters;
    }
}
