package com.packt.rest.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.packt.rest.util.Util;
import com.packt.rest.todo.entity.Todo;
import com.packt.rest.todo.entity.Todos;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TodoServlet extends HttpServlet {

	private static final Gson GSON = new GsonBuilder().create();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String uri = req.getRequestURI();
		Long id = Long.parseLong(uri.substring("/todos/".length()));

		String json = GSON.toJson(Todos.todos.get(id));

		response(resp, 200, json);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String uri = req.getRequestURI();
		Long id = Long.parseLong(uri.substring("/todos/".length()));

		if (Todos.todos.containsKey(id)) {
			resp.setStatus(422);
			resp.getOutputStream().println("You cannot created Todo with id " + id + " because it exists!");
		}

		String json = Util.readInputStream(req.getInputStream());
		Todo todo = GSON.fromJson(json, Todo.class);
		todo.setId(id);

		Todos.todos.put(todo.getId(), todo);

		response(resp, 201, GSON.toJson(todo));
	}

	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String uri = req.getRequestURI();
		Long id = Long.parseLong(uri.substring("/todos/".length()));

		if (!Todos.todos.containsKey(id)) {
			resp.setStatus(422);
			resp.getOutputStream().println("You cannot update Todo with id " + id + " because it doesn't exists!");
		}

		String json = Util.readInputStream(req.getInputStream());
		Todo todo = GSON.fromJson(json, Todo.class);
		todo.setId(id);

		Todos.todos.put(todo.getId(), todo);

		response(resp, 200, GSON.toJson(todo));
	}

	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String uri = req.getRequestURI();
		Long id = Long.parseLong(uri.substring("/todos/".length()));

		Todo todo = Todos.todos.remove(id);
		String json = GSON.toJson(todo);

		response(resp, 200, json);
	}

	private void response(HttpServletResponse resp, int i, String json) throws IOException {
		resp.setStatus(i);
		resp.setHeader("Content-Type", "application/json");
		resp.getOutputStream().println(json);
	}
}
