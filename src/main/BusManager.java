
package main;

import Matrices.LectorCSV;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;

public class BusManager extends Agent{//Agente encargado de generar los agentes de buses y sus viajes en base a su matriz OD
   private LectorCSV read;
   public String ODFilePath;//Ruta del archivo OD es cargada 

    public BusManager() {
        
    }

    @Override
    protected void setup() {
        ODFilePath="";
        
        
        addBehaviour(new Behaviour(this) {//Comportamiento para leer el archivo csv cuando se cargue una ruta
            @Override
            public void action() {
                if(ODFilePath!=""){
                    read.getMatrix(ODFilePath);
                }
            }
            
            @Override
            public boolean done(){
                return ODFilePath=="";// La ruta está vacía?
            }
        });
        
        
    }

    @Override
    protected void takeDown() {
    
    }
    
    
   
}
