
package Agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Nodo;
import org.eclipse.sumo.libtraci.Vehicle;


public class AutoAgent extends Agent{
    private ArrayList<AID> SumoManagers=new ArrayList<>();
    private String id;//id del agente auto
    private Nodo origen, destino;//nodos del agente
    private String idEdgeActual;//enlace en el que se encuentra el agente
    private Integer capacidad;//capacidad del vehiculo
    private double travelTime,departTime;//tiempos de viaje y departure
    
    protected void setup() {
        id=this.getAID().getLocalName();//se obtiene el ID del autobus
        Object[] args=this.getArguments();
        origen=(Nodo)args[0];
       destino=(Nodo)args[1];
       //capacidad=Integer.parseInt((String) args[2]);
       travelTime=Double.parseDouble((String) args[2]);
       departTime=Double.parseDouble((String) args[3]);
       idEdgeActual=origen.getEnlacesOut()[0];
        
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
                //System.out.println("Recibidio inform step()");
                /*Evaluar el entorno del auto en busca de cambios*/
                SimulationInfoMsg msgInfoSumo=new SimulationInfoMsg(id,destino.getEnlacesIn()[0] ,"Auto" , idEdgeActual,null);
                
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
                //System.out.println("Recibidido");
                if (!rsp.idEnlaceNuevo.equals(idEdgeActual)) {//Cambio de enlace
                    double newTravelTime=travelTime-(rsp.currentTime-departTime);
                    if(newTravelTime>rsp.travelTime){
                        System.out.println("Vehiculo "+id+" reenrutado");
                        /*Pendiente mandar comando de reenrutado*/
                        //Vehicle.setRoute(id, a.getEdges());
                    }                
                }
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void handleRefuse(ACLMessage refuse) {
        }

        @Override
        protected void handleFailure(ACLMessage failure) {
        
        }

        @Override
        protected void handleAllResultNotifications(Vector resultNotifications) {
        
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
        System.out.println(id+"Vehiculo acabo su ruta");
    }
    
    
}
