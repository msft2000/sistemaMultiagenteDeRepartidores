
package Agentes;

import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import main.Nodo;
import org.eclipse.sumo.libtraci.Simulation;
import org.eclipse.sumo.libtraci.StringVector;
import org.eclipse.sumo.libtraci.TraCIResults;
import org.eclipse.sumo.libtraci.TraCIStage;
import org.eclipse.sumo.libtraci.Vehicle;
import org.eclipse.sumo.libtraci.libtraci;


public class BusAgent extends Agent{
    private String id;
    private Nodo origen, destino;
    private String idEdgeActual;
    private Integer capacidad;
    private double travelTime,departTime;
    private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    
    protected void setup() {
        id=this.getAID().getLocalName();//se obtiene el ID del autobus
        Object[] args=this.getArguments();
        origen=(Nodo)args[0];
        destino=(Nodo)args[1];
        capacidad=Integer.parseInt((String) args[2]);
        travelTime=Double.parseDouble((String) args[3]);
        departTime=Double.parseDouble((String) args[4]);
        idEdgeActual=origen.getEnlacesOut()[0];
        TickerBehaviour b=new TickerBehaviour(this, 70) {
            @Override
            protected void onTick() {
                //0x7a: id arrived vehicless
                //0x66: current simulation time    
                TraCIResults Res=Simulation.getSubscriptionResults();
                StringVector v=new StringVector(Res.get(0x7a).getString().replace('[', ' ').replace(']', ' ').trim().split(","));

                /*if(v.contains(id)){
                    int numPasajerosRestantes=origen.getValueViaje(destino);
                    int val = ((numPasajerosRestantes<=capacidad) ? 0 : numPasajerosRestantes-capacidad);
                    origen.setViajesBus(destino, val);
                    doDelete();
                } */
                /*else{
                    //0x50: Llega a un enlace nuevo
                    TraCIResults resu=Vehicle.getSubscriptionResults(id);
                    if(resu.containsKey(0x50)){
                        String idEdgeNuevo=resu.get(0x50).getString();
                        if(!idEdgeActual.equals(idEdgeNuevo)&& idEdgeNuevo.contains("E")){
                            idEdgeActual=idEdgeNuevo;
                            try{
                                double currentTime=Double.parseDouble(Res.get(0x66).getString());
                                TraCIStage a=Simulation.findRoute(idEdgeActual, destino.getEnlacesIn()[0],"Bus",0,libtraci.getROUTING_MODE_AGGREGATED());
                                double newTravelTime=travelTime-(currentTime-departTime-10);
                                if(newTravelTime>a.getTravelTime()){
                                    System.out.println("Vehiculo "+id+" reenrutado");
                                    Vehicle.setRoute(id, a.getEdges());
                                }
                            }
                            catch(Exception e){
                                System.out.println("Error: "+id+" "+idEdgeActual);
                                System.err.println(e.getMessage());
                            }
                        }
                    }
                        /*
                        if(!idEdgeActual.equals(idEdgeNuevo)&& !idEdgeActual.contains("E")){
                            //String idRuta=resu.get(0x53).getString();
                            double currentTime=Double.parseDouble(Res.get(0x66).getString());
                            idEdgeActual=idEdgeNuevo;
                            TraCIStage a=Simulation.findRoute(idEdgeActual, destino.getEnlacesIn()[0]);
                            double newTravelTime=travelTime-(currentTime-departTime-10);
                            if(newTravelTime>a.getTravelTime()){
                                System.out.println("Vehiculo "+id+" reenrutado");
                                Vehicle.setRoute(id, a.getEdges());
                            }
                        } */
                    //}

                    //System.out.printf("%s_%s Tiempo viaje: %s\nidRuta: %s\nidEdgeActual: %s\ntiempoRecorrido: %s\n",origen.getID(),destino.getID(),travelTime+"",idRuta,idEdgeActual,(currentTime-departTime-10)+"");
                //}
            }
            
        };
        //addBehaviour(b);
    }

    protected void takeDown() {
       
    }
    
    
    
}
