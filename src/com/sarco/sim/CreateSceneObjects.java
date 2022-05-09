package com.sarco.sim;

import java.util.ArrayList;

import lwjglgamedev.modelLoaders.MD5AnimModel;
import lwjglgamedev.modelLoaders.MD5Loader;
import lwjglgamedev.modelLoaders.MD5Model;
import lwjglgamedev.modelLoaders.OBJLoader;

public class CreateSceneObjects {

	
	public static ArrayList<Object> gen() throws Exception {
		ArrayList<Object> objects = new ArrayList<Object>();
		Mesh skyBox = OBJLoader.loadMesh("/assets/skybox.obj");
		skyBox.setColour(0.5f, 0.5f, 0.5f, 1);
		skyBox.setTexture("./textures/skybox.png");
		Mesh grid = OBJLoader.loadMesh("/assets/grid.obj");
		grid.setTexture("./textures/grid.png");
		Mesh length = OBJLoader.loadMesh("/assets/length.obj");
		length.setColour(0, 0.7f, 0.7f, 0.5f);
		Mesh actin = OBJLoader.loadMesh("/assets/actin.obj");
		actin.setTexture("./textures/texture.png");
		actin.setColour(0, 0.58f, 1f, 1);
		MD5Model myosin = MD5Model.parse("/assets/mediummyosin.md5mesh");
		MD5AnimModel animMyo = MD5AnimModel.parse("/assets/myosin.md5anim");
		objects.add(new Object(skyBox));
		objects.get(0).setScale(20f);
		objects.add(new Object(grid));
		objects.get(1).setPosition(0, -6, 0);
		objects.get(1).setScale(20);
		objects.add(new Object(length));
		objects.get(2).setPosition(0, 1, 0);
		objects.add(new Object(actin));
		objects.get(3).setPosition(5, 0.38f, 0);
		objects.add(new Object(actin));
		objects.get(4).setPosition(5, -0.38f, 0);
		objects.add(new Object(actin));
		objects.get(5).setRotation(0, 180, 0);
		objects.get(5).setPosition(-5, 0.38f, 0);
		objects.add(new Object(actin));
		objects.get(6).setRotation(0, 180, 0);
		objects.get(6).setPosition(-5, -0.38f, 0);
		for (int i = 0; i < 135; i+=3) {
			AnimObject myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(118.8f + (i/3) * 36, 0, 0);
			myosinObj.setPosition(0 - (i/3) * 0.143f, 0, 0);
			objects.add(myosinObj);
			myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(118.8f * 2 + (i/3) * 36, 0, 0);
			myosinObj.setPosition(0 - (i/3) * 0.143f, 0, 0);
			objects.add(myosinObj);
			myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(0 + (i/3) * 36, 0, 0);
			myosinObj.setPosition(0 - (i/3) * 0.143f, 0, 0);
			objects.add(myosinObj);
		}
		for (int i = 0; i < 135; i+=3) {
			AnimObject myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(118.8f - (i/3) * 36, 180, 0);
			myosinObj.setPosition(0 + (i/3) * 0.143f, 0, 0);
			objects.add(myosinObj);
			myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(118.8f * 2 - (i/3) * 36, 180, 0);
			myosinObj.setPosition(0 + (i/3) * 0.143f, 0, 0);
			objects.add(myosinObj);
			myosinObj = MD5Loader.process(myosin, animMyo);
			myosinObj.setRotation(0 - (i/3) * 36, 180, 0);
			myosinObj.setPosition(0 + (i/3) * 0.143f, 0, 0);
			objects.add(myosinObj);
		}
		return objects;
	}
}
