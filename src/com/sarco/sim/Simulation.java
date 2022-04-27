package com.sarco.sim;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
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
import java.util.concurrent.TimeUnit;

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
	boolean vsync = true;
	int fps = 100;
	int liveFPS;

	private Map<Mesh, List<Object>> meshMap;
	private Map<Mesh, List<TextObject>> textMeshMap;

	private static final float FOV = (float) Math.toRadians(60.0f);

	private static final float Z_NEAR = 0.01f;

	private static final float Z_FAR = 1000.f;

	float i = 0.1f;

	double mouseX;
	double mouseY;
	boolean mouseHold;
	double lastX;
	double lastY;

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
		window.init(vsync);
		shader = new ShaderProgram();
		camera = new Camera();
		meshMap = new HashMap<Mesh, List<Object>>();
		glfwSetKeyCallback(window.getWindow(), keyCallback);
		glfwSetScrollCallback(window.getWindow(), scrollCallback);
		glfwSetCursorPosCallback(window.getWindow(), cursorCallback);
		glfwSetMouseButtonCallback(window.getWindow(), mouseCallback);
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

		String font = "./textures/font.png";

		textObjects.add(new TextObject("AutoPlay:Off", font, 16, 16));
		textObjects.get(0).setPosition(10f, 5f, 1);
		textObjects.add(new TextObject("FPS:" + liveFPS, font, 16, 16));
		textObjects.get(1).setPosition(window.getWidth() - 190f, window.getHeight() - 40f, 1);
		textObjects.add(new TextObject("Contracting:Off", font, 16, 16));
		textObjects.get(2).setPosition(window.getWidth() - 300f, 5f, 1);
		textObjects.add(new TextObject("Appearance \21", font, 16, 16));
		textObjects.get(3).setPosition(10f, 35 + 5, 1);
		textObjects.add(new TextObject("View \21", font, 16, 16));
		textObjects.get(4).setPosition(10f, 35 * 2 + 5, 1);
		textObjects.add(new TextObject("Performance \21", font, 16, 16));
		textObjects.get(5).setPosition(10f, 35 * 3 + 5, 1);
		textObjects.add(new TextObject("Controls \21", font, 16, 16));
		textObjects.get(6).setPosition(10f, 35 * 4 + 5, 1);
		textObjects.add(new TextObject("Myosin Colour", font, 16, 16));
		textObjects.get(7).setPosition(20f, 35 * 2 + 5, 1);
		textObjects.add(new TextObject("Myosin Transparency", font, 16, 16));
		textObjects.get(8).setPosition(20f, 35 * 3 + 5, 1);
		textObjects.add(new TextObject("Actin Colour", font, 16, 16));
		textObjects.get(9).setPosition(20f, 35 * 4 + 5, 1);
		textObjects.add(new TextObject("Actin Transparency", font, 16, 16));
		textObjects.get(10).setPosition(20f, 35 * 5 + 5, 1);
		textObjects.add(new TextObject("Re-Centre Camera", font, 16, 16));
		textObjects.get(11).setPosition(20f, 35 * 3 + 5, 1);
		textObjects.add(new TextObject("Speed:" + speed + "/64", font, 16, 16));
		textObjects.get(12).setPosition(20f, 35 * 4 + 5, 1);
		textObjects.add(new TextObject("Quality: Low", font, 16, 16));
		textObjects.get(13).setPosition(20f, 35 * 4 + 5, 1);
		textObjects.add(new TextObject("V-sync: " + vsync, font, 16, 16));
		textObjects.get(14).setPosition(20f, 35 * 5 + 5, 1);
		textObjects.add(new TextObject("FPS: " + fps, font, 16, 16));
		textObjects.get(15).setPosition(20f, 35 * 6 + 5, 1);
		textObjects.add(new TextObject("Arrow Keys: Move Camera", font, 16, 16));
		textObjects.get(16).setPosition(20f, 35 * 5 + 5, 1);
		textObjects.add(new TextObject("Click + Drag: Rotate Camera", font, 16, 16));
		textObjects.get(17).setPosition(20f, 35 * 6 + 5, 1);
		textObjects.add(new TextObject("Scroll: Zoom", font, 16, 16));
		textObjects.get(18).setPosition(20f, 35 * 7 + 5, 1);
		textObjects.add(new TextObject("C: Center Camera", font, 16, 16));
		textObjects.get(19).setPosition(20f, 35 * 8 + 5, 1);
		textObjects.add(new TextObject("V/B: Speed Down/Up", font, 16, 16));
		textObjects.get(20).setPosition(20f, 35 * 9 + 5, 1);
		textObjects.add(new TextObject("A: Toggle AutoPlay", font, 16, 16));
		textObjects.get(21).setPosition(20f, 35 * 10 + 5, 1);
		textObjects.add(new TextObject("Space Bar: Contract", font, 16, 16));
		textObjects.get(22).setPosition(20f, 35 * 11 + 5, 1);
		for (TextObject obj : textObjects) {
			obj.setScale(0.3f);
		}
		for (int i = 7; i < textObjects.size(); i++) {
			textObjects.get(i).toggleVis();
		}
		glViewport(0, 0, window.getWidth(), window.getHeight());
	}

	public void simLoop() throws Exception {
		boolean running = true;
		float delta;
		ArrayList<Integer> averageFPS = new ArrayList<Integer>();
		while (running && !window.shouldClose()) {
			delta = timer.getDelta();
			averageFPS.add(Math.round(1 / delta));
			if(averageFPS.size() == fps / 2) {
				int total = 0;
				for(Integer i : averageFPS) {
					total += i;
				}
				liveFPS = total / fps * 2;
				averageFPS = new ArrayList<Integer>();
			}
			update(delta);
			render();
			window.update(vsync);
			if (!vsync) {
				Thread.sleep(Math.round(Math.ceil((float) 1 / (fps + (0.5 * fps)) * 1000)));
			}
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
			textObjects.get(1).setText("FPS: " + liveFPS);
			textObjects.get(14).setText("vsync: " + vsync);
		if (contract) {
			textObjects.get(2).setText("Contracting:On");
		} else {
			textObjects.get(2).setText("Contracting:Off");
		}

		textObjects.get(12).setText("Speed:" + speed + "/64");
	}

	public void moveActin() {
		moveActin = true;
	}

	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
		if (window.isResized()) {
			textObjects.get(1).setPosition(window.getWidth() - 190f, window.getHeight() - 40f, 1);
			textObjects.get(2).setPosition(window.getWidth() - 300f, 5f, 1);
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

			for (Mesh mesh : object.getMeshes()) {
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
			if (key == GLFW_KEY_RIGHT && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				camera.movePosition(0.1f, 0, 0);
			}
			if (key == GLFW_KEY_LEFT && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				camera.movePosition(-0.1f, 0, 0);
			}
			if (key == GLFW_KEY_UP && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				camera.movePosition(0, 0.1f, 0);
			}
			if (key == GLFW_KEY_DOWN && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				camera.movePosition(0, -0.1f, 0);
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
			if (key == GLFW_KEY_V && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				if (speed > 1) {
					speed--;
				}
			}
			if (key == GLFW_KEY_B && (action == GLFW_REPEAT || action == GLFW_PRESS)) {
				if (speed < 64) {
					speed++;
				}
			}
		}
	};

	private GLFWMouseButtonCallback mouseCallback = new GLFWMouseButtonCallback() {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			// TODO Auto-generated method stub
			if (button == GLFW_MOUSE_BUTTON_LEFT && GLFW_PRESS == action) {
				mouseHold = true;
				lastX = mouseX;
				lastY = mouseY;
			}
			if (button == GLFW_MOUSE_BUTTON_LEFT && GLFW_RELEASE == action) {
				mouseHold = false;
				lastX = mouseX;
				lastY = mouseY;
			}
			if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
				for (TextObject obj : textObjects) {
					if (obj.isMouseOver(mouseX, mouseY)) {
						mouseHold = false;
						if (obj.getText().equals("AutoPlay:Off") || obj.getText().equals("AutoPlay:On")) {
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
						if (obj.getText().equals("vsync: false") || obj.getText().equals("vsync: true")) {
							vsync = !vsync;
						}
						if (obj.getText().equals("Appearance \21")) {
							for (int i = 7; i < 11; i++) {
								textObjects.get(i).toggleVis();
							}
							textObjects.get(4).moveDown(4);
							textObjects.get(5).moveDown(4);
							textObjects.get(6).moveDown(4);
							for (int i = 11; i < textObjects.size(); i++) {
								textObjects.get(i).moveDown(4);
							}
							obj.setText("Appearance \20");
						} else if (obj.getText().equals("Appearance \20")) {
							for (int i = 7; i < 11; i++) {
								textObjects.get(i).toggleVis();
							}
							textObjects.get(4).moveDown(-4);
							textObjects.get(5).moveDown(-4);
							textObjects.get(6).moveDown(-4);
							for (int i = 11; i < textObjects.size(); i++) {
								textObjects.get(i).moveDown(-4);
							}
							obj.setText("Appearance \21");
						}
						if (obj.getText().equals("View \21")) {
							for (int i = 11; i < 13; i++) {
								textObjects.get(i).toggleVis();
							}
							textObjects.get(5).moveDown(2);
							textObjects.get(6).moveDown(2);
							for (int i = 13; i < textObjects.size(); i++) {
								textObjects.get(i).moveDown(2);
							}
							obj.setText("View \20");
						} else if (obj.getText().equals("View \20")) {
							for (int i = 11; i < 13; i++) {
								textObjects.get(i).toggleVis();
							}
							textObjects.get(5).moveDown(-2);
							textObjects.get(6).moveDown(-2);
							for (int i = 13; i < textObjects.size(); i++) {
								textObjects.get(i).moveDown(-2);
							}
							obj.setText("View \21");
						}
						if (obj.getText().equals("Performance \21")) {
							for (int i = 13; i < 16; i++) {
								textObjects.get(i).toggleVis();
							}
							textObjects.get(6).moveDown(3);
							for (int i = 16; i < textObjects.size(); i++) {
								textObjects.get(i).moveDown(3);
							}
							obj.setText("Performance \20");
						} else if (obj.getText().equals("Performance \20")) {
							for (int i = 13; i < 16; i++) {
								textObjects.get(i).toggleVis();
							}
							textObjects.get(6).moveDown(-3);
							for (int i = 16; i < textObjects.size(); i++) {
								textObjects.get(i).moveDown(-3);
							}
							obj.setText("Performance \21");
						}
						if (obj.getText().equals("Controls \21")) {
							for (int i = 16; i < 22; i++) {
								textObjects.get(i).toggleVis();
							}
							obj.setText("Controls \20");
						} else if (obj.getText().equals("Controls \20")) {
							for (int i = 16; i < 22; i++) {
								textObjects.get(i).toggleVis();
							}
							obj.setText("Controls \21");
						}
					}
				}
			}
		}
	};

	private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {

		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			// TODO Auto-generated method stub
			camera.camZoom((float) yoffset / 5);
		}
	};
	private GLFWCursorPosCallback cursorCallback = new GLFWCursorPosCallback() {

		@Override
		public void invoke(long window, double xpos, double ypos) {
			// TODO Auto-generated method stub
			mouseX = xpos;
			mouseY = ypos;
			float mouseMoveX = ((float) lastY - (float) mouseY);
			mouseMoveX *= -1;
			mouseMoveX *= 0.5f;
			float mouseMoveY = ((float) lastX - (float) mouseX);
			mouseMoveY *= -1;
			mouseMoveY *= 0.5f;
			if (mouseHold) {
				camera.moveRotation(mouseMoveX, mouseMoveY, 0);
				lastX = mouseX;
				lastY = mouseY;
			}
		}

	};

}
