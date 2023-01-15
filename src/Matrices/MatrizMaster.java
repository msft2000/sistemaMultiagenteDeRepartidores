
package Matrices;

import java.util.ArrayList;
import java.util.HashMap;
import main.Nodo;
import main.Repartidor;


public class MatrizMaster {
    private ArrayList<String[]> matrizNodos;//Matriz OD leida
    private ArrayList<String[]> matrizRepartidores;
    private Object[] nodosOrigen;//Nodos origen de la matriz 
    private Object[] nodosDestino;//Nodos destino de la matriz
    private HashMap<String,Nodo> Nodos;
    private ArrayList<Repartidor> repartidores;
    
    public MatrizMaster(String pathNodos, String pathRepartidores) {
        //path: dirección del archivo csv de la matriz a leer
        Nodos=new HashMap<>();
        repartidores=new ArrayList<Repartidor>();
        LectorCSV read=new LectorCSV();
        LectorCSV read2=new LectorCSV();
        matrizNodos=read.getMatrix(pathNodos);//se lee el csv y se genera la matriz (index del ArrayList - filas, index de String[] - columnas)
        matrizRepartidores=read2.getMatrix(pathRepartidores);
        nodosOrigen=getNodosOrigen();//se obtienen todos los nodos origen disponibles de la matriz
        nodosDestino=getNodosDestino();//se obtienen todos los nodos destino disponibles de la matriz
        setViajesBus();//se organizan los viajes de bus por nodo
        setEntregas();
    }
    
    private Object[] getNodosDestino(){
        //Devuelve los nodos que son destino - nodos enumerados en la primera fila
        String[]aux=matrizNodos.get(0);//primera fila
        ArrayList<String> nodos =new ArrayList<>();
        for(String i : aux){
            if(!i.equals("")) nodos.add(i);
        }
        return nodos.toArray();
    }
    
    private Object[] getNodosOrigen(){
        //Devuelve los nodos que son origen - nodos enumerados en la primera columna de cada fila
        ArrayList<String> nodos =new ArrayList<>();
        for(String[] i: matrizNodos){
            if(!i[0].equals("")) nodos.add(i[0]); 
        }
        return nodos.toArray();
    }
    
    private void setViajesBus(){
        /*
            Recorre las filas de la matriz (nodosOrigen) crea el Nodo si no existe,
            recorre todas las columnas por cada nodoOrigen, se crea el nodo si no existe,
            y si el par (origen, destino) tiene anexado un valor diferente de 0 o vacío se
            genera el viaje a dicho destino y se indica el número de personas que se deben transportar
        */
        for(int i=0; i<nodosOrigen.length;i++){//recorrer los nodos origen de la matriz
            String idOrigen=(String)nodosOrigen[i];//id del nodo i
            if(!Nodos.containsKey(idOrigen)){//si el nodo no esta creado
                Nodos.put(idOrigen, new Nodo("J"+idOrigen));//se crea y agrega el nodo
            }
            for(int j=0;j<nodosDestino.length;j++){//recorrer los nodos destino de la matriz
                String idDestino=(String)nodosDestino[j];//id del nodo j
                if(!Nodos.containsKey(idDestino)){//si el nodo no esta creado
                    Nodos.put(idDestino, new Nodo("J"+idDestino));//se crea y agrega el nodo
                }
                String valor=matrizNodos.get(i+1)[j+1];//valor en la coordenada origen,destino
                if (!valor.equals("") && !valor.equals("0")){//si se dispone de un valor se agrega a los viajes desde origen
                    Nodos.get(idOrigen).setViajesBus( Nodos.get(idDestino), Integer.parseInt(valor));
                }
            }
        }
    }
    private void setEntregas(){
        for(int i=0; i<nodosOrigen.length;i++){//recorrer los nodos origen de la matriz
            String idOrigen=(String)nodosOrigen[i];//id del nodo i
            for(int j=0;j<nodosDestino.length;j++){//recorrer los nodos destino de la matriz
                String idDestino=(String)nodosDestino[j];//id del nodo j
                String valor=matrizRepartidores.get(i+1)[j+1];//valor en la coordenada origen,destino
                if(!valor.equals("0")){
                    if(repartidores.isEmpty()){
                        Repartidor primerRepartidor = new Repartidor(valor);
                        primerRepartidor.addEntregasPendientes(getNodo(idOrigen), getNodo(idDestino));
                        repartidores.add(primerRepartidor);
                    }else {
                        boolean agrego = false;
                        for(int k=0; k<repartidores.size(); k++) {
                            if(repartidores.get(k).getID().equals(valor)){
                                agrego=true;
                                repartidores.get(k).addEntregasPendientes(getNodo(idOrigen), getNodo(idDestino));
                            }
                        }
                        if(!agrego){
                            Repartidor repartidorNuevo = new Repartidor(valor);
                            repartidorNuevo.addEntregasPendientes(getNodo(idOrigen), getNodo(idDestino));
                            repartidores.add(repartidorNuevo);
                        }
                    }
                }
            }
        }
    }
    
    public Nodo getNodo(String id){
        return Nodos.get(id);
    }
    
    public ArrayList<Nodo> getNodosViajeDisponible(){
        /*
            Devuelve una lista de los nodos que tienen viajes disponibles
        */
        ArrayList<Nodo> nodos=new ArrayList<>();
        for(Nodo i: Nodos.values()){//Se enlistan los nodos que tengan viajes de Bus disponibles
            if(i.getNumViajesBus()>0) nodos.add(i); 
        }
        return nodos;
    
    }
    public void getEntregas(){
        repartidores.forEach((r) -> {
            System.out.println("El repartidor " + r.getID() + " tiene los pedidos pendientes: ");
            r.getEntregasPendientes();
        });
    }
}
