package com.sarco.sim;

import org.joml.Vector3f;

public class Object {

	private Mesh[] meshes;

	private final Vector3f position;

	private float scale;

	private final Vector3f rotation;

	public Object() {
		position = new Vector3f(0, 0, 0);
		scale = 1;
		rotation = new Vector3f(0, 0, 0);
	}

	public Object(Mesh mesh) {
		this();
		this.meshes = new Mesh[] { mesh };
	}

	public Object(Mesh[] meshes) {
		this();
		this.meshes = meshes;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public void movePosition(float offsetX, float offsetY, float offsetZ) {
		if (offsetZ != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
			position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
		}
		if (offsetX != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
			position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
		}
		position.y += offsetY;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		rotation.x += offsetX;
		rotation.y += offsetY;
		rotation.z += offsetZ;
	}

	public Mesh getMesh() {
		return meshes[0];
	}

	public Mesh[] getMeshes() {
		return meshes;
	}

	public void setMeshes(Mesh[] meshes) {
		this.meshes = meshes;
	}

	public void setMesh(Mesh mesh) {
		if (this.meshes != null) {
			for (Mesh currMesh : meshes) {
				currMesh.cleanUp();
			}
		}
		this.meshes = new Mesh[] { mesh };
	}
}