package com.sarco.sim;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import lwjglgamedev.modelLoaders.AnimatedFrame;

public class AnimatedObject extends Object {
	  private int currentFrame = 0;
	  private List<AnimatedFrame> frames;

	  
	  // Constructor adds mesh and frames of the animation
	  public AnimatedObject(Mesh mesh, List<AnimatedFrame> frames) {
	    super(mesh);
	    this.frames = frames;
	  }

	  //Returns the frame object
	   public AnimatedFrame getFrame () {
	       return this.frames.get(currentFrame);
	   }

	   // advances animation by x speed
	  public void nextFrame (int speed) {
	    currentFrame += speed;
	    if(currentFrame > frames.size() - 1 || currentFrame < 0) {
	      currentFrame = 0;
	    }
	  }
	  
	  // get the current animation step
	  public int getFrameInt() {
		  return currentFrame;
	  }
	}