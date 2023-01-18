package main;

import Matrices.MatrizMaster;
import Matrices.MatrizOD;
import VIEW.MAIN;
import jade.Boot;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.sumo.libtraci.*;

public class SumoMain {

    //private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    private static SumoMain instance;
    private jade.core.Runtime jadeRunTime;
    private ContainerController repartidores, autos, buses;
    private int velocidadVehiculos,velocidadBuses,capacidadBuses,velocidadRepartidoresBicicleta,velocidadRepartidoresMoto;
    private MAIN guiMain;

    public SumoMain() {
        guiMain = new MAIN();
        guiMain.setVisible(true);
    }

    public void iniciar(String csvBuses,String csvRepartidores, String csvVehiculos, int velocidadVehiculos, int velocidadBuses, int capacidadBuses, int velocidadRepartidoresBicicleta, int velocidadRepartidoresMoto) {
        this.velocidadVehiculos=velocidadVehiculos;
        this.velocidadBuses=velocidadBuses;
        this.capacidadBuses=capacidadBuses;
        this.velocidadRepartidoresBicicleta=velocidadRepartidoresBicicleta;
        this.velocidadRepartidoresMoto=velocidadRepartidoresMoto;
        Boot.main(new String[]{"-gui"});//iniciación de jade.Boot
        /*
            Instancias para la inicialización de jade y sus agentes
         */
        jadeRunTime = jade.core.Runtime.instance();
        Profile p = new ProfileImpl("localhost", 1099, "MAS-Repartos");//Perfil de los containercontroller
        //Container y perfil de repartidores
        p.setParameter(Profile.CONTAINER_NAME, "Repartidores-Container");
        repartidores = jadeRunTime.createAgentContainer(p);
        //Container y perfil de autos
        p.setParameter(Profile.CONTAINER_NAME, "Autos-Container");
        autos = jadeRunTime.createAgentContainer(p);
        //Container y perfil de buses
        p.setParameter(Profile.CONTAINER_NAME, "Buses-Container");
        buses = jadeRunTime.createAgentContainer(p);
        /*
            Inicialización de SUMO y suscribción de variables
        */
        System.loadLibrary("libtracijni");
        Simulation.start(new StringVector(new String[]{"sumo-gui", "-c", "mapa2Way.sumocfg"}),50109);
        //Simulation.start(new StringVector(new String[]{}),50109);
        int[] co = new int[]{0x7a, 0x66};//Se solicita la información referente a los autos que ya han finalizado sus rutas
        //0x7a: id arrived vehicless
        //0x66: current simulation time
        Simulation.subscribe(new IntVector(co));
        /*Edge.getIDList().forEach((el)->{
            if(((String)el).contains("E")) Edge.subscribe((String) el, new IntVector(new int[]{0x5a})); 
        });*/
 /*
            Carga de las matrices de datos de viajes
         */
        MatrizMaster matVehiculos = new MatrizMaster(csvBuses,csvVehiculos,csvRepartidores);
        addViajeBus(matVehiculos.getNodosViajeDisponible());
        addViajeVehiculo(matVehiculos.getNodosViajeDisponibleVehiculos());

        /*
            Thread encargado de avanzar la simulación cada 1 segundo
         */
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
    }//Singleton

    private void addViajeBus(ArrayList<Nodo> nodos) {//Lee los viajes de bus y los anexa a los agentes
        for (Nodo i : nodos) {
            for (Nodo j : i.getViajesBus().keySet()) {
                /*Asignación de ruta y caracteristicas al Bus*/
                addViajeBus(i, j);
            }
        }
    }
    
    private void addViajeVehiculo(ArrayList<Nodo> nodos) {//Lee los viajes de bus y los anexa a los agentes
        for (Nodo i : nodos) {
            for (Nodo j : i.getViajesVehiculos().keySet()) {
                /*Asignación de ruta y caracteristicas al Bus*/
                addViajeVehiculo(i, j);
            }
        }
    }
    
    public void addViajeBus(Nodo origen, Nodo destino) {//Agrega un bus a la simulación
        int salt = java.time.LocalDateTime.now().hashCode();//Código de bus - hash de la fecha y hora actuales
        String ruta = origen.getID() + "_" + destino.getID() + "_" + salt;//Código de ruta
        String id = "Bus_" + ruta;//Código de bus
        double travelTime = addSimulacionVehiculo(id, ruta, origen.getEnlacesOut()[0], destino.getEnlacesIn()[0], "Bus", this.capacidadBuses+"");//Agrega el vehiculo a la simulación
        addAgenteVehiculo(buses, id, "main.BusAgent", origen, destino, 95, travelTime);//anexa el agente al vehiculo
    }
    
    public void addViajeVehiculo(Nodo origen, Nodo destino) {//Agrega un bus a la simulación
        int salt = java.time.LocalDateTime.now().hashCode();//Código de bus - hash de la fecha y hora actuales
        String ruta = origen.getID() + "_" + destino.getID() + "_" + salt;//Código de ruta
        String id = "Auto_" + ruta;//Código de bus
        double travelTime = addSimulacionVehiculo(id, ruta, origen.getEnlacesOut()[0], destino.getEnlacesIn()[0], "Autos", "95");//Agrega el vehiculo a la simulación
        addAgenteVehiculo(autos, id, "main.VehiculoAgent", origen, destino, 95, travelTime);//anexa el agente al vehiculo
    }

    private void addAgenteVehiculo(ContainerController container, String idAgente, String classAgent, Nodo origen, Nodo destino, int capacidad, double travelTime) {
        if (container != null) {
            try {
                AgentController ac = container.createNewAgent(idAgente, classAgent, new Object[]{origen, destino, capacidad + "", travelTime + "", Simulation.getSubscriptionResults().get(0x66).getString()});
                ac.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
    }
    private double addSimulacionVehiculo(String idVehiculo, String idRuta, String idOrigen, String idDestino, String tipo, String capacidad) {
        TraCIStage ruta = Simulation.findRoute(idOrigen, idDestino, tipo, 0, libtraci.getROUTING_MODE_AGGREGATED());
        Route.add(idRuta, ruta.getEdges());
        Vehicle.add(idVehiculo, idRuta, tipo);
        Vehicle.setParameter(idVehiculo, "capacidad", capacidad);
        //Vehicle.setParameter(idVehiculo, "departTime", Simulation.getSubscriptionResults().get(0x66).getString()+"");
        Vehicle.subscribe(idVehiculo, new IntVector(new int[]{0x50, 0x53}));
        return ruta.getTravelTime();
        //Vehicle.subscribeContext(idVehiculo,0xaa, 100,new IntVector(new int[]{0x8c,0x50,0x53}));
    }

}
