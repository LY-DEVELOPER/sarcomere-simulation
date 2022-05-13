package com.sarco.sim;

import org.joml.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 2);
	private Vector3f rotation = new Vector3f(0, 0, 0);
	private float scale = 1;

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		position = new Vector3f(x, y, z);
	}

	public void movePosition(float x, float y, float z) {
		position.add(new Vector3f(x, y, z));
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z) {
		rotation = new Vector3f(x, y, z);
	}

	public void moveRotation(float x, float y, float z) {
		rotation.add(new Vector3f(x, y, z));
	}
	
	public void camZoom(float zoom) {
		// Moves the cameras z between 5 and 0.1
		zoom *= -1;
		zoom *= position.z;
		this.position.z += zoom;
		if (position.z > 5f) {
			position.z = 5f;
		} else if (position.z < 0.1f) {
			position.z = 0.1f;
		}
	}

}
