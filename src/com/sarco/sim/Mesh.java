package com.sarco.sim;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	private final int VAO;

	private final int VBO;
	
	private final int inVBO;
	
	private final int colVBO;

	private final int vertexCount;

	public Mesh(float[] positions, int[] indices, float[] colours) {
		 FloatBuffer posBuffer = null;
	        FloatBuffer colourBuffer = null;
	        IntBuffer indicesBuffer = null;
	        try {
	            vertexCount = indices.length;

	            VAO = glGenVertexArrays();
	            glBindVertexArray(VAO);

	            // Position VBO
	            VBO = glGenBuffers();
	            posBuffer = MemoryUtil.memAllocFloat(positions.length);
	            posBuffer.put(positions).flip();
	            glBindBuffer(GL_ARRAY_BUFFER, VBO);
	            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
	            glEnableVertexAttribArray(0);
	            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

	            // Colour VBO
	            colVBO = glGenBuffers();
	            colourBuffer = MemoryUtil.memAllocFloat(colours.length);
	            colourBuffer.put(colours).flip();
	            glBindBuffer(GL_ARRAY_BUFFER, colVBO);
	            glBufferData(GL_ARRAY_BUFFER, colourBuffer, GL_STATIC_DRAW);
	            glEnableVertexAttribArray(1);
	            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

	            // Index VBO
	            inVBO = glGenBuffers();
	            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
	            indicesBuffer.put(indices).flip();
	            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, inVBO);
	            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

	            glBindBuffer(GL_ARRAY_BUFFER, 0);
	            glBindVertexArray(0);
	        } finally {
	            if (posBuffer != null) {
	                MemoryUtil.memFree(posBuffer);
	            }
	            if (colourBuffer != null) {
	                MemoryUtil.memFree(colourBuffer);
	            }
	            if (indicesBuffer != null) {
	                MemoryUtil.memFree(indicesBuffer);
	            }
	        }
	}

	public int getVAO() {
		return VAO;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);

		// Delete the VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(VBO);
		glDeleteBuffers(inVBO);
		glDeleteBuffers(colVBO);

		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(VAO);
	}
}
