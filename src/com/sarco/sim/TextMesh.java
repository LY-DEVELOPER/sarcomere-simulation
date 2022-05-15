package com.sarco.sim;

import java.util.ArrayList;
import java.util.List;

public class TextMesh {

	private List<Mesh> meshes;

	public TextMesh() {
		// get font texture
		Texture texture = new Texture("./textures/font.png");
		meshes = new ArrayList<>();
		// get tiles of font
		float tileWidth = (float) texture.getWidth() / (float) 16;
		float tileHeight = (float) texture.getHeight() / (float) 16;
		//for each character create a mesh
		for (int i = 0; i < 256; i++) {
			// gets location of current chat
			int col = i % 16;
			int row = i / 16;
			
			// create lists to store data
			List<Float> positions = new ArrayList<>();
			List<Float> textCoords = new ArrayList<>();
			List<Integer> indices = new ArrayList<>();
			
			// creates 4 verticies for each corner of letter
			for (int j = 0; j < 4; j++) {
				int x = 0;
				int y = 0;
				if (j == 0 || j == 1)
					positions.add(0.0f);
				if (j == 2 || j == 3) {
					positions.add(tileWidth);
					x = 1;
				}
				if (j == 0 || j == 3)
					positions.add(0.0f);
				if (j == 2 || j == 1) {
					positions.add(tileHeight);
					y = 1;
				}

				positions.add(0.0f);
				// sets texture coords to location of letter
				textCoords.add((float) (col + x) / (float) 16);
				textCoords.add((float) (row + y) / (float) 16);
				indices.add(j);
			}
			indices.add(0);
			indices.add(2);

			// add lists to arrays
			float[] posArr = listToArray(positions);
			float[] textCoordsArr = listToArray(textCoords);
			int[] indicesArr = indices.stream().mapToInt(j -> j).toArray();
			//create the mesh
			Mesh mesh = new Mesh(posArr, textCoordsArr, null, indicesArr, null, null);
			mesh.setTexture(texture);
			meshes.add(mesh);
		}
	}

	public Mesh getMesh(int num) {
		return meshes.get(num);
	}

	public static float[] listToArray(List<Float> list) {
		int size = list != null ? list.size() : 0;
		float[] floatArr = new float[size];
		for (int i = 0; i < size; i++) {
			floatArr[i] = list.get(i);
		}
		return floatArr;
	}

}
