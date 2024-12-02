package com.sg.facturacion.asientosws;
import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public RegistrarAsientoRequest createRegistrarAsientoRequest() {
        return new RegistrarAsientoRequest();
    }
}
