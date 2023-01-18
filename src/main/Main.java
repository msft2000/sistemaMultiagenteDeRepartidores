
package main;

import Matrices.MatrizMaster;
import VIEW.MAIN;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Main {
   public static void main(String[] args) {
//       MatrizMaster mat = new MatrizMaster("matriz.csv","matrizRepartidores.csv","matrizVehiculos.csv");
//       mat.getNodosViajeDisponibleVehiculos();
//       MAIN formulario = new MAIN();
//       formulario.setVisible(true);
       SumoMain.getInstance();      
//        Process mProcess;         
//        try{
////            mProcess = Runtime.getRuntime().exec(new String[]{"python3","main.py","J1","Red2Way.net.xml","i"+""},null,new File("."));
////            InputStream stdout = mProcess.getInputStream();
////            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout,StandardCharsets.UTF_8));
//            mProcess = Runtime.getRuntime().exec(new String[]{"python3","main.py","J1","Red2Way.net.xml","i"+ ""},null,new File("."));
//            InputStream stdout = mProcess.getInputStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(stdout,StandardCharsets.UTF_8));
//            String line;
//            String aux="";
//            while((line = reader.readLine()) != null){
//                aux=aux+line;
//            }
//            System.out.println(aux.split(";"));
//        }catch(Exception e) {
//           System.out.println("Exception Raised" + e.toString());
//        }
//        ProcToString pts=new ProcToString("python3","main.py","J1","Red2Way.net.xml","i");
////        ProcToString pts=new ProcToString("bash","bin.sh");
//
//        try {
//            System.out.println(pts.runProcess());
//            System.out.println(pts.runProcess());
////            System.out.println(pts.hasResult());
//            if (pts.hasResult()){
//                String resultado = pts.getResult();
//                System.out.println(resultado);
//            }
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
   }
        /*
        //MatrizOD mat=new MatrizOD("matriz.csv");
        //System.out.println(mat.getNodo("1").getEnlacesOut()[0]);
        //System.out.println(""+Integer.parseInt(""));
        /*
        Simulation.start(new StringVector(new String[] {"sumo-gui", "-c", "mapa3.sumocfg"}));
        for (int i = 0; i < 2000; i++) {
            Simulation.step();
            for(Object j:mat.getNodosOrigenActivos()){
                String a=Junction.getPosition("J"+(String)j).getString();
                System.out.println(a);
            }
        }
        Simulation.close();*/
                
        //System.loadLibrary("libtracijni");
        //Simulation.start(new StringVector(new String[] {"sumo-gui", "-c", "mapa3.sumocfg"}));
        //StringVector a= Edge.getIDList();
        //Iterator j=a.iterator();
        //System.out.println("Step - "+i);
        //while(j.hasNext()){
            //System.out.println(j.next().toString());
        //}
        /*for (int i = 0; i < 2000; i++) {
            Simulation.step();
            /*if(i==5){
                Route.add("r1", Simulation.findRoute("E20", "-E39").getEdges());
                Vehicle.add("v1", "r1", "DEFAULT_VEHTYPE");
                Vehicle.setStop("v1", "-E39",8);
            }
            if (Vehicle.getIDList().contains("v1")) {
                System.out.println("Posicion Vehiculo: "+ Vehicle.getPosition("v1").getString());
                System.out.println("Posicion Nodo J14: "+Junction.getPosition("J14").getString());
            }
                if(Simulation.getStopStartingVehiclesIDList().contains("v1")){
                    System.out.println("Reenrutado");
                    Vehicle.changeTarget("v1", "E20");
                    Vehicle.resume("v1");
                }*/
            /*if(i==0){
                Route.add("bus1_r1", Simulation.findRoute("E20", "E31").getEdges());
                Vehicle.add("bus1","bus1_r1","Bus");
                Vehicle.setParameter("bus1", "capacidad", "95");
                System.out.println("Capacidad: "+ Vehicle.getParameter("bus1", "capacidad"));
                Junction.subscribe("J1");
            }
            
            
        }
        System.out.println(Simulation.getSubscriptionResults().toString());
            
        Simulation.close();*/
    //}
}
