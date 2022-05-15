package com.sarco.sim;

import org.joml.Vector3f;

public class Slider {
	String id;
	int position;
	float value;
	
	TextObject bar;
	TextObject picker;
	
	public Slider(String id, Vector3f position, float value, TextMesh meshes) {
		this.id = id;
		this.value = value;
		// create bar for slider
		bar = new TextObject(" \23\23\23\23\23\23\23\23\23\23\23 ", meshes);
		bar.setPosition(position.x, position.y, position.z);
		bar.setScale(0.3f);
		// create picker for slider
		picker = new TextObject("\22", meshes);
		picker.setPosition(position.x + (192 * (value / 100)) + (0.3f*64), position.y, position.z);
		picker.setScale(0.3f);
	}

	public Slider(String id, Vector3f position, TextMesh meshes) {
		this(id, position, 50, meshes);
	}
	
	public void pickerClick(int x) {
		// set picker to mouse x
		if (value >= 0 && value <= 100) {
			picker.setPosition(x - 9, picker.getPosition().y, picker.getPosition().z);
			value = Math.round(((x - bar.getPosition().x) + (0.3f*-64 - 9)) / 1.92); 
		}
		// keeps picker between 0 and 100
		if(value < 0) {
			value = 0f;
			picker.setPosition(bar.getPosition().x + (0.3f*64), picker.getPosition().y, picker.getPosition().z);
		}
		if(value > 100) {
			value = 100f;
			picker.setPosition(bar.getPosition().x + 192 + (0.3f*64), picker.getPosition().y, picker.getPosition().z);
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
