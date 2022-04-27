package com.sarco.sim.utilities;

public class Timer {
	
	float timeCount;
	double lastLoopTime;
    
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
}
