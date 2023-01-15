package main;

import Matrices.MatrizOD;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.sumo.libtraci.*;

public class SumoMain {

    //private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    private static SumoMain instance;

    public SumoMain() {
        //Iniciar el simulador SUMO y cargar la configuración del mapa
        /*try{java.lang.Runtime.getRuntime().exec(new String[]{"java","jade.Boot","-gui"});}
        catch(Exception e){}*/
        System.loadLibrary("libtracijni");
        Simulation.start(new StringVector(new String[]{"sumo-gui", "-c", "mapa2Way.sumocfg"}));
        int[] co = new int[2];
        co[0] = libtraci.getVAR_ARRIVED_VEHICLES_IDS();
        co[1] = libtraci.getVAR_DEPARTED_VEHICLES_IDS();
        Simulation.subscribe(new IntVector(co));
        MatrizOD busOD = new MatrizOD("matriz.csv");
        addViajeBus(busOD.getNodosViajeDisponible());

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Código a ejecutar cada segundo
                Simulation.step();
            }
        }, 0, 1000);

    }

    public static SumoMain getInstance() {
        if (instance == null) {
            instance = new SumoMain();
        }
        return instance;
    }

    private void addViajeBus(ArrayList<Nodo> nodos) {//Genera los viajes de bus y los anexa a los agentes
        for (Nodo i : nodos) {
            for (Nodo j : i.getViajesBus().keySet()) {
                /*Asignación de ruta y caracteristicas al Bus*/
                addViajeBus(i, j);
            }
        }
    }

    public void addViajeBus(Nodo origen, Nodo destino) {
        int salt = java.time.LocalDateTime.now().hashCode();
        String ruta = origen.getID() + "_" + destino.getID() + "_" + salt;
        String id = "Bus_" + ruta;
        addSimulacionVehiculo(id, ruta, origen.getEnlacesOut()[0], destino.getEnlacesIn()[0], "Bus", "95");//Agrega el vehiculo a la simulación
        addAgenteVehiculo(id, "main.BusAgent", origen, destino, 95);//anexa el agente al vehiculo
     }

    private void addAgenteVehiculo(String idAgente, String classAgent, Nodo origen, Nodo destino, int capacidad) {
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "1099");
        p.setParameter(Profile.CONTAINER_NAME, "Main-Container");
        ContainerController cc = rt.createAgentContainer(p);
        if (cc != null) {
            try {
                AgentController ac = cc.createNewAgent(idAgente,classAgent, new Object[]{origen,destino,capacidad+""});
                ac.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addSimulacionVehiculo(String idVehiculo,String idRuta,String idOrigen,String idDestino,String tipo, String capacidad){ 
        Route.add(idRuta, Simulation.findRoute(idOrigen, idDestino).getEdges());
        Vehicle.add(idVehiculo, idRuta, tipo);
        Vehicle.setParameter(idVehiculo, "capacidad", capacidad);
        Vehicle.subscribeContext(idVehiculo,0xaa, 100,new IntVector(new int[]{0x7a,0x5a}));
        //https://sumo.dlr.de/docs/TraCI/Object_Context_Subscription.html
        //https://sumo.dlr.de/docs/TraCI/Simulation_Value_Retrieval.html
        
    }
    
}
