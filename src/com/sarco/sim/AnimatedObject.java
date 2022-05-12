package com.sarco.sim;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import lwjglgamedev.modelLoaders.AnimatedFrame;

public class AnimatedObject extends Object {
	  private int currentFrame;
	  private List<AnimatedFrame> frames;

	  public AnimatedObject(Mesh mesh, List<AnimatedFrame> frames) {
	    super(mesh);
	    this.frames = frames;
	    currentFrame = 0;
	  }

	   public AnimatedFrame getFrame () {
	       return this.frames.get(currentFrame);
	   }

	  public void nextFrame (int speed) {
	    currentFrame += speed;
	    if(currentFrame > frames.size() - 1 || currentFrame < 0) {
	      currentFrame = 0;
	    }
	  }
	  
	  public int getFrameInt() {
		  return currentFrame;
	  }
	}