package com.packt.rest.router;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class RouterDefinition {
    private String verbs;
    private List<String> pathBits;
    public RouterDefinition(String route) {
        String[] parts = route.split(" ");
        verbs = parts[0];
        String rest = Arrays.stream(parts).skip(1L).collect(joining(" "));
        pathBits = splitURI(rest);
    }

    private List<String> splitURI(String rest) {
        return Arrays.stream(rest.split("/")).filter(e -> !e.isEmpty()).collect(toList());
    }

    public boolean matches(HttpServletRequest request) {
        return compareRequest(request.getMethod(), request.getRequestURI());
    }

    public boolean compareRequest(String method, String url) {
        if(!verbs.equalsIgnoreCase(method)){
        return false;
        }
        List<String> provided = splitURI(url);
        if(provided.size()!=pathBits.size()){
            return false;
        }
        for (int i = 0; i < provided.size(); i++) {
            String providedBit = provided.get(i);
            String patternBit = pathBits.get(i);
            if(patternBit.startsWith(":")){
                continue;
            }
            if(!patternBit.equalsIgnoreCase(providedBit)){
                return false;
            }
        }
        return true;
    }
}
