package com.packt.rest.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.packt.rest.todo.entity.Todo;
import com.packt.rest.todo.entity.Todos;
import com.packt.rest.util.Util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TodoHandler {
    private static final Gson GSON = new GsonBuilder().create();

    public static void listTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = GSON.toJson(Todos.todos.values());
        response(resp, 200, json);
    }

    public static void fetchTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = Long.parseLong(uri.substring("/todos/".length()));

        String json = GSON.toJson(Todos.todos.get(id));

        response(resp, 200, json);
    }

    private static void response(HttpServletResponse resp, int i, String json) throws IOException {
        resp.setStatus(i);
        resp.setHeader("Content-Type", "application/json");
        resp.getOutputStream().println(json);
    }


    public static void createTodoWithId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = getaLongId(req);

        todosContainsKey(Todos.todos.containsKey(id), resp, "You cannot created Todo with id ", id, " because it exists!");

        String json = Util.readInputStream(req.getInputStream());
        Todo todo = GSON.fromJson(json, Todo.class);
        todo.setId(id);

        Todos.todos.put(todo.getId(), todo);

        response(resp, 201, GSON.toJson(todo));
    }

    private static void todosContainsKey(boolean todos, HttpServletResponse resp, String x, Long id, String x1) throws IOException {
        if (todos) {
            resp.setStatus(422);
            resp.getOutputStream().println(x + id + x1);
        }
    }

    private static Long getaLongId(HttpServletRequest req) {
        String uri = req.getRequestURI();
        Long id = Long.parseLong(uri.substring("/todos/".length()));
        return id;
    }

    public static void createTodoWithoutId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = Util.readInputStream(req.getInputStream());
        Todo todo = GSON.fromJson(json, Todo.class);

        todo.setId(Todos.nextId());
        Todos.todos.put(todo.getId(), todo);

        response(resp, 201, GSON.toJson(todo));
    }

    public static void updateTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = Long.parseLong(uri.substring("/todos/".length()));

        todosContainsKey(!Todos.todos.containsKey(id), resp, "You cannot update Todo with id ", id, " because it doesn't exists!");

        String json = Util.readInputStream(req.getInputStream());
        Todo todo = GSON.fromJson(json, Todo.class);
        todo.setId(id);

        Todos.todos.put(todo.getId(), todo);

        response(resp, 200, GSON.toJson(todo));
    }

    public static void deleteTodo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String uri = req.getRequestURI();
        Long id = Long.parseLong(uri.substring("/todos/".length()));

        Todo todo = Todos.todos.remove(id);
        String json = GSON.toJson(todo);

        response(resp, 200, json);
    }
}
