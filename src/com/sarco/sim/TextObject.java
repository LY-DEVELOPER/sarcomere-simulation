package com.sarco.sim;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4i;

import lwjglgamedev.modelLoaders.MD5Loader;

public class TextObject {

	private String text;

	private Vector4f colour = new Vector4f(1f, 1f, 1f, 1f);

	private Vector3f position = new Vector3f(0f, 0f, 1f);

	private LetterObject[] letterObjects;

	private float scale = 1;

	private TextMesh textMesh;

	public TextObject(String Text, TextMesh tmesh) throws Exception {
		this.text = Text;
		this.textMesh = tmesh;
		getTextMeshes();
	}

	public void getTextMeshes() {
		if (letterObjects != null) {
			for (int i = 0; i < letterObjects.length; i++) {
				letterObjects[i].cleanUp();
			}
		}
		char[] chars = text.toCharArray();
		letterObjects = new LetterObject[chars.length];
		for (int i = 0; i < chars.length; i++) {
			letterObjects[i] = new LetterObject(textMesh.getMesh(chars[i]));
			letterObjects[i].movePosition(position.x + i * 19.2f, position.y, position.z);
		}
//		System.out.println(letterObjects.size());
		this.setScale(scale);
	}

	public LetterObject[] getLetter() {
		return letterObjects;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		try {
			getTextMeshes();
		} catch (Exception e) {
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
		return (new Vector4i(x1, x2, y1, y2));
	}

	public void toggleVis() {
		if (this.getPosition().x > 0) {
			this.movePosition(-10000, 0, 0);
		} else {
			this.movePosition(10000, 0, 0);
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		Vector3f newPosition = new Vector3f(0, 0, 0);
		;
		newPosition.x = x;
		position = new Vector3f(x, y, z);
		for (int i = 0; i < letterObjects.length; i++) {
			letterObjects[i].movePosition(x + (19.2f * i), y, z);
		}
	}

	public void movePosition(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
		for (int i = 0; i < letterObjects.length; i++) {
			letterObjects[i].movePosition(x, y, z);
		}
	}

	public void setScale(float scale) {
		this.scale = scale;
		for (int i = 0; i < letterObjects.length; i++) {
			letterObjects[i].setScale(scale);
		}
	}

	public float getScale() {
		return scale;
	}
	
	public void fix() {
		getTextMeshes();
	}

	public void moveDown(float amount) {
		this.movePosition(0, amount * 30, 0);
	}

	public boolean isMouseOver(double x, double y) {
		Vector4i border = this.getBorders();
		if (x > border.x && x < border.y && y > border.z && y < border.w) {
			return true;
		}
		return false;
	}

}
