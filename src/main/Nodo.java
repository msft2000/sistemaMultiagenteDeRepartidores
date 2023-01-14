
package main;

import Python.PythonScripts;
import java.util.HashMap;

public class Nodo {
    private String id;
    private String[] enlacesIn;
    private String[] enlacesOut;
    private HashMap<Nodo,String> viajesBus;
    public Nodo(String id) {
        this.id=id;
        viajesBus=new HashMap<>();
        setEnlaces();
    }
    private void setEnlaces(){
        enlacesIn= PythonScripts.getInstance().getEnlaces(id, 'i');
        enlacesOut= PythonScripts.getInstance().getEnlaces(id, 'o');
    }
    public String getID(){
        return id;
    }
    public String[] getEnlacesOut(){
        return enlacesOut;
    }
    public void setViajesBus(Nodo destino,String value){
        viajesBus.put(destino, value);
    }
    public void getViajesBus(){
        for(Nodo i: viajesBus.keySet()){
            System.out.println("Destino: "+ i.getID()+" Valor: "+viajesBus.get(i));
        }
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
