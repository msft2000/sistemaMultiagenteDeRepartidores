package main;

import Matrices.MatrizOD;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import java.util.ArrayList;
import org.eclipse.sumo.libtraci.*;

public class SumoMain extends Agent {
    //private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    private static SumoMain instance;
     
    public static SumoMain getInstance(){
        if(instance==null){
            instance=new SumoMain();
        }
        return instance;
    }

    protected void setup() {
        //Iniciar el simulador SUMO y cargar la configuración del mapa
        instance=this;
        System.loadLibrary("libtracijni");
        Simulation.start(new StringVector(new String[]{"sumo-gui", "-c", "mapa2Way.sumocfg"}));
        int[] co=new int[2];
        co[0]=libtraci.getVAR_ARRIVED_VEHICLES_IDS();
        co[1]=libtraci.getVAR_DEPARTED_VEHICLES_IDS();
        Simulation.subscribe(new IntVector(co));
        //Simulation.step();
        //Registro del lenguaje de contenido y la ontología utilizada para comunicarse con el AMS
        getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL);
        getContentManager().registerOntology(JADEManagementOntology.getInstance());
        
        //Inicialización de los viajes de bus
        //BusManager b = BusManager.getInstance(this);
        MatrizOD busOD = new MatrizOD("matriz.csv");
        addViaje(busOD.getNodosViajeDisponible());
        
        //Se utiliza un Ticketbehaviour para hace que la simulación avance cada segundo
        /*TickerBehaviour tarea=new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                //System.out.println(Vehicle.getIDCount());
                Simulation.step();
            }
        };
        addBehaviour(tbf.wrap(tarea));*/
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                //System.out.println(Vehicle.getIDCount());
                Simulation.step();
                /*for(TraCIResult i : Vehicle.getSubscriptionResults("Bus_J1_J2_0").values()){
                    System.out.println(i.getString());
                }*/
                
            }
        });
    }

    public void addViaje(ArrayList<Nodo> nodos) {//Genera los viajes de bus y los anexa a los agentes
        int cont = 0;
        for (Nodo i : nodos) {
            for (Nodo j : i.getViajesBus().keySet()) {
                String ruta = i.getID() + "_" + j.getID();
                String auto = "Bus" +"_"+ ruta+"_0";
                Route.add(i.getID() + "_" + j.getID(), Simulation.findRoute(i.getEnlacesOut()[0], j.getEnlacesIn()[0]).getEdges());
                Vehicle.add(auto, ruta, "Bus");
                //Vehicle.setParameter(auto, "capacidad", "95");
                //Vehicle.setStop(auto, j.getEnlacesIn()[0], Lane.getLength(j.getEnlacesIn()[0] + "_0"));

                CreateAgent ca = new CreateAgent();
                ca.setAgentName(auto);
                ca.setClassName("main.BusAgent");
                ca.setContainer(new ContainerID("Main-Container", null));
                ca.addArguments(i);
                ca.addArguments(j);
                ca.addArguments("95");
                Action actExpr = new Action(getAMS(), ca);
                ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                request.addReceiver(getAMS());
                request.setOntology(JADEManagementOntology.getInstance().getName());
                request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
                request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
                try {
                    getContentManager().fillContent(request, actExpr);
                    addBehaviour(new AchieveREInitiator(this, request) {
                        protected void handleInform(ACLMessage inform) {
                            System.out.println("Agent successfully created");
                            
                        }
                        protected void handleFailure(ACLMessage failure) {
                            System.out.println("Error creating agent.");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                cont++;
            }
        }

    }
    public void addBus(Nodo i, Nodo j,int a){ 
        String ruta = i.getID() + "_" + j.getID();
        String id="Bus_"+ruta+"_"+a;
        if(Route.getIDList().contains(ruta)) ruta=ruta+"_"+id.hashCode();
        Route.add(ruta, Simulation.findRoute(i.getEnlacesOut()[0], j.getEnlacesIn()[0]).getEdges());
        Vehicle.add(id, ruta, "Bus");
        Vehicle.setParameter(id, "capacidad", "95");
        CreateAgent ca = new CreateAgent();
        ca.setAgentName(id);
        ca.setClassName("main.BusAgent");
        ca.setContainer(new ContainerID("Main-Container", null));
        ca.addArguments(i);
        ca.addArguments(j);
        ca.addArguments(Integer.getInteger("95"));
        Action actExpr = new Action(getAMS(), ca);
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(getAMS());
        request.setOntology(JADEManagementOntology.getInstance().getName());
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        try {
            getContentManager().fillContent(request, actExpr);
            addBehaviour(new AchieveREInitiator(this, request) {
                protected void handleInform(ACLMessage inform) {
                    System.out.println("Agent successfully created");
                }

                protected void handleFailure(ACLMessage failure) {
                    System.out.println("Error creating agent.");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    

    /*
    public static void main(String[] args) {
        System.loadLibrary("libtracijni");
        Simulation.start(new StringVector(new String[] {"sumo-gui", "-c", "mapa2Way.sumocfg"}));
        Simulation.step();
        BusManager b=BusManager.getInstance();
        while(true){
            Simulation.step();
        }
    }*/
}
