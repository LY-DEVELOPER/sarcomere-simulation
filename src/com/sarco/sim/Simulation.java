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
import java.util.ArrayList;

public class Simulation implements Runnable {

	Window window;
	Timer timer;
	ShaderProgram shader;
	Transformations transformation;

	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	float i = 0.1f;

	ArrayList<Object> objects;

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
		transformation = new Transformations();
		window.init();
		shader = new ShaderProgram();
		shader.createVertexShader(LoadShader.load("/assets/vertex.vs"));
		shader.createFragmentShader(LoadShader.load("/assets/fragment.fs"));
		shader.link();
		shader.createUniform("projectionMatrix");
		shader.createUniform("worldMatrix");
		glfwSetKeyCallback(window.getWindow(), keyCallback);

		objects = new ArrayList<Object>();
	}

	public void simLoop() {
		boolean running = true;
		float delta;
		float[] positions = new float[] {
			    // VO
			    -0.5f,  0.5f,  0.5f,
			    // V1
			    -0.5f, -0.5f,  0.5f,
			    // V2
			    0.5f, -0.5f,  0.5f,
			    // V3
			     0.5f,  0.5f,  0.5f,
			    // V4
			    -0.5f,  0.5f, -0.5f,
			    // V5
			     0.5f,  0.5f, -0.5f,
			    // V6
			    -0.5f, -0.5f, -0.5f,
			    // V7
			     0.5f, -0.5f, -0.5f,
			};
		float[] colours = new float[]{
			    0.5f, 0.0f, 0.0f,
			    0.0f, 0.5f, 0.0f,
			    0.0f, 0.0f, 0.5f,
			    0.0f, 0.5f, 0.5f,
			    0.5f, 0.0f, 0.0f,
			    0.0f, 0.5f, 0.0f,
			    0.0f, 0.0f, 0.5f,
			    0.0f, 0.5f, 0.5f,
			};
		int[] indices = new int[] {
			    // Front face
			    0, 1, 3, 3, 1, 2,
			    // Top Face
			    4, 0, 3, 5, 4, 3,
			    // Right face
			    3, 2, 7, 5, 3, 7,
			    // Left face
			    6, 1, 0, 6, 0, 4,
			    // Bottom face
			    2, 1, 6, 2, 6, 7,
			    // Back face
			    7, 6, 4, 7, 4, 5,
			};
		Mesh mesh = new Mesh(positions, indices, colours);
		objects.add(new Object(mesh));
		objects.get(0).setPosition(0, 0, -2);
		while (running && !window.shouldClose()) {
			delta = timer.getDelta();
			update(delta);
			render();
			window.update();
		}
	}

	public void update(float delta) {
		i+= 1f;
		
		for(Object object: objects) {
			object.setRotation(i, 0, -i);
		}
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		shader.bind();
		// Update projection Matrix
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(),
				Z_NEAR, Z_FAR);
		shader.setUniform("projectionMatrix", projectionMatrix);

		// Render each gameItem
		objects.forEach((object) -> {
			Matrix4f worldMatrix = transformation.getWorldMatrix(object.getPosition(), object.getRotation(),
					object.getScale());
			shader.setUniform("worldMatrix", worldMatrix);
			// Render the mesh for this game item
			object.getMesh().render();
		});
		
		shader.unbind();
	}

	public void cleanUp() {
		keyCallback.free();
		window.cleanUp();
		shader.cleanUp();
		objects.forEach((object) -> {
			object.getMesh().cleanUp();
		});
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
