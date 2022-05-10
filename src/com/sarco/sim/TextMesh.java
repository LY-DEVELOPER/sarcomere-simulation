package com.sarco.sim;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;

public class TextMesh {
	private static final float ZPOS = 0.0f;

	private static final int VERTICES_PER_QUAD = 4;
	
	private Vector4f colour = new Vector4f(1,1,1,1);
	
	private Mesh[] meshes;
	
	public TextMesh() {
		try {
			buildMesh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void buildMesh() throws Exception {
		Texture texture = new Texture("./textures/font.png");
		
		int numCols = 16;
		int numRows = 16;
		
		char c;
		
		String text = "";
		
		for(int i = 0; i <= 256; i++) {
			c = (char) i;
			text += Character.toString(c);	
		}
		
		char[] chars = text.toCharArray();

		int numChars = chars.length;

		
		List<Mesh> tempMeshes = new ArrayList<>();

		float tileWidth = (float) texture.getWidth() / (float) numCols;
		float tileHeight = (float) texture.getHeight() / (float) numRows;
		
		for (int j = 0; j < numChars; j++) {
			char currChar = chars[j];
			int col = currChar % numCols;
			int row = currChar / numCols;
			
			List<Float> positions = new ArrayList();
			List<Float> textCoords = new ArrayList();
			float[] normals = new float[0];
			List<Integer> indices = new ArrayList();

			// Build a character tile composed by two triangles

			// Left Top vertex
			positions.add(0.0f); // x
			positions.add(0.0f); // y
			positions.add(ZPOS); // z
			textCoords.add((float) col / (float) numCols);
			textCoords.add((float) row / (float) numRows);
			indices.add(0);

			// Left Bottom vertex
			positions.add(0.0f); // x
			positions.add(tileHeight); // y
			positions.add(ZPOS); // z
			textCoords.add((float) col / (float) numCols);
			textCoords.add((float) (row + 1) / (float) numRows);
			indices.add(1);

			// Right Bottom vertex
			positions.add(tileWidth); // x
			positions.add(tileHeight); // y
			positions.add(ZPOS); // z
			textCoords.add((float) (col + 1) / (float) numCols);
			textCoords.add((float) (row + 1) / (float) numRows);
			indices.add(2);

			// Right Top vertex
			positions.add(tileWidth); // x
			positions.add(0.0f); // y
			positions.add(ZPOS); // z
			textCoords.add((float) (col + 1) / (float) numCols);
			textCoords.add((float) row / (float) numRows);
			indices.add(3);

			// Add indices por left top and bottom right vertices
			indices.add(0);
			indices.add(2);
			
			float[] posArr = listToArray(positions);
			float[] textCoordsArr = listToArray(textCoords);
			int[] indicesArr = indices.stream().mapToInt(i -> i).toArray();
			Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
			mesh.setColour(colour.x, colour.y, colour.z, colour.w);
			mesh.setTexture(texture);
			tempMeshes.add(mesh);
		}
		this.meshes = meshToArray(tempMeshes);
	}
	
	public Mesh getMesh(int num) {
		return meshes[num];
	}
	
	public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }
	public static Mesh[] meshToArray(List<Mesh> mesh) {
        int size = mesh != null ? mesh.size() : 0;
        Mesh[] meshArr = new Mesh[size];
        for (int i = 0; i < size; i++) {
            meshArr[i] = mesh.get(i);
        }
        return meshArr;
    }
}
