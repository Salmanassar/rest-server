package com.packt.rest.router;

import com.packt.rest.todo.servlet.TodoHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RouterServlet extends HttpServlet {
    private static final Map<RouterDefinition, RouteHandler> routeMap = new HashMap<>();

    static {
        addRoute("GET /todos", TodoHandler::listTodo);
        addRoute("GET /todos/:id", TodoHandler::fetchTodo);
        addRoute("POST /todos", TodoHandler::createTodoWithoutId);
        addRoute("POST /todos/:id", TodoHandler::createTodoWithId);
        addRoute("PUT /todos/:id", TodoHandler::updateTodo);
        addRoute("DELETE /todos/:id", TodoHandler::deleteTodo);
        addRoute("GET /hello/:username", ((request, response) -> {
            String username = request.getRequestURI().substring("/hello/".length());
            response.setStatus(200);
            response.getWriter().println("Hello, " + username);
        }));
    }

    private static void addRoute(String route, RouteHandler routeHandler) {
        routeMap.put(new RouterDefinition(route), routeHandler);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        genericHandler(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        genericHandler(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        genericHandler(req, resp);
    }

    private void noMatchHandler(HttpServletResponse resp) throws IOException {
        resp.setStatus(401);
        resp.getWriter().println("See you later alligator!!");
    }

    private boolean isVerified(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader("Authorization");
        String actualToken = "secrete_staff";
        if (!actualToken.equals(token)) {
            response.getWriter().println("Sorry,authentication required");
            response.setStatus(401);
            return false;
        }
        return true;
    }

    private void genericHandler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isVerified(req, resp)) {
            for (Map.Entry<RouterDefinition, RouteHandler> route : routeMap.entrySet()) {
                if (route.getKey().matches(req)) {
                    route.getValue().execute(req, resp);
                    return;
                }
            }
            noMatchHandler(resp);
        }
    }
}
