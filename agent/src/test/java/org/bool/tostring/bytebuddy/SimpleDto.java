package org.bool.tostring.bytebuddy;

public class SimpleDto {

    private final String name;

    private final int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public SimpleDto(String name, int value) {
        this.name = name;
        this.value = value;
    }
}