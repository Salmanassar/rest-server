package com.packt.rest.router;

import com.google.gson.GsonBuilder;
import com.packt.rest.auth.Token;
import com.packt.rest.database.DB;
import com.packt.rest.todo.entity.Todo;
import com.packt.rest.todo.servlet.TodoHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RouterServlet extends HttpServlet {

    private static final Map<RouteDefinition, RouteHandler> routes = new HashMap<>();

    private static void addRoute(String route, RouteHandler handler) {
        routes.put(new RouteDefinition(route), handler);
    }

    static {
        addRoute("GET /todos", TodoHandler::listTodos);
        addRoute("GET /todos/:id", TodoHandler::fetchTodo);
        addRoute("POST /todos", TodoHandler::createTodoWithoutId);
        addRoute("POST /todos/:id", TodoHandler::createTodoWithId);
        addRoute("PUT /todos/:id", TodoHandler::updateTodo);
        addRoute("DELETE /todos/:id", TodoHandler::deleteTodo);

        addRoute("GET /hello/:username", (req, resp) -> {
            String username = req.getRequestURI().substring("/hello/".length());
            resp.setStatus(200);
            resp.getWriter().println("Hello, " + username);
        });
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        genericHandler(req, resp);
    }

    private void genericHandler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Headers", "Authorization");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        resp.addHeader("Access-Control-Allow-Credentials", "false");

        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
            resp.setStatus(200);
            resp.getWriter().println("Allowing OPTIONS");
            return;
        }

        String token = req.getHeader("Authorization");

        String actualToken = DB.db.findOptional(Token.class, "SELECT * FROM Tokens")
                .map(Token::getValue)
                .orElse("Need_more_secrete_staff");

        boolean isVerified = actualToken.equals(token);

        if (!isVerified) {
            resp.getWriter().println("Sorry, authentication required.");
            resp.setStatus(401); // 401, 403
            return;
        }

        for (Map.Entry<RouteDefinition, RouteHandler> route : routes.entrySet()) {
            if (route.getKey().matches(req)) {
                route.getValue().execute(req, resp);
                return;
            }
        }
        noMatchHandler(resp);
    }

    private void noMatchHandler(HttpServletResponse resp) throws IOException {
        resp.setStatus(404);
        resp.getWriter().println("Route not found");
    }
}
