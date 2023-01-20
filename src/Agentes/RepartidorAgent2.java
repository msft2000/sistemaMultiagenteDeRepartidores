
package Agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import main.Nodo;
import main.Repartidor;
import org.eclipse.sumo.libtraci.Simulation;
import org.eclipse.sumo.libtraci.TraCIStage;
import org.eclipse.sumo.libtraci.Vehicle;
import org.eclipse.sumo.libtraci.libtraci;


public class RepartidorAgent2 extends Agent{
    private final ArrayList<AID> SumoManagers=new ArrayList<>();
    private String id;//id del agente auto
    private Nodo origen, destino;//nodos del agente
    private String idEdgeActual;//enlace en el que se encuentra el agente
    private double travelTime,departTime;//tiempos de viaje y departure
    private Repartidor repartidor;
    private HashMap<Nodo,ArrayList<Nodo>> viajes;
    private Boolean retorno=true;//true - el repartidor retorna a origen - false - el repartidor analiza origen en busca de destinos caso contrario cambia de origen
    
    protected void setup() {
        id=this.getAID().getLocalName();//se obtiene el ID del autobus
        Object[] args=this.getArguments();
        origen=(Nodo)args[0];
        destino=(Nodo)args[1];
        travelTime=Double.parseDouble((String) args[2]);
        departTime=Double.parseDouble((String) args[3]);
        repartidor=(Repartidor)args[4];
        idEdgeActual=origen.getEnlacesOut()[0];
        viajes=repartidor.getEntregasPendientes();
        
        
        //One Shot Behaviour para encontrar el agente SumoAgent
        addBehaviour(new CyclicBehaviour() { 
            public void action() {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("SumoManager");//Agentes SumoManager
                template.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent,template);
                    SumoManagers.clear();
                    for (int i = 0; i < result.length; ++i) {
                        SumoManagers.add(result[i].getName());//se agrega el AID del agente SumoManager
                    }
                }catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        });
        
        /*Behaviour FIPA REQUEST para procesar detención de vehiculo y cambio de ruta*/
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)); //Plantilla de recepción de mensajes
        
        /*addBehaviour(new AchieveREResponder(this,template){
            @Override
            protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
                return null;
            }

            @Override
            protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
                ACLMessage inform = request.createReply();
                if (viajes.isEmpty()) doDelete();//si el repartidor no tiene más viajes se descarta
                if(retorno){
                    TraCIStage ruta=Simulation.findRoute(idEdgeActual,origen.getEnlacesIn()[0], repartidor.getTipoRepartidor(), 0,libtraci.getROUTING_MODE_AGGREGATED());
                    travelTime=ruta.getTravelTime();
                    Vehicle.setRoute(id, ruta.getEdges());
                    Vehicle.setStop(id, origen.getEnlacesIn()[0],-1);
                    Vehicle.resume(id);
                    retorno=false;
                }
                else{
                    ArrayList<Nodo> destinos=viajes.get(origen);
                    if(destinos.size()>0){//Todavia hay viajes desde el origen
                        destino=destinos.get(0);
                        TraCIStage ruta=Simulation.findRoute(idEdgeActual,destino.getEnlacesIn()[0], repartidor.getTipoRepartidor(), 0,libtraci.getROUTING_MODE_AGGREGATED());
                        travelTime=ruta.getTravelTime();
                        destinos.remove(destino);
                        Vehicle.setRoute(id, ruta.getEdges());
                        Vehicle.setStop(id, destino.getEnlacesIn()[0],-1);
                        Vehicle.resume(id);
                        retorno=true;
                    }
                    else{
                        viajes.remove(origen);//se han completado los viajes en el origen
                        destino=(Nodo)viajes.keySet().toArray()[0];
                        origen=destino;
                        TraCIStage ruta=Simulation.findRoute(idEdgeActual,destino.getEnlacesIn()[0], repartidor.getTipoRepartidor(), 0,libtraci.getROUTING_MODE_AGGREGATED());
                        travelTime=ruta.getTravelTime();
                        Vehicle.setStop(id, destino.getEnlacesIn()[0],-1);
                        Vehicle.resume(id);
                        retorno=false;
                    }
                            
                }
                    
                return inform;
            }
            
        });*/
        /**---------------------------------------------------------------------------*/
        addBehaviour(new GetInfoSimulacion());
        /*---------------------------------------------------------------------------------------------------*/
        /*Registrar los vehiculos en el DF*/
        DFAgentDescription dfd = new DFAgentDescription();
	dfd.setName(getAID());
	ServiceDescription sd = new ServiceDescription();
	sd.setType("Vehiculos");
	sd.setName(getLocalName()+"-Vehiculos");
	dfd.addServices(sd);
	try {
            DFService.register(this, dfd);
	}
	catch (FIPAException fe) {
            fe.printStackTrace();
	}  
        
    }
    
    private class GetInfoSimulacion extends CyclicBehaviour{
        private MessageTemplate mt=MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF);//Inform de que se dio un step en la simulación
        public void action() {
            ACLMessage msg=myAgent.receive(mt);
            if(msg!=null){ //se dio un step en la simulación
                /*Evaluar el entorno del auto en busca de cambios*/
                SimulationInfoMsg msgInfoSumo=new SimulationInfoMsg(id,destino.getEnlacesIn()[0] ,"Auto" , idEdgeActual,travelTime,departTime,null);
                
                /*Protocolo de Comunicación FIPA REquest Respond*/
                ACLMessage rqs = new ACLMessage(ACLMessage.REQUEST);
  		for (AID i:SumoManagers) {//Se agregan los SumoManagers que prosesan los datos
                    rqs.addReceiver(i);
  		}
                rqs.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);//Se establece el protocolo
                try {
                    rqs.setContentObject(msgInfoSumo);//Se envia el objeto con los elementos a procesar
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                
                addBehaviour(new ProcesarInfoSumo(myAgent,rqs));//Se espera la respuesta
                
            }
            else{
                block();
            }
        }
        
    }
    private class ProcesarInfoSumo extends AchieveREInitiator{

        public ProcesarInfoSumo(Agent a,ACLMessage msg) {
            super(a, msg);
        }
        @Override
        protected void handleInform(ACLMessage inform) {
            try {
                SimulationInfoResponse rsp=(SimulationInfoResponse)inform.getContentObject();
                if (!rsp.idEnlaceNuevo.equals(idEdgeActual)) {//Cambio de enlace
                    idEdgeActual=rsp.idEnlaceNuevo;//se actualiza el enlace actual del auto
                    travelTime=rsp.travelTime;//Se actualiza el travel tiem
                    if(travelTime!=rsp.travelTime) System.out.println("Vehiculo "+id+" reenrutado");   //en caso de haber sido reenrutado            
                }
            } catch (UnreadableException ex) {
            }
        }
        
        @Override
        protected void handleFailure(ACLMessage failure) {
            if(!failure.getContent().equals("Esperar")) doDelete();
            //else myAgent.doWait(10);
        } 

        @Override
        protected void handleRefuse(ACLMessage refuse) {
            
        }

        
        
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
	}
	catch (FIPAException fe) {
            fe.printStackTrace();
	}
        System.out.println(id+" Repartidor a finalizado su viaje");
    }
    
    
}
