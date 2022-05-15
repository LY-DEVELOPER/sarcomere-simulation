package com.sarco.sim;

import java.util.ArrayList;

import lwjglgamedev.modelLoaders.MD5AnimModel;
import lwjglgamedev.modelLoaders.MD5Loader;
import lwjglgamedev.modelLoaders.MD5Model;

public class Quality {
	
	private String setQuality = "Low";

	public void set(String quality, ArrayList<Object> objects) throws Exception {
		// Loads myosin model with matching quality
		MD5Model myosin = MD5Model.parse("/assets/"+quality.toLowerCase()+"myosin.md5mesh");
		MD5AnimModel animMyo = MD5AnimModel.parse("/assets/myosin.md5anim");
		// Apply meshes to objects
		for (Object object : objects) {
			if (object instanceof AnimatedObject) {
				object.setMesh(MD5Loader.process(myosin, animMyo).getMesh());
			}
		}
		setQuality = quality;
	}
	
	public String getQuality() {
		return setQuality;
	}

}
