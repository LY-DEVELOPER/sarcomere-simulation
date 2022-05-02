package com.sarco.sim;

import java.util.ArrayList;

import org.joml.Vector3f;

import lwjglgamedev.modelLoaders.MD5AnimModel;
import lwjglgamedev.modelLoaders.MD5Loader;
import lwjglgamedev.modelLoaders.MD5Model;

public class Quality {
	
	String setQuality = "Medium";

	public void set(String quality, ArrayList<Object> objects) throws Exception {
		MD5Model myosin = MD5Model.parse("/assets/"+quality.toLowerCase()+"myosin.md5mesh");
		MD5AnimModel animMyo = MD5AnimModel.parse("/assets/myosin.md5anim");
		for (Object object : objects) {
			if (object instanceof AnimObject) {
				object.setMeshes(MD5Loader.process(myosin, animMyo).getMeshes());
			}
		}
		setQuality = quality;
	}
	
	public String getQuality() {
		return setQuality;
	}

}
