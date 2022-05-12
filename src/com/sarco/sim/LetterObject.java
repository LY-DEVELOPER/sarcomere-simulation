package com.sarco.sim;

public class LetterObject extends Object {
	
	public LetterObject(Mesh mesh) {
		super(mesh);
	}
	
	public void cleanUp() {
		this.mesh = null;
	}
}
