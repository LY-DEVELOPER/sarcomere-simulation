package com.sarco.sim;

public class LetterObject extends SimObject {
	
	public LetterObject(Mesh mesh) {
		super(mesh);
	}
	
	// This is required as parent deletes old mesh, this function doesn't
	public void delete() {
		this.mesh = null;
	}
}
