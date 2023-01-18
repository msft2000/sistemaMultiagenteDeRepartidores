package main;

import Matrices.MatrizMaster;
import GUI.SumoMainGui;
import jade.Boot;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.sumo.libtraci.*;

public class SumoMain {

    //private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    private static SumoMain instance;
    private jade.core.Runtime jadeRunTime;
    private ContainerController repartidores, autos, buses;
    private double vAutos,vBuses,vBici,vMoto;
    private int cBuses;
    private SumoMainGui guiMain;

    public SumoMain() {
        guiMain = new SumoMainGui();
        guiMain.setVisible(true);
    }

    public void iniciar(String csvBuses,String csvRepartidores, String csvAutos, double velocidadVehiculos, double velocidadBuses, int capacidadBuses, double velocidadRepartidoresBicicleta, double velocidadRepartidoresMoto) {
        this.vAutos=velocidadVehiculos;
        this.vBuses=velocidadBuses;
        this.cBuses=capacidadBuses;
        this.vBici=velocidadRepartidoresBicicleta;
        this.vMoto=velocidadRepartidoresMoto;
        
        Boot.main(new String[]{"-gui"});//iniciación de jade.Boot
        /*
            Instancias para la inicialización de jade y sus agentes
         */
        jadeRunTime = jade.core.Runtime.instance();
        Profile p1,p2,p3; 
        p1= new ProfileImpl("localhost", 1099, "MAS-Repartos");//Perfil de los containercontroller
        p2= new ProfileImpl("localhost", 1099, "MAS-Repartos");//Perfil de los containercontroller
        p3= new ProfileImpl("localhost", 1099, "MAS-Repartos");//Perfil de los containercontroller
        //Container y perfil de repartidores
        p1.setParameter(Profile.CONTAINER_NAME, "Repartidores-Container");
        repartidores = jadeRunTime.createAgentContainer(p1);
        //Container y perfil de autos
        p2.setParameter(Profile.CONTAINER_NAME, "Autos-Container");
        autos = jadeRunTime.createAgentContainer(p2);
        //Container y perfil de buses
        p3.setParameter(Profile.CONTAINER_NAME, "Buses-Container");
        buses = jadeRunTime.createAgentContainer(p3);
        /*
            Inicialización de SUMO y suscribción de variables
        */
        System.loadLibrary("libtracijni");
        Simulation.start(new StringVector(new String[]{"sumo-gui", "-c", "resources/SumoMaps/mapa2Way2.sumocfg"}));
        int[] co = new int[]{0x7a, 0x66};//Se solicita la información referente a los autos que ya han finalizado sus rutas
        //0x7a: id arrived vehicless
        //0x66: current simulation time
        Simulation.subscribe(new IntVector(co));
 /*
            Carga de las matrices de datos de viajes
         */
        //MatrizOD busOD = new MatrizOD(csvBuses);
        //addViajeBus(busOD.getNodosViajeDisponible());
        MatrizMaster matVehiculos=new MatrizMaster(csvRepartidores,csvBuses,csvAutos);
        addViajeBus(matVehiculos.getNodosViajeBusDisponible());
        addViajeAuto(matVehiculos.getNodosViajeAutosDisponible());

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
    
    private void addViajeAuto(ArrayList<Nodo> nodos) {//Lee los viajes de bus y los anexa a los agentes
        for (Nodo i : nodos) {
            for (Nodo j : i.getViajesBus().keySet()) {
                /*Asignación de ruta y caracteristicas al Bus*/
                addViajeAuto(i, j);
            }
        }
    }

    public void addViajeBus(Nodo origen, Nodo destino) {//Agrega un bus a la simulación
        int salt = java.time.LocalDateTime.now().hashCode();//Código de bus - hash de la fecha y hora actuales
        String ruta = origen.getID() + "_" + destino.getID() + "_" + salt;//Código de ruta
        String id = "Bus_" + ruta;//Código de bus
        double travelTime = addSimulacionVehiculo(id, ruta, origen.getEnlacesOut()[0], destino.getEnlacesIn()[0], "Bus", cBuses+"",vBuses);//Agrega el vehiculo a la simulación
        addAgenteVehiculo(buses, id, "main.BusAgent", origen, destino, cBuses+"", travelTime);//anexa el agente al vehiculo
    }
    
    public void addViajeAuto(Nodo origen, Nodo destino) {//Agrega un bus a la simulación
        int salt = java.time.LocalDateTime.now().hashCode();//Código de bus - hash de la fecha y hora actuales
        String ruta = origen.getID() + "_" + destino.getID() + "_" + salt;//Código de ruta
        String id = "Auto_" + ruta;//Código de bus
        double travelTime = addSimulacionVehiculo(id, ruta, origen.getEnlacesOut()[0], destino.getEnlacesIn()[0], "Auto", "",vAutos);//Agrega el vehiculo a la simulación
        addAgenteVehiculo(autos, id, "main.AutoAgent", origen, destino, "", travelTime);//anexa el agente al vehiculo
    }


    private void addAgenteVehiculo(ContainerController container, String idAgente, String classAgent, Nodo origen, Nodo destino, String capacidad, double travelTime) {
        if (container != null) {
            try {
                AgentController ac;
                if (!capacidad.equals("")) ac = container.createNewAgent(idAgente, classAgent, new Object[]{origen, destino, capacidad , travelTime + "", Simulation.getSubscriptionResults().get(0x66).getString()});
                else ac = container.createNewAgent(idAgente, classAgent, new Object[]{origen, destino , travelTime + "", Simulation.getSubscriptionResults().get(0x66).getString()});
                ac.start();
            } catch (StaleProxyException e) {
                System.err.println(e.getMessage());
            }
        }
    
    }
    private double addSimulacionVehiculo(String idVehiculo, String idRuta, String idOrigen, String idDestino, String tipo, String capacidad,double velocidadMaxima) {
        TraCIStage ruta = Simulation.findRoute(idOrigen, idDestino, tipo, 0, libtraci.getROUTING_MODE_AGGREGATED());
        Route.add(idRuta, ruta.getEdges());
        Vehicle.add(idVehiculo, idRuta, tipo);
        Vehicle.setMaxSpeed(idVehiculo, velocidadMaxima);//Velocidad máxima del vehiculo
        if(!"".equals(capacidad)) Vehicle.setParameter(idVehiculo, "capacidad", capacidad);
        Vehicle.subscribe(idVehiculo, new IntVector(new int[]{0x50, 0x53}));
        return ruta.getTravelTime();
        //Vehicle.subscribeContext(idVehiculo,0xaa, 100,new IntVector(new int[]{0x8c,0x50,0x53}));
    }

}
