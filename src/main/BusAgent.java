
package main;

import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import jade.wrapper.StaleProxyException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.sumo.libtraci.Route;
import org.eclipse.sumo.libtraci.Simulation;
import org.eclipse.sumo.libtraci.StringVector;
import org.eclipse.sumo.libtraci.TraCIResults;
import org.eclipse.sumo.libtraci.Vehicle;
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
                else{
                    int routeIndex=Vehicle.getRouteIndex(id);
                    if(routeIndex>=0) System.out.println(Route.getEdges(Vehicle.getRouteID(id)).get(routeIndex));
                }
                    
            }
            
        };
        addBehaviour(b);
    }

    protected void takeDown() {
        try {
            //System.out.println(Vehicle.);
            getContainerController().kill();
        } catch (StaleProxyException ex) {
            Logger.getLogger(BusAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    
    
}
