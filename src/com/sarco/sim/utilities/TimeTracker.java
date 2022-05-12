package com.sarco.sim.utilities;

public class TimeTracker {
	
	double lastLoop;
	double timeStarted;
    
	public double getSystemTime() {
	    return System.nanoTime() / 1000000000.0;
	}

	public TimeTracker() {
	    lastLoop = getSystemTime();
	    timeStarted = getSystemTime();
	}
	
	public float getTimeSince() {
	    double time = getSystemTime();
	    float timeSince = (float) (time - lastLoop);
	    lastLoop = time;
	    return timeSince;
	}
	
	public double getLastLoop() {
		return lastLoop;
	}
	
	public double getTimeElapsed() {
		return getSystemTime() - timeStarted;
	}
}

