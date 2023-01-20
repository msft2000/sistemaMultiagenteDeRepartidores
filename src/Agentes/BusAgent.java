
package Agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
import main.Nodo;

public class BusAgent extends Agent{
    
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
        capacidad=Integer.parseInt((String) args[2]);
        travelTime=Double.parseDouble((String) args[3]);
        departTime=Double.parseDouble((String) args[4]);
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
                /*Evaluar el entorno del auto en busca de cambios*/
                SimulationInfoMsg msgInfoSumo=new SimulationInfoMsg(id,destino.getEnlacesIn()[0] ,"Auto" , idEdgeActual,travelTime,departTime);
                
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
                    if(travelTime!=rsp.travelTime) {
                        System.out.println(id+" | Vehiculo reenrutado");//en caso de haber sido reenrutado  
                        travelTime=rsp.travelTime;//Se actualiza el travel tiem
                    }
                }
            } catch (UnreadableException ex) {
            }
        }

        @Override
        protected void handleFailure(ACLMessage failure) {
            if(!failure.getContent().equals("Esperar")) doDelete();
        } 
    }

    @Override
    protected void takeDown() {
        try {DFService.deregister(this);}
	catch (FIPAException fe) {}
        System.out.println(id+" | Vehiculo acabo su ruta");
    }
    
}
