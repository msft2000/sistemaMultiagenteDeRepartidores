
package main;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import java.util.Map;
import java.util.Vector;
import org.eclipse.sumo.libtraci.Simulation;
import org.eclipse.sumo.libtraci.StringVector;
import org.eclipse.sumo.libtraci.TraCIResults;
import org.eclipse.sumo.libtraci.libtraci;


public class BusAgent extends Agent{
    private String id;
    private Nodo origen, destino;
    private Integer capacidad;
    private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    
    protected void setup() {
        id=this.getAID().getLocalName();//se obtiene el ID del autobus
        Object[] args=this.getArguments();
        origen=(Nodo)args[0];
        destino=(Nodo)args[1];
        capacidad=Integer.parseInt((String) args[2]);
        
        
        TickerBehaviour b=new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                TraCIResults Res=Simulation.getSubscriptionResults();
                StringVector v=new StringVector(Res.get(libtraci.getVAR_ARRIVED_VEHICLES_IDS()).getString().replace('[', ' ').replace(']', ' ').trim().split(","));
                if(v.contains(id)){
                    int numPasajerosRestantes=origen.getValueViaje(destino);
                    int val = ((numPasajerosRestantes<=capacidad) ? 0 : numPasajerosRestantes-capacidad);
                    //System.out.println("Arrived : "+id);
                    origen.setViajesBus(destino, val);
                    doDelete();
                } 
                    
            }
            
        };
        /*addBehaviour(tbf.wrap(new CyclicBehaviour(this) {
            @Override
            public void action() {
                for(TraCIResult i : Simulation.getSubscriptionResults().values()){
                    System.out.println("Agente "+id+": "+i.getString());
                }
            }
        }));*/
        addBehaviour(b);
    }

    protected void takeDown() {
        //System.out.println(Vehicle.);
       
    }
    
    
    
}
