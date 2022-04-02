package com.sarco.sim.utilities;

import java.io.InputStream;
import java.util.Scanner;

public class LoadShader {
	public static String load(String file) throws Exception {
		String shader;
        try (InputStream in = LoadShader.class.getResourceAsStream(file);
             Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            shader = scanner.useDelimiter("\\A").next();
        }
        return shader;
	}
}
