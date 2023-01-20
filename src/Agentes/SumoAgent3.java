package Agentes;

import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;
import java.util.ArrayList;
import org.eclipse.sumo.libtraci.Lane;
import org.eclipse.sumo.libtraci.Simulation;
import org.eclipse.sumo.libtraci.StringVector;
import org.eclipse.sumo.libtraci.TraCIResults;
import org.eclipse.sumo.libtraci.TraCIStage;
import org.eclipse.sumo.libtraci.Vehicle;
import org.eclipse.sumo.libtraci.libtraci;

public class SumoAgent3 extends Agent {

    ArrayList<AID> vehiculos = new ArrayList<>();
    ArrayList<String> departedVehicles=new ArrayList<>();
    protected void setup() {
        //getContentManager().registerLanguage(codec);
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(JADEManagementOntology.getInstance());
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)); //Plantilla de recepción de mensajes
        this.setQueueSize(10000);

        /*Obtener lista de agentes vehiculo----------------------------------------------------------------*/
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                DFAgentDescription tmp = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Vehiculos");//Agentes Vehiculo
                tmp.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, tmp);
                    vehiculos.clear();
                    for (int i = 0; i < result.length; ++i) {
                        vehiculos.add(result[i].getName());//se agrega el AID del agente SumoManager
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        });
        /*-----------------------------------------------------------------------------------------------*/

        addBehaviour(new GetData(this, template));

        addBehaviour(new TickerBehaviour(this, 100) {
            @Override
            protected void onTick() {
                if (myAgent.getCurQueueSize() == 0) {/*Avanza la simulación solo si la cola de mensajes está vacía*/
                    Simulation.step();
                    /*Se obtienen los autos que se han colocado en la simulación*/
                    departedVehicles.addAll(Simulation.getDepartedIDList());
                    /*Se verifican cuales autos ya han llegado a destino*/
                    StringVector stops=Simulation.getStopStartingVehiclesIDList();//Vehiculos que se han detenido
                    if(!stops.isEmpty())RequestDataCambioRuta(stops);//Procesar el cambio de ruta de los vehiculos detenidos
                   
                    /*--------------Dar aviso a los vehiculos de que la simulación avanzó-------------------*/
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM_REF);
                    for (AID i : vehiculos) {/*Solo los autos en simulación pueden empezar a solicitar información*/
                        if(departedVehicles.contains(i.getLocalName()))msg.addReceiver(i);
                    }
                    msg.setContent("Step()");
                    myAgent.send(msg);
                }
            }
        });

        /*Registro del servicio*/
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("SumoManager");
        sd.setName(getLocalName() + "-SumoManager");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {}
    }
    private void doDeleteVehiculos(StringVector v) {
        for (int i=0;i<vehiculos.size();i++) {
            String name = vehiculos.get(i).getLocalName();
            if (v.contains(name)) {
                vehiculos.remove(i);
            }
        }
    }
    
    private void RequestDataCambioRuta(StringVector v){
        /*Se envia un request a cada agente que se detuvo para solicitar información del nuevo destino*/
        /*Protocolo de Comunicación FIPA REquest Respond*/
        ACLMessage rqs = new ACLMessage(ACLMessage.REQUEST_WHEN);
        for (AID i:vehiculos) {
            if(v.contains(i.getLocalName()))rqs.addReceiver(i);//Se agregan los vehiculos involucrados
        }
        rqs.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST_WHEN);//Se establece el protocolo
        rqs.setContent("RequestDAta");
        addBehaviour(new AchieveREInitiator(this, rqs){
            
            @Override
            protected void handleInform(ACLMessage inform) {
                    String[] info=inform.getContent().split(";");
                    String idVehiculo=(String)info[0];
                    String origenID=(String)info[1];
                    String destinoID=(String)info[2];
                    String vClass=(String)info[3];
                    TraCIStage ruta=Simulation.findRoute(origenID,destinoID, vClass, 0,libtraci.getROUTING_MODE_AGGREGATED());
                    double travelTime=ruta.getTravelTime();
                    Vehicle.setRoute(idVehiculo, ruta.getEdges());
                    if(!info[4].equals("T"))Vehicle.setStop(idVehiculo, destinoID,Lane.getLength(destinoID+"_0"));
                    Vehicle.resume(idVehiculo);
                    ACLMessage response=inform.createReply();
                    response.setPerformative(ACLMessage.INFORM_IF);//se va a indicar al agente que actualice su información
                    //response.setProtocol(FIPA_REQUEST_WHEN);
                    response.setContent(travelTime+"");
                    myAgent.send(response);       
            }

            @Override
            protected void handleFailure(ACLMessage failure) {
             
            }
        });//Se espera la respuesta
    
    
    
    
    }
    
    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private class GetData extends AchieveREResponder {

        public GetData(Agent a, MessageTemplate mt) {
            super(a, mt);
        }

        @Override
        protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
            return null;
        }

        @Override
        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
            ACLMessage inform = request.createReply();
            StringVector Res = Vehicle.getIDList();//Se consulta cuales autos estan en ruta
            if(!Res.contains(request.getSender().getLocalName())){
                throw new FailureException("Auto Salió de la simulación");//Si el auto que envia el msg esta fuera de ruta se elimina
            }
            else if(!departedVehicles.contains(request.getSender().getLocalName())){
                inform.setContent("No Departed");
                return inform;
            }
            try {
                SimulationInfoMsg msg = (SimulationInfoMsg) request.getContentObject();//se recupera el contenido del mensaje
                inform.setPerformative(ACLMessage.INFORM);//se va a enviar un inform
                inform.setContentObject(getResultados(msg));
                return inform;
            } catch (Exception ex) {
                throw new FailureException("Auto Salió de la simulación");
            }
        }

        private SimulationInfoResponse getResultados(SimulationInfoMsg msg) {
            //Se procesa la información y se reenruta o no el auto
            SimulationInfoResponse rsp;
            TraCIResults Res = Simulation.getSubscriptionResults();
            TraCIResults resu = Vehicle.getSubscriptionResults(msg.idVehiculo);
            double currentTime = Double.parseDouble(Res.get(0x66).getString());//Tiempo actual de simulación
            String idEdgeNuevo = resu.get(0x50).getString();//Enlace en el que se encuentra el vehiculo
            if (!msg.idEnlaceActual.equals(idEdgeNuevo) && idEdgeNuevo.contains("E")) {
                TraCIStage a = Simulation.findRoute(idEdgeNuevo, msg.idEnlaceDestino, msg.typeVehicle, 0, libtraci.getROUTING_MODE_AGGREGATED());
                double TravelTime = a.getTravelTime();//tiempo de viaje promedio desde la nueva Edge
                StringVector EdgesRuta = a.getEdges();
                double CurrentTravelTime=msg.travelTime-(currentTime-msg.departTime);
                if(CurrentTravelTime>TravelTime){//El nuevo camino calculado es más rapido
                    Vehicle.setRoute(msg.idVehiculo, EdgesRuta);//Vehiculo reenrutado
                    double NewTravelTime=TravelTime+(currentTime-msg.departTime);
                    rsp = new SimulationInfoResponse(currentTime,NewTravelTime , idEdgeNuevo, EdgesRuta.toArray());
                }
                else rsp = new SimulationInfoResponse(currentTime, msg.travelTime, idEdgeNuevo, EdgesRuta.toArray());
            } else {
                rsp = new SimulationInfoResponse(currentTime, msg.travelTime, msg.idEnlaceActual, null);
            }
            return rsp;
        }

    }

}
