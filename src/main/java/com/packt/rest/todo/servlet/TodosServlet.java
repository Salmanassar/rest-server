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

public class TodosServlet extends HttpServlet {

	private static final Gson GSON = new GsonBuilder().create();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String json = GSON.toJson(Todos.todos.values());

		response(resp, 200, json);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String json = Util.readInputStream(req.getInputStream());
		Todo todo = GSON.fromJson(json, Todo.class);

		todo.setId(Todos.nextId());
		Todos.todos.put(todo.getId(), todo);

		response(resp, 201, GSON.toJson(todo));
	}

	private void response(HttpServletResponse resp, int i, String GSON) throws IOException {
		resp.setStatus(i);
		resp.setHeader("Content-Type", "application/json");
		resp.getOutputStream().println(GSON);
	}
}
