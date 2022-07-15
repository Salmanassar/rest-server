package com.packt.rest.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.packt.rest.database.DB;
import org.dalesbred.query.SqlQuery;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.joining;

public class Util {

	public static String readInputStream(InputStream stream) {
		return new BufferedReader(new InputStreamReader(stream)).lines().collect(joining("\n"));
	}
}

