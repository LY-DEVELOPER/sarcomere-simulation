package com.sarco.sim;

import com.sarco.sim.*;

public class Simulation implements Runnable{
	
	 public static final int TARGET_FPS = 75;

	    public static final int TARGET_UPS = 30;

	    private final Window window;

	    private final Timer timer;

	    private final ISimLogic simLogic;

	    public Simulation(String windowTitle, int width, int height, boolean vSync, ISimLogic simLogic) throws Exception {
	        window = new Window(windowTitle, width, height, vSync);
	        this.simLogic = simLogic;
	        timer = new Timer();
	    }

	    @Override
	    public void run() {
	        try {
	            init();
	            simLoop();
	        } catch (Exception excp) {
	            excp.printStackTrace();
	        }
	    }

	    protected void init() throws Exception {
	        window.init();
	        timer.init();
	        simLogic.init();
	    }

	    protected void simLoop() {
	        float elapsedTime;
	        float accumulator = 0f;
	        float interval = 1f / TARGET_UPS;

	        boolean running = true;
	        while (running && !window.windowShouldClose()) {
	            elapsedTime = timer.getElapsedTime();
	            accumulator += elapsedTime;

	            input();

	            while (accumulator >= interval) {
	                update(interval);
	                accumulator -= interval;
	            }

	            render();

	            if (!window.isvSync()) {
	                sync();
	            }
	        }
	    }

	    private void sync() {
	        float loopSlot = 1f / TARGET_FPS;
	        double endTime = timer.getLastLoopTime() + loopSlot;
	        while (timer.getTime() < endTime) {
	            try {
	                Thread.sleep(1);
	            } catch (InterruptedException ie) {
	            }
	        }
	    }

	    protected void input() {
	        simLogic.input(window);
	    }

	    protected void update(float interval) {
	        simLogic.update(interval);
	    }

	    protected void render() {
	        simLogic.render(window);
	        window.update();
	    }
	}
