
package Matrices;

import java.util.ArrayList;
import java.util.HashMap;
import main.Nodo;

public class MatrizOD {
    private ArrayList<String[]> matriz;
    private Object[] nodosOrigen;
    private Object[] nodosDestino;
    private HashMap<String,Nodo> Nodos;
    
    public MatrizOD(String path) {
        Nodos=new HashMap<>();
        LectorCSV read=new LectorCSV();
        matriz=read.getMatrix(path);
        nodosOrigen=getNodosOrigen();
        nodosDestino=getNodosDestino();
        getViajes();//se organizan los viajes por nodo origen
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
    
    private void getViajes(){
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
                    Nodos.get(idOrigen).setViajesBus( Nodos.get(idDestino), valor);
                }
            }
        }
    }
    
    
    
    public Nodo getNodo(String id){
        return Nodos.get(id);
    }
    
}
