
package Matrices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import main.Repartidor;

public class MatrizREP {
    private ArrayList<String[]> matriz;
    private Object[] nodosOrigen;
    private Object[] nodosDestino;
    private ArrayList<Repartidor> repartidores;
    
    public MatrizREP(String path) {
        repartidores=new ArrayList<Repartidor>();
        LectorCSV read=new LectorCSV();
        matriz=read.getMatrix(path);
        nodosOrigen=getNodosOrigen();
        nodosDestino=getNodosDestino();
        setEntregas();//se organizan los viajes por nodo origen
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
    
    private void setEntregas(){
        for(int i=0; i<nodosOrigen.length;i++){//recorrer los nodos origen de la matriz
            String idOrigen=(String)nodosOrigen[i];//id del nodo i
            for(int j=0;j<nodosDestino.length;j++){//recorrer los nodos destino de la matriz
                String idDestino=(String)nodosDestino[j];//id del nodo j
                String valor=matriz.get(i+1)[j+1];//valor en la coordenada origen,destino
                if(!valor.equals("0")){
                    if(repartidores.isEmpty()){
                        Repartidor primerRepartidor = new Repartidor(valor);
                        primerRepartidor.addEntregasPendientes(idOrigen, idDestino);
                        repartidores.add(primerRepartidor);
                    }else {
                        boolean agrego = false;
                        for(int k=0; k<repartidores.size(); k++) {
                            if(repartidores.get(k).getID().equals(valor)){
                                agrego=true;
                                repartidores.get(k).addEntregasPendientes(idOrigen, idDestino);
                            }
                        }
                        if(!agrego){
                            Repartidor repartidorNuevo = new Repartidor(valor);
                            repartidorNuevo.addEntregasPendientes(idOrigen, idDestino);
                            repartidores.add(repartidorNuevo);
                        }
                    }
                }
            }
        }
    }
    
    public void getEntregas(){
        repartidores.forEach((r) -> {
            System.out.println("El repartidor " + r.getID() + " tiene los pedidos pendientes: ");
            r.getEntregasPendientes();
        });
    }
    
}
