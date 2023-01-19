package Agentes;

import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.MessageQueue;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
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
import jade.domain.persistence.DeleteAgent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;
import java.io.IOException;
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
        getContentManager().registerOntology(JADEManagementOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());
        MessageTemplate template = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)); //Plantilla de recepción de mensajes
        this.setQueueSize(100);

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
                    /*Eliminar vehiculos que han terminado su ruta*/
                    TraCIResults Res = Simulation.getSubscriptionResults();
                    StringVector v = new StringVector(Res.get(0x7a).getString().replace('[', ' ').replace(']', ' ').trim().split(","));
                    doDeleteVehiculos(v);
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

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private void doDeleteVehiculos(StringVector v) {
        for (AID i : vehiculos) {
            String name = i.getLocalName();
            if (v.contains(name)) {
                DeleteAgent dA = new DeleteAgent();
                dA.setAgent(i);
                Action actExpr = new Action(getAMS(), dA);
                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                request.addReceiver(getAMS());
                request.setOntology(JADEManagementOntology.getInstance().getName());
                try {
                    getContentManager().fillContent(request, actExpr);
                    /*addBehaviour(new AchieveREInitiator(this, request) {
                        protected void handleInform(ACLMessage inform) {
                            System.out.println("Agent successfully created");
                        }

                        protected void handleFailure(ACLMessage failure) {
                            System.out.println("Error creating agent.");
                        }
                    });*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

        protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
            ACLMessage inform = request.createReply();
            try {
                SimulationInfoMsg msg = (SimulationInfoMsg) request.getContentObject();
                inform.setPerformative(ACLMessage.INFORM);
                inform.setContentObject(getResultados(msg));
                //System.out.println("Respondido");
                return inform;
            } catch (UnreadableException ex) {
                ex.printStackTrace();
                throw new FailureException("unexpected-error");
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new FailureException("unexpected-error");
            }
        }

        private SimulationInfoResponse getResultados(SimulationInfoMsg msg) {
            SimulationInfoResponse rsp;
            TraCIResults Res = Simulation.getSubscriptionResults();
            TraCIResults resu = Vehicle.getSubscriptionResults(msg.idVehiculo);
            double currentTime = Double.parseDouble(Res.get(0x66).getString());//Tiempo actual de simulación
            String idEdgeNuevo = resu.get(0x50).getString();//Enlace en el que se encuentra el vehiculo
            if (!msg.idEnlaceActual.equals(idEdgeNuevo) && idEdgeNuevo.contains("E")) {
                TraCIStage a = Simulation.findRoute(idEdgeNuevo, msg.idEnlaceDestino, msg.typeVehicle, 0, libtraci.getROUTING_MODE_AGGREGATED());
                double TravelTime = a.getTravelTime();
                StringVector EdgesRuta = a.getEdges();
                rsp = new SimulationInfoResponse(currentTime, TravelTime, idEdgeNuevo, EdgesRuta.toArray());
            } else {
                rsp = new SimulationInfoResponse(currentTime, -1, idEdgeNuevo, null);
            }
            return rsp;
        }

    }

}
