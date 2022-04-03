package com.sarco.sim;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	private final int VAO;

	 private final List<Integer> VBOList;

	private final int vertexCount;
	private Texture texture;
	private Vector3f colour;

	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
		 FloatBuffer posBuffer = null;
	        FloatBuffer textCoordsBuffer = null;
	        IntBuffer indicesBuffer = null;
	        FloatBuffer vecNormalsBuffer = null;
	        colour = new Vector3f(0,0,1f);
	        try {
	            vertexCount = indices.length;
	            VBOList = new ArrayList<>();

	            VAO = glGenVertexArrays();
	            glBindVertexArray(VAO);

	            // Position VBO
	            int VBO = glGenBuffers();
	            VBOList.add(VBO);
	            posBuffer = MemoryUtil.memAllocFloat(positions.length);
	            posBuffer.put(positions).flip();
	            glBindBuffer(GL_ARRAY_BUFFER, VBO);
	            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
	            glEnableVertexAttribArray(0);
	            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
	            
	            // Vertex normals VBO
	            VBO = glGenBuffers();
	            VBOList.add(VBO);
	            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
	            vecNormalsBuffer.put(normals).flip();
	            glBindBuffer(GL_ARRAY_BUFFER, VBO);
	            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
	            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
	            
	            // Texture coordinates VBO
	            VBO = glGenBuffers();
	            VBOList.add(VBO);
	            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
	            textCoordsBuffer.put(textCoords).flip();
	            glBindBuffer(GL_ARRAY_BUFFER, VBO);
	            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
	            glEnableVertexAttribArray(1);
	            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

	            // Index VBO
	            VBO = glGenBuffers();
	            VBOList.add(VBO);
	            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
	            indicesBuffer.put(indices).flip();
	            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, VBO);
	            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

	            glBindBuffer(GL_ARRAY_BUFFER, 0);
	            glBindVertexArray(0);
	        } finally {
	            if (posBuffer != null) {
	                MemoryUtil.memFree(posBuffer);
	            }
	            if (textCoordsBuffer != null) {
	                MemoryUtil.memFree(textCoordsBuffer);
	            }
	            if (indicesBuffer != null) {
	                MemoryUtil.memFree(indicesBuffer);
	            }
	        }
	}
	
	public void render() {
		if(texture != null) {
			// Activate first texture bank
	        glActiveTexture(GL_TEXTURE0);
	        // Bind the texture
	        glBindTexture(GL_TEXTURE_2D, texture.getId());
		}

		 // Draw the mesh
		glBindVertexArray(VAO);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

		// Restore state
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public int getVAO() {
		return VAO;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public void setTexture(String sTexture) {
		try {
			this.texture = new Texture(sTexture);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public boolean isTextured() {
		if(texture != null) {
			return true;
		} else {
			return false;
		} 
	}
	
	public void setColour(float x, float y, float z) {
		colour.x = x;
		colour.y = y;
		colour.z = z;
	}
	
	public Vector3f getColour() {
		return colour;
	}
	

	public void cleanUp() {
		glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int VBO : VBOList) {
            glDeleteBuffers(VBO);
        }

        // Delete the texture
        if(texture != null) {
        	texture.cleanup();
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(VAO);
	}
}
