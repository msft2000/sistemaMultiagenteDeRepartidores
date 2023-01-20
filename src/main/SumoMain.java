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
import java.util.HashMap;
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

    public void iniciar(String csvBuses,String csvRepartidores, String csvAutos, MatrizMaster matVehiculos,double velocidadVehiculos, double velocidadBuses, int capacidadBuses, double velocidadRepartidoresBicicleta, double velocidadRepartidoresMoto) {
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
        Simulation.start(new StringVector(new String[]{"sumo-gui", "-c", "resources/SumoMaps/mapa2Way.sumocfg","--start"}));
        int[] co = new int[]{0x7a, 0x66};//Se solicita la información referente a los autos que ya han finalizado sus rutas
        //0x7a: id arrived vehicless
        //0x66: current simulation time
        Simulation.subscribe(new IntVector(co));
        

        /*
            Carga de las matrices de datos de viajes
         */
        addViajeBus(matVehiculos.getNodosViajeBusDisponible());
        //addViajeAuto(matVehiculos.getNodosViajeAutosDisponible());
        addViajeRepartidor(matVehiculos.getRepartidores());
        
        try {
            jadeRunTime.createAgentContainer(new ProfileImpl("localhost", 1099, "MAS-Repartos")).createNewAgent("SumoManager", "Agentes.SumoAgent2", null).start();//Perfil de los containercontroller
        } catch (StaleProxyException ex) {
            ex.printStackTrace();
        }
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
            for (Nodo j : i.getViajesAutos().keySet()) {
                /*Asignación de ruta y caracteristicas al Bus*/
                addViajeAuto(i, j);
            }
        }
    }
    
    private void addViajeRepartidor(ArrayList<Repartidor> repartidores) {//Lee los viajes de bus y los anexa a los agentes
        for (Repartidor rep : repartidores) {
            HashMap<Nodo, ArrayList<Nodo>> entregas=rep.getEntregasPendientes();
            for (Nodo origen:entregas.keySet()) {
                ArrayList<Nodo> destino = entregas.get(origen);
                addViajeRepartidor(origen, destino.get(0), rep.getTipoRepartidor(), rep);
                destino.remove(0);
                break;
            }
        }
    }

    public void addViajeBus(Nodo origen, Nodo destino) {//Agrega un bus a la simulación
        int salt = java.time.LocalDateTime.now().hashCode();//Código de bus - hash de la fecha y hora actuales
        String ruta = origen.getID() + "_" + destino.getID() + "_" + salt;//Código de ruta
        String id = "Bus_" + ruta;//Código de bus
        double travelTime = addSimulacionVehiculo(id, ruta, origen.getEnlacesOut()[0], destino.getEnlacesIn()[0], "Bus", cBuses+"",vBuses,false);//Agrega el vehiculo a la simulación
        addAgenteVehiculo(buses, id, "Agentes.BusAgent", origen, destino, cBuses+"", travelTime,null);//anexa el agente al vehiculo
    }
    
    public void addViajeAuto(Nodo origen, Nodo destino) {//Agrega un bus a la simulación
        int salt = java.time.LocalDateTime.now().hashCode();//Código de bus - hash de la fecha y hora actuales
        String ruta = origen.getID() + "_" + destino.getID() + "_" + salt;//Código de ruta
        String id = "Auto_" + ruta;//Código de bus
        double travelTime = addSimulacionVehiculo(id, ruta, origen.getEnlacesOut()[0], destino.getEnlacesIn()[0], "Auto", "",vAutos,false);//Agrega el vehiculo a la simulación
        addAgenteVehiculo(autos, id, "Agentes.AutoAgent", origen, destino, "", travelTime,null);//anexa el agente al vehiculo
    }
    
    public void addViajeRepartidor(Nodo origen, Nodo destino, String tipoRepartidor, Repartidor repartidor) {//Agrega un bus a la simulación
        int salt = java.time.LocalDateTime.now().hashCode();//Código de bus - hash de la fecha y hora actuales
        String ruta = origen.getID() + "_" + destino.getID() + "_" + salt;//Código de ruta
        String id = repartidor.getID();//Código de bus
        double travelTime = addSimulacionVehiculo(id, ruta, origen.getEnlacesOut()[0], destino.getEnlacesIn()[0], tipoRepartidor, "",((tipoRepartidor=="Moto") ? vMoto : vBici),true);//Agrega el vehiculo a la simulación
        addAgenteVehiculo(repartidores, id, "Agentes.RepartidorAgent", origen, destino, "", travelTime,repartidor);//anexa el agente al vehiculo
    }


    private void addAgenteVehiculo(ContainerController container, String idAgente, String classAgent, Nodo origen, Nodo destino, String capacidad, double travelTime,Repartidor r) {
        if (container != null) {
            try {
                AgentController ac;
                if (!capacidad.equals("")) ac = container.createNewAgent(idAgente, classAgent, new Object[]{origen, destino, capacidad , travelTime + "", Simulation.getSubscriptionResults().get(0x66).getString()});
                else if (r!=null) ac = container.createNewAgent(idAgente, classAgent, new Object[]{origen, destino , travelTime + "", Simulation.getSubscriptionResults().get(0x66).getString(),r});
                else ac = container.createNewAgent(idAgente, classAgent, new Object[]{origen, destino , travelTime + "", Simulation.getSubscriptionResults().get(0x66).getString()});
                ac.start();
            } catch (StaleProxyException e) {
                System.err.println(e.getMessage());
            }
        }
    
    }
    private double addSimulacionVehiculo(String idVehiculo, String idRuta, String idOrigen, String idDestino, String tipo, String capacidad,double velocidadMaxima,boolean stop) {
        TraCIStage ruta = Simulation.findRoute(idOrigen, idDestino, tipo, 0, libtraci.getROUTING_MODE_AGGREGATED());
        Route.add(idRuta, ruta.getEdges());
        Vehicle.add(idVehiculo, idRuta, tipo);
        Vehicle.setMaxSpeed(idVehiculo, velocidadMaxima);//Velocidad máxima del vehiculo
        if(!"".equals(capacidad)) Vehicle.setParameter(idVehiculo, "capacidad", capacidad);
        if(stop) Vehicle.setStop(idVehiculo,idDestino , Lane.getLength(idDestino+"_0")); //parada para vehiculos que realizan rutas ida y vuelta
        Vehicle.subscribe(idVehiculo, new IntVector(new int[]{0x50, 0x53}));
        return ruta.getTravelTime();
    }

}
