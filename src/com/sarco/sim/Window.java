package com.sarco.sim;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class Window {

	private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

	long window;

	int height;

	int width;

	boolean resized = false;

	public void init(boolean vsync) {
		// start GLFW
		glfwInit();
		
		// create window
		height = 720;
		width = 1280;
		window = glfwCreateWindow(width, height, "SarcoSim", NULL, NULL);
		
		if (window == NULL) {
			glfwTerminate();
		}
		
		// set a resize call back
		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			this.width = width;
			this.height = height;
			this.setResized(true);
		});

		// set window as the current context for all OpenGL api interactions
		glfwMakeContextCurrent(window);
		
		//enable vsync
		if (vsync) {
			glfwSwapInterval(1);
		}
		
		// Start OpenGL
		GL.createCapabilities();

		// Load icon image
		GLFWImage image = GLFWImage.malloc();
		GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
		ByteBuffer imageByte;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer comp = stack.mallocInt(1);
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);

			imageByte = stbi_load("./textures/icon.png", w, h, comp, 4);
		}
		image.set(50, 50, imageByte);
		imageBuffer.put(0, image);
		//set icon image
		glfwSetWindowIcon(window, imageBuffer);
		stbi_image_free(imageByte);
		
		// enable depth, show only outside face, allow transparency
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public void update(boolean vysnc) {
		//toggle vsync
		if (vysnc) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}
		//update window
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public void delete() {
		glfwDestroyWindow(window);
		glfwTerminate();
		errorCallback.free();
	}

	public long getWindow() {
		return window;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}

	public boolean isResized() {
		return resized;
	}

	public void setResized(boolean r) {
		resized = r;
	}
}
