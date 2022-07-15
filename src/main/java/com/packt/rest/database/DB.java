package com.packt.rest.database;

import org.dalesbred.Database;

public class DB {
    public static final Database db;

    static {
        db = Database.forUrlAndCredentials("jdbc:postgresql://localhost:5433/postgres", "postgres", "todo");
    }
}
