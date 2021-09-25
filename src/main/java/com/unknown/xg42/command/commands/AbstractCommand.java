package com.unknown.xg42.command.commands;


public abstract class AbstractCommand
{
    private final String name;

    public AbstractCommand(String name)
    {
        this.name = name;
    }

    public abstract void execute(String[] args);

    public String getName()
    {
        return name;
    }

}
