package com.sarco.sim.utilities;

public class TimeTracker {
	
	double lastLoop;
	double timeStarted;
    
	public double getSystemTime() {
		// get system time so that 1f is equal to 1 second
	    return System.nanoTime() / 1000000000.0;
	}

	public TimeTracker() {
	    lastLoop = getSystemTime();
	    timeStarted = getSystemTime();
	}
	
	public float getTimeSince() {
		// get time since this function was last called
	    double time = getSystemTime();
	    float timeSince = (float) (time - lastLoop);
	    lastLoop = time;
	    return timeSince;
	}
	
	public double getLastLoop() {
		// get time getTimeSince was last run
		return lastLoop;
	}
	
	public double getTimeElapsed() {
		// get time since program started
		return getSystemTime() - timeStarted;
	}
}

