
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



public class RepartidorAgent extends Agent{
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
        
        /*Espera mensje de actualización de tiempo de viaje*/
        addBehaviour(new CyclicBehaviour() {
            private MessageTemplate tmp=MessageTemplate.MatchPerformative(ACLMessage.INFORM_IF);
            @Override
            public void action() {
                ACLMessage msg=myAgent.receive(tmp);
                if(msg!=null){
                    travelTime=Double.parseDouble(msg.getContent());  
                }
                else{
                    block();
                }
            }
        });
        
        
        /*Behaviour FIPA REQUEST para procesar detención de vehiculo y cambio de ruta*/
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST_WHEN),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST_WHEN)); //Plantilla de recepción de mensajes
        
        addBehaviour(new AchieveREResponder(this,template){/*Procesa el cambio de destinos*/
            @Override
            protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
                return null;
            }

            @Override
            protected ACLMessage handleRequest(ACLMessage request) throws NotUnderstoodException, RefuseException {
                return null;
            }
            
            @Override
            protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
                ACLMessage inform = request.createReply();
                if (viajes.isEmpty()) {
                    doDelete();
                }//si el repartidor no tiene más viajes se descarta
                if(retorno){
                    System.out.println(id+": Vuelta - "+idEdgeActual+"-"+origen.getEnlacesIn()[0]);
                    //inform.setContent(id+";"+idEdgeActual+";"+origen.getEnlacesIn()[0]+";"+repartidor.getTipoRepartidor()+";F");
                    if(viajes.size()==1 && viajes.get(origen).isEmpty()==true) inform.setContent(id+";"+idEdgeActual+";"+origen.getEnlacesIn()[0]+";"+repartidor.getTipoRepartidor()+";T");
                    else inform.setContent(id+";"+idEdgeActual+";"+origen.getEnlacesIn()[0]+";"+repartidor.getTipoRepartidor()+";F");
                    
                    retorno=false;
                }
                else{
                    ArrayList<Nodo> destinos=viajes.get(origen);
                    if(!destinos.isEmpty()){//Todavia hay viajes desde el origen
                        destino=destinos.get(0);
                        destinos.remove(destino);
                        System.out.println(id+": IDA - "+idEdgeActual+"-"+destino.getEnlacesIn()[0]);
                        inform.setContent(id+";"+idEdgeActual+";"+destino.getEnlacesIn()[0]+";"+repartidor.getTipoRepartidor()+";F");
                        retorno=true;
                    }
                    else{
                        viajes.remove(origen);//se han completado los viajes en el origen
                        if(viajes.isEmpty()){
                            doDelete();
                        }  
                        else{
                            
                            destino=(Nodo)viajes.keySet().toArray()[0];
                            System.out.println(id+": IDA - "+idEdgeActual+"-"+destino.getEnlacesIn()[0]);
                            origen=destino;
                            inform.setContent(id+";"+idEdgeActual+";"+destino.getEnlacesIn()[0]+";"+repartidor.getTipoRepartidor()+";F");
                            retorno=false;
                        }
                        
                    }        
                }
                inform.setPerformative(ACLMessage.INFORM);
                return inform;
            }
  
        });
        
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
	catch (FIPAException fe) {}  
        
    }
    
    private class GetInfoSimulacion extends CyclicBehaviour{
        private MessageTemplate mt=MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF);//Inform de que se dio un step en la simulación
        public void action() {
            ACLMessage msg=myAgent.receive(mt);
            if(msg!=null){ //se dio un step en la simulación
                /*Evaluar el entorno del auto en busca de cambios*/
                SimulationInfoMsg msgInfoSumo=new SimulationInfoMsg(id,destino.getEnlacesIn()[0] ,repartidor.tipoRepartidor , idEdgeActual,travelTime,departTime);
                
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
            //System.out.println("Falla ----");
        } 

        @Override
        protected void handleRefuse(ACLMessage refuse) {
            //System.out.println("Falla ----");
        }

        
        
    }

    @Override
    protected void takeDown() {
        try {DFService.deregister(this);}
	catch (FIPAException fe) {}
        System.out.println(id+" Repartidor a finalizado su viaje");
    }
    
    
}
