
package main;

import Matrices.MatrizOD;
import java.util.ArrayList;
import org.eclipse.sumo.libtraci.Vehicle;
import org.eclipse.sumo.libtraci.Route;
import org.eclipse.sumo.libtraci.Simulation;

public final class BusManager{//Agente encargado de generar los agentes de buses y sus viajes en base a su matriz OD
    private static BusManager instance;
    private MatrizOD busOD;
    private ArrayList<Nodo> nodos;
    private BusManager() {
        busOD=new MatrizOD("matriz.csv");
        nodos=busOD.getNodosViajeDisponible();
        int cont=0;
        for(Nodo i: nodos){
            for(Nodo j: i.getViajesBus().keySet()){
                String auto="B"+cont;
                String ruta=i.getID()+"_"+j.getID();
                Route.add(i.getID()+"_"+j.getID(), Simulation.findRoute(i.getEnlacesOut()[0], j.getEnlacesIn()[1]).getEdges());
                Vehicle.add(auto,ruta,"Bus");
                cont++;
            }
        }
    }
    public static BusManager getInstance(){
        if(instance==null){
            instance=new BusManager();
        }
        return instance;
    }
    
    
    

}
