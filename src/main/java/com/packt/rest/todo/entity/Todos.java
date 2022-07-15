package com.packt.rest.todo.entity;

import com.packt.rest.database.DB;
import org.dalesbred.query.SqlQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.dalesbred.query.SqlQuery.namedQuery;

public class Todos {

    public static List<Todo> list() {
        return DB.db.findAll(Todo.class, "SELECT * FROM todos");
    }

    public static Todo fetch(Long id) {
        return getOptionalTodo(id).orElse(null);
    }

    public static boolean exists(Long id) {
        return getOptionalTodo(id).isPresent();
    }

    private static Optional<Todo> getOptionalTodo(Long id) {
        SqlQuery query = namedQuery("SELECT * FROM todos WHERE id = :id", Map.of("id", id));
        return DB.db.findOptional(Todo.class, query);
    }

    public static Todo update(Todo todo) {
        if (todo.getId() == null) {
            SqlQuery query = namedQuery("INSERT INTO todos (text) VALUES (:text)", todo);
            Long id = DB.db.updateAndProcessGeneratedKeys(result -> {
                result.next();
                return result.getLong(1);
            }, List.of("id"), query);
            todo.setId(id);
        } else {
            DB.db.update(namedQuery("UPDATE todos SET text = :text", todo));
        }
        return todo;
    }

    public static Todo delete(Long id) {
        Todo todo = fetch(id);
        DB.db.update(namedQuery("DELETE FROM todos WHERE id = :id", Map.of("id", id)));
        return todo;
    }
}
