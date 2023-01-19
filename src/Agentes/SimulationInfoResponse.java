
package Agentes;

import java.io.Serializable;
import org.eclipse.sumo.libtraci.StringVector;


public class SimulationInfoResponse implements Serializable{
    double currentTime, travelTime;
    String idEnlaceNuevo;//si al devolver esta vacio entonces no se ha cambiado de nodo aun
    Object[] EdgesRuta; //se devuelven los edges de la ruta calculado si la hay

    public SimulationInfoResponse(double currentTime, double travelTime, String idEnlaceNuevo, Object[] EdgesRuta) {
        this.currentTime = currentTime;
        this.travelTime = travelTime;
        this.idEnlaceNuevo = idEnlaceNuevo;
        this.EdgesRuta = EdgesRuta;
    }
    
}
