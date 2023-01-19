
package Agentes;

import java.io.Serializable;
import org.eclipse.sumo.libtraci.StringVector;

public class SimulationInfoMsg implements Serializable{
    String idVehiculo, idEnlaceDestino,typeVehicle,idEnlaceActual;
    Object[] EdgesRuta; //en caso de solicitar reenrutado se lee este valor

    public SimulationInfoMsg(String idVehiculo, String idEnlaceDestino, String typeVehicle, String idEnlaceActual, Object[] EdgesRuta) {
        this.idVehiculo = idVehiculo;
        this.idEnlaceDestino = idEnlaceDestino;
        this.typeVehicle = typeVehicle;
        this.idEnlaceActual = idEnlaceActual;
        this.EdgesRuta = EdgesRuta;
    }

    

}
