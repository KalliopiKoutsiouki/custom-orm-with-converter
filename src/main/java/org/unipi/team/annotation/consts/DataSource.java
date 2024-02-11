package org.unipi.team.annotation.consts;

public enum DataSource {
    H2 ("H2"),
    DERBY ("Derby"),
    MYSQL ("mySql"),
    MARIADB ("mariadb");

    private final String value;
    DataSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
