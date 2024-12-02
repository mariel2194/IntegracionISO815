package com.sg.facturacion.asientosws;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.sg.facturacion.models.AsientoContable;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

@Service
public class AsientoContableServiceClient extends WebServiceGatewaySupport {

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public int sendAsientoContable() {
        // Crear el objeto de solicitud hardcodeado
        RegistrarAsientoRequest request = new RegistrarAsientoRequest();
        request.setidAuxiliar(3);
        request.setDescripcion("Test Asiento Facturacion");
        request.setCuentadb(100);
        request.setCuentacr(0);
        request.setMonto(5000.0);

        // Configurar el marshaller para incluir el namespace correcto
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.sg.facturacion.asientosws");

        // Configurar el WebServiceTemplate con el marshaller
        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);

        // Enviar la solicitud y recibir la respuesta
        RegistrarAsientoResponse response = (RegistrarAsientoResponse) webServiceTemplate.marshalSendAndReceive("http://asientocontablews.somee.com/AsientoContableService.asmx?op=RegistrarAsiento",  request,
                new SoapActionCallback("http://tempuri.org/RegistrarAsiento") // SOAP Action
                );

        // Imprimir el id de asiento recibido
        System.out.println("Id de asiento: " + response.getidAsiento());

        return response.getidAsiento();
    }
}

