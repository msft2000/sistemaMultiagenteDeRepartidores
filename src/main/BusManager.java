
package main;

import Matrices.MatrizOD;
import java.util.ArrayList;


public final class BusManager{//Agente encargado de generar los agentes de buses y sus viajes en base a su matriz OD
    private static BusManager instance;
    private MatrizOD busOD;
    private ArrayList<Nodo> nodos;
    private static SumoMain Sumo;
    private BusManager() {
        busOD=new MatrizOD("matriz.csv");
        nodos=busOD.getNodosViajeDisponible();
        
        IniciarViajes();
    }
    public static BusManager getInstance(SumoMain in){
        if(instance==null){
            Sumo=in;
            instance=new BusManager();
        }
        return instance;
    }
    
    public void IniciarViajes(){
        Sumo.addViaje(nodos);
    }
    
    
    
    

}
