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
import jade.proto.AchieveREResponder;
import java.util.ArrayList;
import org.eclipse.sumo.libtraci.Simulation;
import org.eclipse.sumo.libtraci.StringVector;
import org.eclipse.sumo.libtraci.TraCIResults;
import org.eclipse.sumo.libtraci.TraCIStage;
import org.eclipse.sumo.libtraci.Vehicle;
import org.eclipse.sumo.libtraci.libtraci;

public class SumoAgent extends Agent {

    ArrayList<AID> vehiculos = new ArrayList<>();

    protected void setup() {
        //getContentManager().registerLanguage(codec);
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(JADEManagementOntology.getInstance());
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)); //Plantilla de recepción de mensajes
        this.setQueueSize(1000);

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
                if (myAgent.getCurQueueSize() == 0) {
                    Simulation.step();
                    //TraCIResults Res = Simulation.getSubscriptionResults();
                    //StringVector v = new StringVector(Res.get(0x7a).getString().replace('[', ' ').replace(']', ' ').trim().split(","));
                    //doDeleteVehiculos(v);
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM_REF);
                    for (AID i : vehiculos) {
                        msg.addReceiver(i);
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
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
    private void doDeleteVehiculos(StringVector v) {
        for (int i=0;i<vehiculos.size();i++) {
            String name = vehiculos.get(i).getLocalName();
            if (v.contains(name)) {
                vehiculos.remove(i);
            }
        }

    }
    
    

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
            /*if (!vehiculos.contains(request.getSender()))throw new FailureException("Auto Salió de la simulación");//Si el auto que envia el msg esta fuera de ruta se elimina
            else if(!Res.contains(request.getSender().getLocalName())&&!vehiculos.contains(request.getSender())){
                throw new FailureException("Auto Salió de la simulación");//Si el auto que envia el msg esta fuera de ruta se elimina
            }
            else if(!Res.contains(request.getSender().getLocalName())&&vehiculos.contains(request.getSender())){
               throw new FailureException("Esperar");
            }*/
            if(!Res.contains(request.getSender().getLocalName())){
                throw new FailureException("Auto Salió de la simulación");//Si el auto que envia el msg esta fuera de ruta se elimina
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
