package com.sarco.sim;

public class Main {
	
	public static void main(String[] args) {
		try {
			Simulation sim = new Simulation();
			sim.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
