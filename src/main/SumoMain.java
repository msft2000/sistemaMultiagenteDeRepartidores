
package main;

import org.eclipse.sumo.libtraci.Simulation;
import org.eclipse.sumo.libtraci.StringVector;


public class SumoMain {
    public static void main(String[] args) {
        System.loadLibrary("libtracijni");
        Simulation.start(new StringVector(new String[] {"sumo-gui", "-c", "mapa3.sumocfg.xml"}));
        Simulation.step();
        BusManager b=BusManager.getInstance();
        while(true){
            Simulation.step();
        }
    }
}
