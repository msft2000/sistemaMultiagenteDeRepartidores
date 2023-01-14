
package Matrices;

import java.util.ArrayList;
import java.util.HashMap;
import main.Nodo;

public class MatrizOD {
    private ArrayList<String[]> matriz;//Matriz OD leida
    private Object[] nodosOrigen;//Nodos origen de la matriz 
    private Object[] nodosDestino;//Nodos destino de la matriz
    private HashMap<String,Nodo> Nodos;
    /*
        Nodos: Map cuyo codigo es el id del nodo (El codigo solo es númerico no incluye la J) e.g '1'
        El valor es el nodo en cuestión
    */
    
    public MatrizOD(String path) {
        //path: dirección del archivo csv de la matriz a leer
        Nodos=new HashMap<>();
        LectorCSV read=new LectorCSV();
        matriz=read.getMatrix(path);//se lee el csv y se genera la matriz (index del ArrayList - filas, index de String[] - columnas)
        nodosOrigen=getNodosOrigen();//se obtienen todos los nodos origen disponibles de la matriz
        nodosDestino=getNodosDestino();//se obtienen todos los nodos destino disponibles de la matriz
        setViajesBus();//se organizan los viajes de bus por nodo
    }
    
    private Object[] getNodosDestino(){
        //Devuelve los nodos que son destino - nodos enumerados en la primera fila
        String[]aux=matriz.get(0);//primera fila
        ArrayList<String> nodos =new ArrayList<>();
        for(String i : aux){
            if(!i.equals("")) nodos.add(i);
        }
        return nodos.toArray();
    }
    
    private Object[] getNodosOrigen(){
        //Devuelve los nodos que son origen - nodos enumerados en la primera columna de cada fila
        ArrayList<String> nodos =new ArrayList<>();
        for(String[] i: matriz){
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
                String valor=matriz.get(i+1)[j+1];//valor en la coordenada origen,destino
                if (!valor.equals("") && !valor.equals("0")){//si se dispone de un valor se agrega a los viajes desde origen
                    Nodos.get(idOrigen).setViajesBus( Nodos.get(idDestino), Integer.parseInt(valor));
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
    
    
}
