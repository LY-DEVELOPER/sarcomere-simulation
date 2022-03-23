package com.sarco.sim;

public class main {

	public static void main(String[] args) {
        try {
            boolean vSync = true;
            ISimLogic simLogic = new SarcoSim();
            Simulation simulation = new Simulation("Sarcomere Simulation", 600, 480, vSync, simLogic);
            simulation.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }

}