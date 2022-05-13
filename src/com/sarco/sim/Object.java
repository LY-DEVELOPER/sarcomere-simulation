package com.sarco.sim;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Object {

	protected Mesh mesh;

	private final Vector3f position = new Vector3f(0, 0, 0);;

	private float scale = 1;

	private final Vector3f rotation = new Vector3f(0, 0, 0);;

	public Object(Mesh mesh) {
		this.mesh = mesh;
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
		// Moves the x and z so object moves relative of world and not its rotation
		if (offsetZ != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
			position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
		}
		if (offsetX != 0) {
			position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
			position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
		}
		// Moves y
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
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		//Delete old mesh
		if(this.getMesh() != null) {
			this.mesh.cleanUp();
		}
		this.mesh = mesh;
	}

	// Set the individual colour values of the object
    public void setR(float x) {
    	Vector4f i = this.getMesh().getColour();
    	this.getMesh().setColour(x, i.y, i.z, i.w);
    }
    public void setG(float y) {
    	Vector4f i = this.getMesh().getColour();
    	this.getMesh().setColour(i.x, y, i.z, i.w);
    }
    public void setB(float z) {
    	Vector4f i = this.getMesh().getColour();
    	this.getMesh().setColour(i.x, i.y, z, i.w);
    }
    public void setT(float w) {
    	Vector4f i = this.getMesh().getColour();
    	this.getMesh().setColour(i.x, i.y, i.z, w);
    }
}