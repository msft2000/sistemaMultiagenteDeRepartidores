
package main;

import Python.PythonScripts;
import java.io.Serializable;
import java.util.HashMap;

public class Nodo implements Serializable{
    private String id;
    private String[] enlacesIn;
    private String[] enlacesOut;
    //private HashMap<Nodo,Integer[]> viajesBus;
    private HashMap<Nodo,Integer> viajesBus;//viajes que realizan los buses
    private HashMap<Nodo,Integer> viajesAutos; //viajes que realizan los autos
    public Nodo(String id) {
        this.id=id;//ID de los nodos (junctions de Sumo) los IDs tienen el formato 'Jn'
        viajesBus=new HashMap<>();//Mapa cuya clave es el Nodo destino y el valor es la cantidad de personas que viajan a ese destino
        viajesAutos=new HashMap<>();
        setEnlaces();//Se obtienen los enlaces (Ids de Edges de Sumo) que entran y salen del nodo
    }
    private void setEnlaces(){
        enlacesIn= PythonScripts.getInstance().getEnlaces(id, 'i');
        enlacesOut= PythonScripts.getInstance().getEnlaces(id, 'o');
    }
    public String getID(){//Obtener id del nodo
        return id;
    }
    public String[] getEnlacesOut(){//Se obtienen los enlaces de salida del nodo
        return enlacesOut;
    }
    public String[] getEnlacesIn(){//Se obtienen los enlaces de entrada al nodo
        return enlacesIn;
    }
    public void setViajesBus(Nodo destino,int value){//Se agrega un viaje al nodo destino y con una cantidad value de pasajeros
        for(Nodo i: viajesBus.keySet()){
            if(i.getID().equals(destino.getID())){
                if(value!=0) {//Actualización del número de pasajeros faltantes por transportar
                    System.out.println("Viaje "+id+"_"+destino.getID()+" : Faltan "+value+" Pasajeros para completar la ruta");
                    SumoMain.getInstance().addViajeBus(this, destino);
                    viajesBus.put(destino, value);
                    return;
                }
                else {
                    System.out.println("Viaje "+id+"_"+destino.getID()+" : Viajes terminados");
                    viajesBus.remove(i);
                    return;
                }
            }
        }
        viajesBus.put(destino, value);
    }
    
    public void setViajesAutos(Nodo destino,int value){
        viajesAutos.put(destino, value);
    }
    
    public int getNumViajesBus(){//devuelve el número de viajes de bus que parten del nodo
        return viajesBus.size();
    }
    
    public HashMap<Nodo,Integer> getViajesBus(){
       return viajesBus;
    }
    
    public int getValueViaje(Nodo destino){
        for(Nodo i: viajesBus.keySet()){
            if(i.getID().equals(destino.getID())){
                return viajesBus.get(i);
            }
        }
        return -1;
    }
    
    public int getNumViajesAutos(){//devuelve el número de viajes de bus que parten del nodo
        return viajesAutos.size();
    }
    
    public HashMap<Nodo,Integer> getViajesAutos(){
       return viajesAutos;
    }
    
    public int getValueViajeAutos(Nodo destino){
        for(Nodo i: viajesAutos.keySet()){
            if(i.getID().equals(destino.getID())){
                return viajesAutos.get(i);
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
        Nodo aux=(Nodo)obj;
        return aux.getID().equals(this.id);
    }
    
    
}
