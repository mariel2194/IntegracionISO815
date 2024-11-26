package com.sg.facturacion.asientosws;

import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.sg.facturacion.models.AsientoContable;

import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.soap.SoapMessage;

@Component
public class AsientoContableSoapClient {

    private final WebServiceTemplate webServiceTemplate;

    public AsientoContableSoapClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public Integer registrarAsiento(AsientoContable asientoContable) {
        String soapAction = "http://tempuri.org/RegistrarAsiento";
        String url = "http://asientocontablews.somee.com/AsientoContableService.asmx?op=RegistrarAsiento";

        String requestXml = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <RegistrarAsiento xmlns=\"http://tempuri.org/\">\n" +
                "      <idAuxiliar>3</idAuxiliar>\n" + 
                "      <descripcion>" + asientoContable.getDescripcion() + "</descripcion>\n" +
                "      <cuentaDB>" + asientoContable.getCuentadb() + "</cuentaDB>\n" +
                "      <cuentaCR>" + asientoContable.getCuentacr() + "</cuentaCR>\n" +
                "      <monto>" + asientoContable.getMonto() + "</monto>\n" +
                "    </RegistrarAsiento>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";

        try {
            SoapMessage response = (SoapMessage) webServiceTemplate.marshalSendAndReceive(url, requestXml, new SoapActionCallback(soapAction));

            String responseXml = response.getPayloadSource().toString();
            Integer asientoId = extractAsientoIdFromResponse(responseXml);
            return asientoId;

        } catch (SoapFaultClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Integer extractAsientoIdFromResponse(String responseXml) {
        // Parse the response XML and extract the ID
        // In a real implementation, you would use an XML parser (like JAXB) to extract the ID properly
        // For simplicity, we're just simulating the extraction from the SOAP response
        String result = responseXml.substring(responseXml.indexOf("<RegistrarAsientoResult>") + 23,
                responseXml.indexOf("</RegistrarAsientoResult>"));
        return Integer.parseInt(result);
    }
}
