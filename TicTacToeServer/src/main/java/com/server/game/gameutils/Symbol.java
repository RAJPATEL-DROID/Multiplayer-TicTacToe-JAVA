package com.server.game.gameutils;

public enum Symbol
{
    CROSS("X"), ZERO("O"), NO_SYMBOL(" ");

    private final String icon;

    Symbol(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return icon;
    }
}