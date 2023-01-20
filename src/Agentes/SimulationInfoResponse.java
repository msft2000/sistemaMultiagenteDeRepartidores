
package Agentes;

import java.io.Serializable;

public class SimulationInfoResponse implements Serializable{
    double currentTime, travelTime;
    String idEnlaceNuevo;//si al devolver esta vacio entonces no se ha cambiado de nodo aun
    public SimulationInfoResponse(double currentTime, double travelTime, String idEnlaceNuevo) {
        this.currentTime = currentTime;
        this.travelTime = travelTime;
        this.idEnlaceNuevo = idEnlaceNuevo;
    }
    
}
