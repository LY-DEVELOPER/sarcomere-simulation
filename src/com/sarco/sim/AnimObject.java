package com.sarco.sim;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import lwjglgamedev.modelLoaders.AnimatedFrame;

public class AnimObject extends Object {
	 private int currentFrame;
	    
	    private List<AnimatedFrame> frames;

	    private List<Matrix4f> invJointMatrices;
	    
	    public AnimObject(Mesh[] meshes, List<AnimatedFrame> frames, List<Matrix4f> invJointMatrices) {
	        super(meshes);
	        this.frames = frames;
	        this.invJointMatrices = invJointMatrices;
	        currentFrame = 0;
	    }

	    public List<AnimatedFrame> getFrames() {
	        return frames;
	    }

	    public void setFrames(List<AnimatedFrame> frames) {
	        this.frames = frames;
	    }
	    
	    public AnimatedFrame getCurrentFrame() {
	        return this.frames.get(currentFrame);
	    }
	    
	    public int getCurrentFrameInt() {
	    	return currentFrame;
	    }
	    
	    public AnimatedFrame getNextFrame(int speed) {
	        int nextFrame = currentFrame + speed;    
	        if ( nextFrame > frames.size() - 1) {
	            nextFrame = 0;
	        }
	        return this.frames.get(nextFrame);
	    }

	    public void nextFrame(int speed) {
	        int nextFrame = currentFrame + speed;    
	        if ( nextFrame > frames.size() - 1) {
	            currentFrame = 0;
	        } else if (nextFrame < 0) {
	        	nextFrame = 0;
	        } else {
	            currentFrame = nextFrame;
	        }
	    }    

	    public List<Matrix4f> getInvJointMatrices() {
	        return invJointMatrices;
	    }
}
