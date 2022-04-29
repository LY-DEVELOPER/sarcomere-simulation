package com.sarco.sim;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.joml.Vector4i;

import com.sarco.sim.utilities.MD5Loader;

public class TextObject extends Object {
	private static final float ZPOS = 0.0f;

	private static final int VERTICES_PER_QUAD = 4;

	private String text;

	private final int numCols;

	private final int numRows;
	
	private Vector4f colour = new Vector4f(1f,1f,1f,1f);

	public TextObject(String text, String fontFileName, int numCols, int numRows) throws Exception {
		super();
		this.text = text;
		this.numCols = numCols;
		this.numRows = numRows;
		Texture texture = new Texture(fontFileName);
		this.setMeshes(new Mesh[] { buildMesh(texture, numCols, numRows)});
	}

	private Mesh buildMesh(Texture texture, int numCols, int numRows) throws Exception {
        byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
        int numChars = chars.length;

        List<Float> positions = new ArrayList();
        List<Float> textCoords = new ArrayList();
        float[] normals   = new float[0];
        List<Integer> indices   = new ArrayList();

        float tileWidth = (float)texture.getWidth() / (float)numCols;
        float tileHeight = (float)texture.getHeight() / (float)numRows;
        for(int i=0; i<numChars; i++) {
            byte currChar = chars[i];
            int col = currChar % numCols;
            int row = currChar / numCols;

            // Build a character tile composed by two triangles

            // Left Top vertex
            positions.add((float)i*tileWidth); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)col / (float)numCols );
            textCoords.add((float)row / (float)numRows );
            indices.add(i*VERTICES_PER_QUAD);

            // Left Bottom vertex
            positions.add((float)i*tileWidth); // x
            positions.add(tileHeight); //y
            positions.add(ZPOS); //z
            textCoords.add((float)col / (float)numCols );
            textCoords.add((float)(row + 1) / (float)numRows );
            indices.add(i*VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add((float)i*tileWidth + tileWidth); // x
            positions.add(tileHeight); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(col + 1)/ (float)numCols );
            textCoords.add((float)(row + 1) / (float)numRows );
            indices.add(i*VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add((float)i*tileWidth + tileWidth); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(col + 1)/ (float)numCols );
            textCoords.add((float)row / (float)numRows );
            indices.add(i*VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);
        }
        
        float[] posArr = MD5Loader.listToArray(positions);
        float[] textCoordsArr = MD5Loader.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setColour(colour.x, colour.y, colour.z, colour.w);
        mesh.setTexture(texture);
        return mesh;
	}
	
	public String getText() {
	    return text;
	}

	public void setText(String text) {
	    this.text = text;
	    Texture texture = this.getMesh().getTexture();
	    try {
			this.setMeshes(new Mesh[] { buildMesh(texture, numCols, numRows)});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Vector4i getBorders() {
		int x1, x2, y1, y2;
		int size = this.text.length();
		int width = size * Math.round(64 * this.getScale());
		x1 = (int) this.getPosition().x;
		x2 = x1 + width;
		y1 = (int) this.getPosition().y;
		y2 = y1 + Math.round(128 * this.getScale());
		return(new Vector4i(x1, x2, y1, y2));
	}
	
	public void toggleVis() {
		if(this.getPosition().x > 0) {
			this.movePosition(-10000, 0, 0);
		}else {
			this.movePosition(10000, 0, 0);
		}
	}
	
	public void reset() {
		setText(text);
	}
	
	public void moveDown(float amount) {
		this.movePosition(0, amount * 30, 0);
	}
	
	public boolean isMouseOver(double x, double y) {
		Vector4i border = this.getBorders();
		if(x > border.x && x < border.y && y > border.z && y < border.w) {
			return true;
		}
		return false;
	}
}
