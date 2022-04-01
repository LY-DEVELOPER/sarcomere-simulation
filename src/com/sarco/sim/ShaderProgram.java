package com.sarco.sim;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

public class ShaderProgram {
	
	CharSequence vertexShaderSource = "#version 330 core\n" +
		    "layout (location = 0) in vec3 aPos;\n" +
		    "void main()\n" +
		    "{\n" +
		    "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
		    "}\0";
	CharSequence fragmentShaderSource = "#version 330 core\n" +
		    "out vec4 FragColor;\n" +
		    "void main()\n" +
		    "{\n" +
		    "   FragColor = vec4(1.0f, 0.0f, 0.2f, 1.0f);\n" +
		    "}\n\0";
	
	int vertexShader, fragmentShader, shaderProgram;
	
	public void init() {
		//Set up shaders and shader program
		vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		
		fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentShaderSource);
		glCompileShader(fragmentShader);
		
		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glBindFragDataLocation(shaderProgram, 0, "fragColor");
		glLinkProgram(shaderProgram);
		
		//if shaders go wrong need to ask it for the error
		int statusV = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
		if (statusV != GL_TRUE) {
		    throw new RuntimeException(glGetShaderInfoLog(vertexShader));
		}
		
		int statusF = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
		if (statusF != GL_TRUE) {
		    throw new RuntimeException(glGetShaderInfoLog(fragmentShader));
		}
		
		int status = glGetProgrami(shaderProgram, GL_LINK_STATUS);
		if (status != GL_TRUE) {
		    throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
		}
		
		//now we have the shader program we can delete these
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader); 
	}
	
	public void cleanUp() {
		glDeleteProgram(shaderProgram);
	}
	
}
