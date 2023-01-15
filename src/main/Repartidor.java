package main;

import Python.PythonScripts;
import java.util.ArrayList;
import java.util.HashMap;

public class Repartidor {

    private String id;
    private HashMap<String, ArrayList<String>> entregasPendientes;

    public Repartidor(String id) {
        this.id = id;
        entregasPendientes = new HashMap<>();
    }

    public String getID() {
        return id;
    }

    public void addEntregasPendientes(String origen, String destino) {
        if (entregasPendientes.containsKey(origen)) { 
            ArrayList<String> auxEntregas = (ArrayList<String>) entregasPendientes.get(origen).clone();
            auxEntregas.add(destino);
            entregasPendientes.put(origen, auxEntregas);
        } else {
            ArrayList<String> auxEntregas = new ArrayList<>();
            auxEntregas.add(destino);
            entregasPendientes.put(origen, auxEntregas);
        }
    }

    public void getEntregasPendientes() {
        entregasPendientes.forEach((origen,destinos) -> System.out.println("Origen: " + origen + ": destinos: " + destinos));
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        Nodo aux = (Nodo) obj;
        return aux.getID().equals(this.id);
    }

}
