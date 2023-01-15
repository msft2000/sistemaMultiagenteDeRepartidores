
package main;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import org.eclipse.sumo.libtraci.Simulation;
import org.eclipse.sumo.libtraci.TraCIResult;
import org.eclipse.sumo.libtraci.TraCIResults;
import org.eclipse.sumo.libtraci.TraCIStringList;
import org.eclipse.sumo.libtraci.Vehicle;

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
                try{
                    TraCIResults ssRes = Simulation.getSubscriptionResults();
                    //System.out.println(Simulation.getTime());
                    /*if(Simulation.getArrivedIDList().contains(id)){
                        origen.setViajesBus(destino, origen.getValueViaje(destino)-Integer.parseInt(Vehicle.getParameter(id, "capacidad")));
                        doDelete();
                    }*/
                }
                catch(Exception e){
                    //System.out.println(e.getMessage());
                    /*origen.setViajesBus(destino, origen.getValueViaje(destino)-capacidad);
                    doDelete();*/
                }
                
            }
            
        };
        addBehaviour(tbf.wrap(new CyclicBehaviour(this) {
            @Override
            public void action() {
                for(TraCIResult i : Simulation.getSubscriptionResults().values()){
                    System.out.println("Agente "+id+": "+i.getString());
                }
            }
        }));
        
    }

    protected void takeDown() {
        //System.out.println(Vehicle.);
       
    }
    
    
    
}
