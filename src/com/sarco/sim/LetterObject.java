package com.sarco.sim;

public class LetterObject extends Object {
	
	public LetterObject(Mesh mesh) {
		super(mesh);
	}
	
	// This is required as parent deletes old mesh, this function doesn't
	public void cleanUp() {
		this.mesh = null;
	}
}
