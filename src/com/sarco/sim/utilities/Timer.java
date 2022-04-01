package com.sarco.sim.utilities;

public class Timer {
	
	float timeCount;
	double lastLoopTime;
	int fps;
	int fpsCount;
	int ups;
	int upsCount;
    
	public double getTime() {
	    return System.nanoTime() / 1000000000.0;
	}

	public void init() {
	    lastLoopTime = getTime();
	}
	
	public float getDelta() {
	    double time = getTime();
	    float delta = (float) (time - lastLoopTime);
	    lastLoopTime = time;
	    timeCount += delta;
	    return delta;
	}
	
	public void updateFPS() {
	    fpsCount++;
	}

	public void updateUPS() {
	    upsCount++;
	}
	
	public void update() {
	    if (timeCount > 1f) {
	        fps = fpsCount;
	        fpsCount = 0;

	        ups = upsCount;
	        upsCount = 0;

	        timeCount -= 1f;
	    }
	}
}
