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
import org.joml.Vector4f;

import com.sarco.sim.utilities.LoadShader;
import com.sarco.sim.utilities.MD5AnimModel;
import com.sarco.sim.utilities.MD5Loader;
import com.sarco.sim.utilities.MD5Model;
import com.sarco.sim.utilities.OBJLoader;
import com.sarco.sim.utilities.Timer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation implements Runnable {

	Window window;
	Timer timer;
	ShaderProgram shader;
	Camera camera;
	Transformations transformation;

	boolean contract = false;
	boolean autoPlay = false;
	boolean moveActin;
	boolean actinReturn = false;
	int speed = 16;

	private Map<Mesh, List<Object>> meshMap;

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
		meshMap = new HashMap<Mesh, List<Object>>();
		shader.createVertexShader(LoadShader.load("/assets/vertex.vs"));
		shader.createFragmentShader(LoadShader.load("/assets/fragment.fs"));
		shader.link();
		shader.createUniform("projectionMatrix");
		shader.createUniform("modelViewMatrix");
		shader.createUniform("texture_sampler");
		shader.createUniform("colour");
		shader.createUniform("useColour");
		shader.createUniform("jointsMatrix");
		glfwSetKeyCallback(window.getWindow(), keyCallback);
		glfwSetScrollCallback(window.getWindow(), scrollCallback);

		objects = new ArrayList<Object>();
		Mesh skyBox = OBJLoader.loadMesh("/assets/skybox.obj");
		skyBox.setColour(0.5f, 0.5f, 0.5f);
		skyBox.setTexture("skybox.png");
		Mesh grid = OBJLoader.loadMesh("/assets/grid.obj");
		grid.setTexture("grid.png");
		Mesh length = OBJLoader.loadMesh("/assets/length.obj");
		length.setColour(0, 0.7f, 0.7f);
		Mesh actin = OBJLoader.loadMesh("/assets/actin.obj");
		actin.setColour(0, 1f, 1f);
		MD5Model myosin = MD5Model.parse("/assets/myosin.md5mesh");
		MD5AnimModel animMyo = MD5AnimModel.parse("/assets/myosin.md5anim");
		objects.add(new Object(skyBox));
		objects.get(0).setScale(20f);
		objects.add(new Object(grid));
		objects.get(1).setPosition(0, -6, 0);
		objects.get(1).setScale(20);
		objects.add(new Object(length));
		objects.get(2).setPosition(0, 1, 0);
		objects.add(new Object(actin));
		objects.get(3).setPosition(5, 0.4f, 0);
		objects.add(new Object(actin));
		objects.get(4).setPosition(5, -0.4f, 0);
		objects.add(new Object(actin));
		objects.get(5).setRotation(0, 180, 0);
		objects.get(5).setPosition(-5, 0.4f, 0);
		objects.add(new Object(actin));
		objects.get(6).setRotation(0, 180, 0);
		objects.get(6).setPosition(-5, -0.4f, 0);
		camera.setRotation(10, 20, 0);
		camera.setPosition(0, 0, 3);
		camera.setScale(0.2f);
		for (int i = 0; i < 45; i++) {
			AnimObject myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(118.8f + i * 36, 0, 0);
			myosinObj.setPosition(0 - i * 0.143f, 0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			AnimObject myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(118.8f * 2 + i * 36, 0, 0);
			myosinObj.setPosition(0 - i * 0.143f, 0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			AnimObject myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(0 + i * 36, 0, 0);
			myosinObj.setPosition(0 - i * 0.143f, 0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			AnimObject myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(118.8f - i * 36, 180, 0);
			myosinObj.setPosition(0 + i * 0.143f, 0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			AnimObject myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(118.8f * 2 - i * 36, 180, 0);
			myosinObj.setPosition(0 + i * 0.143f, 0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 45; i++) {
			AnimObject myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(0 - i * 36, 180, 0);
			myosinObj.setPosition(0 + i * 0.143f, 0, 0);
			objects.add(myosinObj);
		}

		objects.forEach((object) -> {
			Mesh[] meshes = object.getMeshes();
			for (Mesh mesh : meshes) {
				List<Object> list = meshMap.get(mesh);
				if (list == null) {
					list = new ArrayList<>();
					meshMap.put(mesh, list);
				}
				list.add(object);
			}
		});
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
		moveActin = false;
		if(objects.get(3).getPosition().x <= 0.3 && autoPlay){ 
			contract = false;
		}
		if (objects.get(3).getPosition().x > 0.3 && !actinReturn && contract) {
			objects.forEach((object) -> {
				if (object instanceof AnimObject) {
					((AnimObject) object).nextFrame(speed);
					int frame = ((AnimObject) object).getCurrentFrameInt();
					if (frame >= 32 && frame <= 64) {
						moveActin();
					}
				}
			});

			if (moveActin) {
				float amount = (float) (0.13 / 32) * speed;
				objects.get(3).movePosition(-amount, 0, 0);
				objects.get(4).movePosition(-amount, 0, 0);
				objects.get(5).movePosition(-amount, 0, 0);
				objects.get(6).movePosition(-amount, 0, 0);
			}
		}else if (!actinReturn && !contract) {
			objects.forEach((object) -> {
				if (object instanceof AnimObject) {
					((AnimObject) object).nextFrame(speed);
					int frame = ((AnimObject) object).getCurrentFrameInt();
					if (frame <= 16 || frame >= 80) {
						actinReturn = true;
					}
				}
			});
		} else if (actinReturn || !contract) {
			if (objects.get(3).getPosition().x < 5) {
				float amount = (float) (0.13 / 32) * speed;
				objects.get(3).movePosition(amount, 0, 0);
				objects.get(4).movePosition(amount, 0, 0);
				objects.get(5).movePosition(amount, 0, 0);
				objects.get(6).movePosition(amount, 0, 0);
			}else if(autoPlay) {
				contract = true;
				actinReturn = false;
			}
		}
	}

	public void moveActin() {
		moveActin = true;
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

		for (Mesh mesh : meshMap.keySet()) {

			mesh.renderList(meshMap.get(mesh), (Object object) -> {

				Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(object, viewMatrix);
				shader.setUniform("modelViewMatrix", modelViewMatrix);
				shader.setUniform("colour", object.getMesh().getColour());
				shader.setUniform("useColour", object.getMesh().isTextured() ? 0 : 1);

				if (object instanceof AnimObject) {
					AnimObject animObject = (AnimObject) object;
					AnimatedFrame frame = animObject.getCurrentFrame();
					shader.setUniform("jointsMatrix", frame.getJointMatrices());
				}
			});
		}
		shader.unbind();
	}

	public void cleanUp() {
		keyCallback.free();
		window.cleanUp();
		shader.cleanUp();
		objects.forEach((object) -> {
			for (Mesh mesh : object.getMeshes()) {
				mesh.cleanUp();
			}
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
				camera.moveRotation(-2.5f, 0, 0);
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
				camera.setScale(0.2f);
			}
			if (key == GLFW_KEY_SPACE && !autoPlay && (action == GLFW_PRESS || action == GLFW_RELEASE)) {
				if (!autoPlay) {
					contract = !contract;
					if (actinReturn) {
						actinReturn = false;
					}
				}
			}
			if (key == GLFW_KEY_A && action == GLFW_PRESS) {
				autoPlay = !autoPlay;
				contract = true;	
				if(actinReturn) {
					actinReturn = false;
				}
				if (!autoPlay) {
					actinReturn = true;
					contract = false;
				}
			}
			if (key == GLFW_KEY_V && action == GLFW_PRESS) {
				if (speed > 1) {
					speed--;
				}
			}
			if (key == GLFW_KEY_B && action == GLFW_PRESS) {
				if (speed < 64) {
					speed++;
				}
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
