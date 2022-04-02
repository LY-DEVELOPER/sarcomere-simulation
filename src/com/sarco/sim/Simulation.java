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

import com.sarco.sim.utilities.LoadShader;
import com.sarco.sim.utilities.Timer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;

public class Simulation implements Runnable {

	Window window;
	Timer timer;
	ShaderProgram shader;
	Mesh mesh;

	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	private Matrix4f projectionMatrix;

	@Override
	public void run() {
		try {
			init();
			simLoop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cleanUp();
	}

	public void init() throws Exception {
		window = new Window();
		timer = new Timer();
		window.init();
		shader = new ShaderProgram();
        shader.createVertexShader(LoadShader.load("/assets/vertex.vs"));
        shader.createFragmentShader(LoadShader.load("/assets/fragment.fs"));
        shader.link();
		float aspectRatio = (float) window.getWidth() / window.getHeight();
		System.out.println(aspectRatio);
		projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
		shader.createUniform("projectionMatrix");
		glfwSetKeyCallback(window.getWindow(), keyCallback);
	}

	public void simLoop() {
		boolean running = true;
		float delta;
		float[] positions = new float[]{
	            -0.5f,  0.5f, -2f,
	            -0.5f, -0.5f, -1.05f,
	             0.5f, -0.5f, -1.05f,
	             0.5f,  0.5f, -1.05f,
	        };
	        float[] colours = new float[]{
	            0.5f, 0.0f, 0.0f,
	            0.0f, 0.5f, 0.0f,
	            0.0f, 0.0f, 0.5f,
	            0.0f, 0.5f, 0.5f,
	        };
	        int[] indices = new int[]{
	            0, 1, 3, 3, 1, 2,
	        };
		mesh = new Mesh(positions, indices, colours);
		while (running && !window.shouldClose()) {
			delta = timer.getDelta();
			update(delta);
			render(mesh);
			window.update();
		}
	}

	public void update(float delta) {

	}

	public void render(Mesh mesh) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		// Add our VAO to the gpu
		shader.bind();
		float aspectRatio = (float) window.getWidth() / window.getHeight();
		projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
		shader.setUniform("projectionMatrix", projectionMatrix);
		glBindVertexArray(mesh.getVAO());
		// RENDER!!!
		glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glBindVertexArray(0);
		shader.unbind();
	}

	public void cleanUp() {
		keyCallback.free();
		window.cleanUp();
		shader.cleanUp();
		mesh.cleanUp();
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
