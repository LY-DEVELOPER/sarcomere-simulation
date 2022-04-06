package com.sarco.sim;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.stb.STBImage.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

public class Window {

	private GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);

	long window;

	int height;

	int width;

	boolean resized = false;

	public void init() {
		// TODO Auto-generated method stub
		glfwSetErrorCallback(errorCallback);
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		height = 480;
		width = 640;
		window = glfwCreateWindow(width, height, "SarcoSim", NULL, NULL);
		if (window == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}
		glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
			this.width = width;
			this.height = height;
			this.setResized(true);
		});

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		GLFWImage image = GLFWImage.malloc(); GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        try {
			image.set(50, 50, loadImage("./icon.png"));
	        imagebf.put(0, image);
	        glfwSetWindowIcon(window, imagebf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//		glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
		glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
	}

	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public void cleanUp() {
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
		// TODO Auto-generated method stub
		return resized;
	}

	public void setResized(boolean r) {
		// TODO Auto-generated method stub
		resized = r;
	}

	public static ByteBuffer loadImage(String path) throws Exception {
		ByteBuffer image;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer comp = stack.mallocInt(1);
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);

			image = stbi_load(path, w, h, comp, 4);
			if (image == null) {
				// throw new resource_error("Could not load image resources.");
			}
		}
		return image;
	}
}
