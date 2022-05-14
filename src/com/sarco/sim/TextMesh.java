package com.sarco.sim;

import java.util.ArrayList;
import java.util.List;

public class TextMesh {

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
		char c;
		String text = "";
		for (int i = 0; i <= 256; i++) {
			c = (char) i;
			text += Character.toString(c);
		}
		char[] chars = text.toCharArray();
		List<Mesh> tempMeshes = new ArrayList<>();
		float tileWidth = (float) texture.getWidth() / (float) 16;
		float tileHeight = (float) texture.getHeight() / (float) 16;
		for (int i = 0; i < 256; i++) {
			char currChar = chars[i];
			int col = currChar % 16;
			int row = currChar / 16;

			List<Float> positions = new ArrayList<Float>();
			List<Float> textCoords = new ArrayList<Float>();
			float[] normals = new float[0];
			List<Integer> indices = new ArrayList<Integer>();

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
				textCoords.add((float) (col + x) / (float) 16);
				textCoords.add((float) (row + y) / (float) 16);
				indices.add(j);
			}

			indices.add(0);
			indices.add(2);

			float[] posArr = listToArray(positions);
			float[] textCoordsArr = listToArray(textCoords);
			int[] indicesArr = indices.stream().mapToInt(j -> j).toArray();
			Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr, null, null);
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
