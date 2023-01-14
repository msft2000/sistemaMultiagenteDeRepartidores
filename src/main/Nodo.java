
package main;

import Python.PythonScripts;
import java.util.HashMap;

public class Nodo {
    private String id;
    private String[] enlacesIn;
    private String[] enlacesOut;
    private HashMap<Nodo,Integer> viajesBus;
    public Nodo(String id) {
        this.id=id;//ID de los nodos (junctions de Sumo) los IDs tienen el formato 'Jn'
        viajesBus=new HashMap<>();//Mapa cuya clave es el Nodo destino y el valor es la cantidad de personas que viajan a ese destino
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
        if(viajesBus.containsKey(destino)){
            BusManager manager=BusManager.getInstance();
            //manager.
        }
        viajesBus.put(destino, value);
    }
    
    public int getNumViajesBus(){//devuelve el número de viajes de bus que parten del nodo
        return viajesBus.size();
    }
    
    public HashMap<Nodo,Integer> getViajesBus(){
       return viajesBus;
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
