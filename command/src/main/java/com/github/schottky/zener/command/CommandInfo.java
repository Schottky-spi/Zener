package com.github.schottky.zener.command;

import java.util.List;

public class CommandInfo {

    private final String shortDescription;
    private final String longDescription;
    private final List<String> exampleUses;

    public CommandInfo(String shortDescription, String longDescription, List<String> exampleUses) {
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.exampleUses = exampleUses;
    }
}
