
package Agentes;

import java.io.Serializable;

public class SimulationInfoMsg implements Serializable{
    String idVehiculo, idEnlaceDestino,typeVehicle,idEnlaceActual;
    double travelTime,departTime;
    public SimulationInfoMsg(String idVehiculo, String idEnlaceDestino, String typeVehicle, String idEnlaceActual, double travelTime, double departTime) {
        this.idVehiculo = idVehiculo;
        this.idEnlaceDestino = idEnlaceDestino;
        this.typeVehicle = typeVehicle;
        this.idEnlaceActual = idEnlaceActual;
        this.travelTime = travelTime;
        this.departTime = departTime;
    }

 

    

}
