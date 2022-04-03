package com.sarco.sim;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.opengl.*;

import com.sarco.sim.utilities.LoadShader;
import com.sarco.sim.utilities.OBJLoader;
import com.sarco.sim.utilities.Timer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Simulation implements Runnable {

	Window window;
	Timer timer;
	ShaderProgram shader;
	Camera camera;
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
		camera = new Camera();
		shader.createVertexShader(LoadShader.load("/assets/vertex.vs"));
		shader.createFragmentShader(LoadShader.load("/assets/fragment.fs"));
		shader.link();
		shader.createUniform("projectionMatrix");
        shader.createUniform("modelViewMatrix");
        shader.createUniform("texture_sampler");
        shader.createUniform("colour");
        shader.createUniform("useColour");
		glfwSetKeyCallback(window.getWindow(), keyCallback);
		glfwSetScrollCallback(window.getWindow(), scrollCallback);

		objects = new ArrayList<Object>();
		Mesh skyBox = OBJLoader.loadMesh("/assets/skybox.obj");
		skyBox.setTexture("assets/skybox.png");
		Mesh length = OBJLoader.loadMesh("/assets/length.obj");
		Mesh myosin = OBJLoader.loadMesh("/assets/myosin.obj");
		myosin.setTexture("assets/myosin.png");
		objects.add(new Object(skyBox));
		objects.get(0).setScale(20f);
		objects.add(new Object(length));
		objects.get(1).setPosition(0, 1, 0);
		camera.setPosition(0, 0, 3);
		camera.setScale(0.28f);
		for (int i = 0; i < 45; i++) {
			Object myosinObj = new Object(myosin);
			myosinObj.setRotation(118.8f + i * 36, 0, 0);
			myosinObj.setPosition(0 - i * 0.143f,  0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			Object myosinObj = new Object(myosin);
			myosinObj.setRotation(118.8f*2 + i * 36, 0, 0);
			myosinObj.setPosition(0 - i * 0.143f,  0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			Object myosinObj = new Object(myosin);
			myosinObj.setRotation(0 + i * 36, 0, 0);
			myosinObj.setPosition(0 - i * 0.143f,  0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			Object myosinObj = new Object(myosin);
			myosinObj.setRotation(118.8f - i * 36,  180, 0);
			myosinObj.setPosition(0 + i * 0.143f,  0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			Object myosinObj = new Object(myosin);
			myosinObj.setRotation(118.8f*2 - i * 36,  180, 0);
			myosinObj.setPosition(0 + i * 0.143f,  0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			Object myosinObj = new Object(myosin);
			myosinObj.setRotation(0 - i * 36,  180, 0);
			myosinObj.setPosition(0 + i * 0.143f,  0, 0);
			objects.add(myosinObj);
		}
	}

	public void simLoop() throws Exception {
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

		shader.setUniform("texture_sampler", 0);

		Matrix4f viewMatrix = transformation.getViewMatrix(camera);

		// Render each gameItem
		objects.forEach((object) -> {
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(object, viewMatrix);
			shader.setUniform("modelViewMatrix", modelViewMatrix);
			shader.setUniform("colour", object.getMesh().getColour());
		    shader.setUniform("useColour", object.getMesh().isTextured() ? 0 : 1);
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
			if (key == GLFW_KEY_UP && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				camera.moveRotation(2.5f, 0, 0);
			}
			if (key == GLFW_KEY_DOWN && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				camera.moveRotation(-2.5f, 0 ,0);
			}
			if (key == GLFW_KEY_RIGHT && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				camera.moveRotation(0, -2.5f, 0);
			}
			if (key == GLFW_KEY_LEFT && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				camera.moveRotation(0, 2.5f, 0);
			}
			if (key == GLFW_KEY_C && action == GLFW_PRESS) {
				camera.setRotation(0, 0, 0);
				camera.setPosition(0, 0, 3);
				camera.setScale(0.28f);
			}
		}
	};

	private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {

		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			// TODO Auto-generated method stub
			camera.camZoom((float) yoffset / 10);
		}
	};

}
