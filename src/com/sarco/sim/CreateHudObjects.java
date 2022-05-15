package com.sarco.sim;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

public class CreateHudObjects {
	
	public static List<TextObject> genTexts(TextMesh textMesh, Window window) {
		ArrayList<TextObject> textObjects = new ArrayList<>();
		// Adding the ui objects
		textObjects.add(new TextObject("AutoPlay:On", textMesh));
		textObjects.get(0).setPosition(10f, 5f, 1);
		textObjects.add(new TextObject("FPS:", textMesh));
		textObjects.get(1).setPosition(window.getWidth() - 190f, window.getHeight() - 40f, 1);
		textObjects.add(new TextObject("Current Process:", textMesh));
		textObjects.get(2).setPosition(10f, window.getHeight() - 60f, 1);
		textObjects.add(new TextObject("Contracting:Off", textMesh));
		textObjects.get(3).setPosition(window.getWidth() - 300f, 5f, 1);
		textObjects.add(new TextObject("Appearance \21", textMesh));
		textObjects.get(4).setPosition(10f, 30 + 5, 1);
		textObjects.add(new TextObject("View \21", textMesh));
		textObjects.get(5).setPosition(10f, 30 * 2 + 5, 1);
		textObjects.add(new TextObject("Performance \21", textMesh));
		textObjects.get(6).setPosition(10f, 30 * 3 + 5, 1);
		textObjects.add(new TextObject("Controls \21", textMesh));
		textObjects.get(7).setPosition(10f, 30 * 4 + 5, 1);

		textObjects.add(new TextObject("Myosin Colour", textMesh));
		textObjects.get(8).setPosition(20f, 30 * 2 + 5, 1);
		textObjects.add(new TextObject("Myosin Transparency", textMesh));
		textObjects.get(9).setPosition(20f, 30 * 4 + 5, 1);
		textObjects.add(new TextObject("Actin Colour", textMesh));
		textObjects.get(10).setPosition(20f, 30 * 6 + 5, 1);
		textObjects.add(new TextObject("Actin Transparency", textMesh));
		textObjects.get(11).setPosition(20f, 30 * 8 + 5, 1);

		textObjects.add(new TextObject("Re-Centre Camera", textMesh));
		textObjects.get(12).setPosition(20f, 30 * 3 + 5, 1);
		textObjects.add(new TextObject("Speed:", textMesh));
		textObjects.get(13).setPosition(20f, 30 * 4 + 5, 1);

		textObjects.add(new TextObject("Quality: Medium", textMesh));
		textObjects.get(14).setPosition(20f, 30 * 4 + 5, 1);
		textObjects.add(new TextObject("V-sync: ", textMesh));
		textObjects.get(15).setPosition(20f, 30 * 5 + 5, 1);
		textObjects.add(new TextObject("FPS: ", textMesh));
		textObjects.get(16).setPosition(20f, 30 * 6 + 5, 1);

		textObjects.add(new TextObject("Arrow Keys: Move Camera", textMesh));
		textObjects.get(17).setPosition(20f, 30 * 5 + 5, 1);
		textObjects.add(new TextObject("Click + Drag: Rotate Camera", textMesh));
		textObjects.get(18).setPosition(20f, 30 * 6 + 5, 1);
		textObjects.add(new TextObject("Scroll: Zoom", textMesh));
		textObjects.get(19).setPosition(20f, 30 * 7 + 5, 1);
		textObjects.add(new TextObject("C: Center Camera", textMesh));
		textObjects.get(20).setPosition(20f, 30 * 8 + 5, 1);
		textObjects.add(new TextObject("A: Toggle AutoPlay", textMesh));
		textObjects.get(21).setPosition(20f, 30 * 9 + 5, 1);
		textObjects.add(new TextObject("Space Bar: Contract", textMesh));
		textObjects.get(22).setPosition(20f, 30 * 10 + 5, 1);

		// setting all the text objects to the same scale
		for (TextObject obj : textObjects) {
			obj.setScale(0.3f);
		}
		// making specific text objects invisible
		for (int i = 8; i < textObjects.size(); i++) {
			textObjects.get(i).toggleVis();
		}

		// making slider objects invisible
		return textObjects;
	}

	public static List<Slider> genSliders(TextMesh textMesh) {
		ArrayList<Slider> sliderObjects = new ArrayList<>();

		// Adding the ui objects
		sliderObjects.add(new Slider("mColour", new Vector3f(0, 30 * 3 + 5, 1), textMesh));
		sliderObjects.add(new Slider("mTran", new Vector3f(0, 30 * 5 + 5, 1), 100, textMesh));
		sliderObjects.add(new Slider("aColour", new Vector3f(0, 30 * 7 + 5, 1), 100, textMesh));
		sliderObjects.add(new Slider("aTran", new Vector3f(0, 30 * 9 + 5, 1), 100, textMesh));

		sliderObjects.add(new Slider("speed", new Vector3f(0, 30 * 5 + 5, 1), 20, textMesh));

		sliderObjects.add(new Slider("fps", new Vector3f(0, 30 * 7 + 5, 1), 20, textMesh));

		// making slider objects invisible
		sliderObjects.forEach(Slider::toggleVis);
		return sliderObjects;
	}
}
