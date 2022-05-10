package com.sarco.sim;

import org.joml.Vector3f;
import org.joml.Vector3i;

public class Slider {
	String id;
	int position;
	float value;
	
	TextObject bar;
	TextObject picker;
	
	public Slider(String id, Vector3f position, float value, TextMesh meshes) throws Exception {
		this.id = id;
		this.value = value;
		bar = new TextObject(" \23\23\23\23\23\23\23\23\23\23\23 ", meshes);
		picker = new TextObject("\22", meshes);
		bar.setPosition(position.x, position.y, position.z);
		bar.setScale(0.3f);
		picker.setPosition(position.x + (192 * (value / 100)) + (0.3f*64), position.y, position.z);
		picker.setScale(0.3f);
		bar.fix();
		picker.fix();
	}

	public Slider(String id, Vector3f position, TextMesh meshes) throws Exception {
		this(id, position, 50, meshes);
	}
	
	public void moveDown(float amount) {
		bar.movePosition(0, amount * 35, 0);
		picker.movePosition(0, amount * 35, 0);
	}
	
	public void pickerClick(int x) {
		if (value >= 0 && value <= 100) {
			picker.getPosition().x = x -9;
			value = Math.round(((x - bar.getPosition().x) + (0.3f*-64 - 9)) / 1.92); 
		}
		if(value < 0) {
			value = 0f;
			picker.getPosition().x = bar.getPosition().x + (0.3f*64);
		}
		if(value > 100) {
			value = 100f;
			picker.getPosition().x = bar.getPosition().x + 192 + (0.3f*64);
		}
	}
	
	public TextObject getBar() {
		return bar;
	}
	public TextObject getPicker() {
		return picker;
	}
	
	public void toggleVis() {
		bar.toggleVis();
		picker.toggleVis();
	}
	
	public void moveDown(int i) {
		bar.moveDown(i);
		picker.moveDown(i);
	}
}
