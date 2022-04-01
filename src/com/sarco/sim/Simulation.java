package com.sarco.sim;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.opengl.*;
import com.sarco.sim.utilities.Timer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;

public class Simulation implements Runnable {

	Window window;
	Timer timer;
	ShaderProgram shader;
	
	
	int VAO, VBO;

	@Override
	public void run() {
		init();
		simLoop();
		cleanUp();
	}

	public void init() {
		window = new Window();
		timer = new Timer();
		shader = new ShaderProgram();
		window.init();
		shader.init();
		glfwSetKeyCallback(window.getWindow(), keyCallback);
	}

	public void simLoop() {
		boolean running = true;
		float delta;
		while (running && !window.shouldClose()) {
			delta = timer.getDelta();

			update(delta);
			render();

			window.update();
		}
	}

	public void update(float delta) {
		// create our VAO
		VAO = glGenVertexArrays();
		VBO = glGenBuffers();
		glBindVertexArray(VAO);

		
		float[] vertices = new float[]{
			     0.0f,  0.5f, 0.0f,
			    -0.5f, -0.5f, 0.0f,
			     0.5f, -0.5f, 0.0f
			};
		
		FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
		verticesBuffer.put(vertices).flip();

		glBindBuffer(GL_ARRAY_BUFFER, VBO);
		glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
		
		if (verticesBuffer != null) {
		    MemoryUtil.memFree(verticesBuffer);
		}
		//How we want the open gl to interpret our vertext data
		int posAttrib = 0;
		int floatSize = 3;
		glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(posAttrib); 
		 // note that this is allowed, the call to glVertexAttribPointer registered VBO as the vertex attribute's bound vertex buffer object so afterwards we can safely unbind
	    glBindBuffer(GL_ARRAY_BUFFER, 0); 

	    // You can unbind the VAO afterwards so other VAO calls won't accidentally modify this VAO, but this rarely happens. Modifying other
	    // VAOs requires a call to glBindVertexArray anyways so we generally don't unbind VAOs (nor VBOs) when it's not directly necessary.
	    glBindVertexArray(0); 

	}

	public void render() {
		//clear the screen
		glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		// Add our VAO to the gpu
		glUseProgram(shader.shaderProgram);
		glBindVertexArray(VAO);
		//RENDER!!!
		glDrawArrays(GL_TRIANGLES, 0, 3);
	}

	public void cleanUp() {
		keyCallback.free();
		window.cleanUp();
		shader.cleanUp();
		glDeleteVertexArrays(VAO);
	    glDeleteBuffers(VBO);
	}

	private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
		}
	};

}
