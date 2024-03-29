package main;

import Python.PythonScripts;
import java.util.ArrayList;
import java.util.HashMap;
import main.Nodo;

public class Repartidor {

    private String id;
    private HashMap<Nodo, ArrayList<Nodo>> entregasPendientes;

    public Repartidor(String id) {
        this.id = id;
        entregasPendientes = new HashMap<>();
    }

    public String getID() {
        return id;
    }

    public void addEntregasPendientes(Nodo origen, Nodo destino) {
        if (entregasPendientes.containsKey(origen)) {
            ArrayList<Nodo> auxEntregas = (ArrayList<Nodo>) entregasPendientes.get(origen).clone();
            auxEntregas.add(destino);
            entregasPendientes.put(origen, auxEntregas);
        } else {
            ArrayList<Nodo> auxEntregas = new ArrayList<>();
            auxEntregas.add(destino);
            entregasPendientes.put(origen, auxEntregas);
        }
    }

    public void getEntregasPendientes() {
        entregasPendientes.forEach((origen, destinos) -> {
            System.out.print("Origen: " + origen + ": destinos: ");
            destinos.forEach(des -> System.out.print(" " + des + " "));
            System.out.println("");
        });
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
