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
	ShaderProgram hudShader;
	Camera camera;
	Transformations transformation;

	boolean contract = false;
	boolean autoPlay = false;
	boolean moveActin;
	boolean actinReturn = false;
	int speed = 8;

	private Map<Mesh, List<Object>> meshMap;
	private Map<Mesh, List<TextObject>> textMeshMap;

	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	float i = 0.1f;

	ArrayList<Object> objects;
	ArrayList<TextObject> textObjects;

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
		glfwSetKeyCallback(window.getWindow(), keyCallback);
		glfwSetScrollCallback(window.getWindow(), scrollCallback);
		shader.createVertexShader(LoadShader.load("/assets/vertex.vs"));
		shader.createFragmentShader(LoadShader.load("/assets/fragment.fs"));
		shader.link();
		shader.createUniform("projectionMatrix");
		shader.createUniform("modelViewMatrix");
		shader.createUniform("texture_sampler");
		shader.createUniform("colour");
		shader.createUniform("useColour");
		shader.createUniform("jointsMatrix");
		hudShader = new ShaderProgram();
		hudShader.createVertexShader(LoadShader.load("/assets/hud_vertex.vs"));
		hudShader.createFragmentShader(LoadShader.load("/assets/hud_fragment.fs"));
		hudShader.link();
		hudShader.createUniform("projModelMatrix");
		hudShader.createUniform("colour");
		camera.setRotation(10, 20, 0);
		camera.setPosition(0, 0, 3);
		camera.setScale(0.2f);

		objects = CreateSceneObjects.gen();

		textObjects = new ArrayList<TextObject>();

		textObjects.add(new TextObject("AutoPlay:Off", "src/assets/font.png", 16, 16));
		textObjects.get(0).setPosition(5f, 5f, 1);
		textObjects.get(0).setScale(0.3f);
		textObjects.add(new TextObject("Contracting:Off", "src/assets/font.png", 16, 16));
		textObjects.get(1).setPosition(260f, 5f, 1);
		textObjects.get(1).setScale(0.3f);
		textObjects.add(new TextObject("Speed:" + speed + "/64", "src/assets/font.png", 16, 16));
		textObjects.get(2).setPosition(570f, 5f, 1);
		textObjects.get(2).setScale(0.3f);
		
		textObjects.add(new TextObject("Controls:", "src/assets/font.png", 16, 16));
		textObjects.get(3).setPosition(5f, window.getHeight() - 215, 1);
		textObjects.get(3).setScale(0.3f);
		textObjects.add(new TextObject("Arrow Keys: Rotate Camera", "src/assets/font.png", 16, 16));
		textObjects.get(4).setPosition(5f, window.getHeight() - 185, 1);
		textObjects.get(4).setScale(0.3f);
		textObjects.add(new TextObject("Scroll: Zoom", "src/assets/font.png", 16, 16));
		textObjects.get(5).setPosition(5f, window.getHeight() - 155, 1);
		textObjects.get(5).setScale(0.3f);
		textObjects.add(new TextObject("C: Center Camera", "src/assets/font.png", 16, 16));
		textObjects.get(6).setPosition(5f, window.getHeight() - 125, 1);
		textObjects.get(6).setScale(0.3f);
		textObjects.add(new TextObject("V/B: Speed Down/Up", "src/assets/font.png", 16, 16));
		textObjects.get(7).setPosition(5f, window.getHeight() - 95, 1);
		textObjects.get(7).setScale(0.3f);
		textObjects.add(new TextObject("A: Toggle AutoPlay", "src/assets/font.png", 16, 16));
		textObjects.get(8).setPosition(5f, window.getHeight() - 65, 1);
		textObjects.get(8).setScale(0.3f);
		textObjects.add(new TextObject("Space Bar: Contract", "src/assets/font.png", 16, 16));
		textObjects.get(9).setPosition(5f, window.getHeight() - 35, 1);
		textObjects.get(9).setScale(0.3f);

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
		if (objects.get(3).getPosition().x <= 0.3 && autoPlay) {
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
		} else if (!actinReturn && !contract) {
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
			} else if (autoPlay) {
				contract = true;
				actinReturn = false;
			}
		}
		if (autoPlay) {
			textObjects.get(0).setText("AutoPlay:On");
		} else {
			textObjects.get(0).setText("AutoPlay:Off");
		}

		if (contract) {
			textObjects.get(1).setText("Contracting:On");
		} else {
			textObjects.get(1).setText("Contracting:Off");
		}

		textObjects.get(2).setText("Speed:" + speed + "/64");
		
		textObjects.get(3).setPosition(5f, window.getHeight() - 220, 1);
		textObjects.get(4).setPosition(5f, window.getHeight() - 190, 1);
		textObjects.get(5).setPosition(5f, window.getHeight() - 160, 1);
		textObjects.get(6).setPosition(5f, window.getHeight() - 130, 1);
		textObjects.get(7).setPosition(5f, window.getHeight() - 100, 1);
		textObjects.get(8).setPosition(5f, window.getHeight() - 70, 1);
		textObjects.get(9).setPosition(5f, window.getHeight() - 40, 1);
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

		for (Object object : objects) {

			Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(object, viewMatrix);
			shader.setUniform("modelViewMatrix", modelViewMatrix);
			shader.setUniform("colour", object.getMesh().getColour());
			shader.setUniform("useColour", object.getMesh().isTextured() ? 0 : 1);

			if (object instanceof AnimObject) {
				AnimObject animObject = (AnimObject) object;
				AnimatedFrame frame = animObject.getCurrentFrame();
				shader.setUniform("jointsMatrix", frame.getJointMatrices());
			}
			
			for(Mesh mesh : object.getMeshes()) {
				mesh.render();
			}
		}
		shader.unbind();

		hudShader.bind();

		Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
		for (Object object : textObjects) {
			Mesh mesh = object.getMesh();
			// Set ortohtaphic and model matrix for this HUD item
			Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(object, ortho);
			hudShader.setUniform("projModelMatrix", projModelMatrix);
			hudShader.setUniform("colour", object.getMesh().getColour());

			// Render the mesh for this HUD item
			mesh.render();
		}

		hudShader.unbind();
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
		textObjects.forEach((object) -> {
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
				if (actinReturn) {
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
