package com.sg.facturacion.asientosws;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.sg.facturacion.models.AsientoContable;

@XmlRootElement(name = "RegistrarAsientoResponse", namespace = "http://tempuri.org/")
public class RegistrarAsientoResponse {

    private Integer idAsiento;   
    

    @XmlElement(name = "idAsiento", namespace = "http://tempuri.org/")
    public Integer getidAsiento() {
        return idAsiento;
    }
    
    public void setidAuxiliar(Integer idAsiento) {
        this.idAsiento = idAsiento;
    }

  

    
}

