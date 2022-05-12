package com.sarco.sim;

import org.joml.Vector3f;

public class Camera {

	private Vector3f position;
	private Vector3f rotation;
	private float scale;

	public Camera() {
		position = new Vector3f(0, 0, 2);
		rotation = new Vector3f(0, 0, 0);
		scale = 1;
	}

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
