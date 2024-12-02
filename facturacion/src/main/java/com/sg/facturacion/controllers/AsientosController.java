package com.sg.facturacion.controllers;

import com.sg.facturacion.asientosws.AsientoContableServiceClient;
import com.sg.facturacion.models.AsientoContable;
import com.sg.facturacion.repositories.AsientoContableRepository;
import com.sg.facturacion.repositories.FacturacionRepository;
import com.sg.facturacion.services.AsientoContableService;
import com.sg.facturacion.services.FacturacionService;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.*;
import com.sg.facturacion.models.Facturacion;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/asientos")
public class AsientosController {

    @Autowired
    private AsientoContableService asientoContableService; 
    
    @Autowired
    private AsientoContableRepository asientoContableRepository; 
    
    @Autowired
    private FacturacionRepository facturacionRepository;
    
    @Autowired
    private FacturacionService facturacionService;
  

    private static final Logger logger = LoggerFactory.getLogger(AsientosController.class);

    @GetMapping
    public String getAsientos(Model model) {
        List<AsientoContable> asientosList = asientoContableService.listAsientosContables(); 
        model.addAttribute("asientos", asientosList);
        return "asientos"; 
    }
    
    @PostMapping("/contabilizar/{id}")
    public String contabilizar(@RequestParam("id") Integer id, Model model) {
        AsientoContable asiento = asientoContableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asiento Contable no encontrado con ID: " + id));

        // Build the SOAP request
        String soapRequest = buildSoapRequest(asiento);
        String soapEndpoint = "http://asientocontablews.somee.com/AsientoContableService.asmx";

        try {
            String soapResponse = sendSoapRequest(soapEndpoint, soapRequest);
            Integer registrarAsientoResult = extractRegistrarAsientoResult(soapResponse);

            if (registrarAsientoResult != null) {
                asiento.setIdAsiento(registrarAsientoResult);
                asientoContableRepository.save(asiento);
                model.addAttribute("successMessage", "La transacción fue contabilizada exitosamente.");
            } else {
                throw new IllegalArgumentException("No se pudo obtener el resultado del SOAP.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Hubo un error al contabilizar el asiento: " + e.getMessage());
        }

        return "redirect:/asientos";
    }
    
 // Método para construir el XML
    private String buildSoapRequest(AsientoContable asiento) {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <soap12:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                             xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                             xmlns:soap12="http://www.w3.org/2003/05/soap-envelope">
              <soap12:Body>
                <RegistrarAsiento xmlns="http://tempuri.org/">
                  <idAuxiliar>%d</idAuxiliar>
                  <descripcion>%s</descripcion>
                  <cuentaDB>%d</cuentaDB>
                  <cuentaCR>%d</cuentaCR>
                  <monto>%.2f</monto>
                </RegistrarAsiento>
              </soap12:Body>
            </soap12:Envelope>
            """.formatted(
                asiento.getIdAuxiliar(),
                asiento.getDescripcion(),
                asiento.getCuentadb(),
                asiento.getCuentacr(),
                asiento.getMonto()
            );
    }
    
    
    private String sendSoapRequest(String endpointUrl, String soapRequest) throws IOException {
        URL url = new URL(endpointUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = soapRequest.getBytes("UTF-8");
            os.write(input, 0, input.length);
        }           
        

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Error en la solicitud SOAP. Código de respuesta: " + responseCode);
        }
        
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println(response.toString());
            return response.toString(); 
        }
        
    }
       
    private Integer extractRegistrarAsientoResult(String response) {
        // Use regex to parse the response and extract <RegistrarAsientoResult>
        Pattern pattern = Pattern.compile("<RegistrarAsientoResult>(\\d+)</RegistrarAsientoResult>");
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return null;
    }


    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("Error en el sistema", e);
        model.addAttribute("errorMessage", "Error en el sistema: " + e.getMessage());
        return "error"; 
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<AsientoContable> getEditAsientoForm(@PathVariable("id") Integer id) {
        AsientoContable asientoContable = asientoContableService.getAsientoContableById(id); 
        if (asientoContable == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(asientoContable, HttpStatus.OK);
    }

    @PostMapping("/delete/{id}")
    public String deleteAsientos(@PathVariable("id") Integer id) {
        asientoContableService.deleteAsientoContable(id);
        Facturacion factura = facturacionService.getFacturacionById(id);
        if (factura != null) {
            factura.setAsentada(false); 
            facturacionRepository.save(factura);
        }

        
        return "redirect:/asientos";
    }


   
}
