package com.sg.facturacion.controllers;

import com.sg.facturacion.asientosws.AsientoContableServiceClient;
import com.sg.facturacion.models.AsientoContable;
import com.sg.facturacion.repositories.AsientoContableRepository;
import com.sg.facturacion.services.*;
import com.sg.facturacion.services.AsientoContableService;
import com.sg.facturacion.services.ClienteService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/asientos")
public class AsientosController {

    @Autowired
    private AsientoContableService asientoContableService; 
    
    @Autowired
    private AsientoContableServiceClient client;
    
    @Autowired
    private AsientoContableRepository asientoContableRepository; // 

    private static final Logger logger = LoggerFactory.getLogger(AsientosController.class);

    @GetMapping
    public String getAsientos(Model model) {
        List<AsientoContable> asientosList = asientoContableService.listAsientosContables(); 
        model.addAttribute("asientos", asientosList);
        return "asientos"; 
    }
    
    @PostMapping("/contabilizar/{id}")
    public String contabilizar(@RequestParam("id") Integer id, Model model) {
        // Buscar el asiento contable por ID
        AsientoContable asiento = asientoContableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asiento Contable no encontrado con ID: " + id));

        // Construir el XML para la solicitud SOAP
        String soapRequest = buildSoapRequest(asiento);

        // Enviar el XML al servicio SOAP
        String soapEndpoint = "http://asientocontablews.somee.com/AsientoContableService.asmx";
        try {
            sendSoapRequest(soapEndpoint, soapRequest);
            model.addAttribute("successMessage", "El asiento contable fue contabilizado exitosamente.");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Hubo un error al contabilizar el asiento: " + e.getMessage());
        }

        // Redirigir o renderizar la vista actualizando los mensajes de éxito o error
        return "redirect:/asientos"; // Ajusta esta ruta según tu estructura de vistas
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

    // Método para enviar la solicitud SOAP
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
            return response.toString(); // Devuelve la respuesta del servicio SOAP
        }
        
    }
    
    private String extractIdAsientoFromResponse(String response) {
	    // Aquí puedes usar una librería de parsing XML como JAXB o cualquier otro método para extraer el valor
	    // A continuación, te muestro un ejemplo usando expresiones regulares para obtener el valor entre las etiquetas <IdAsiento>

	    String idAsiento = null;
	    Pattern pattern = Pattern.compile("<IdAsiento>(.*?)</IdAsiento>");
	    Matcher matcher = pattern.matcher(response);
	    if (matcher.find()) {
	        idAsiento = matcher.group(1); // Obtener el valor dentro de las etiquetas
	    }
	    return idAsiento;
	}


  


    // Manejar excepciones
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("Error en el sistema", e);
        model.addAttribute("errorMessage", "Error en el sistema: " + e.getMessage());
        return "error"; // Nombre de la vista para mostrar el error
    }

    // Obtener el formulario de edición para un asiento contable
    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<AsientoContable> getEditAsientoForm(@PathVariable("id") Integer id) {
        AsientoContable asientoContable = asientoContableService.getAsientoContableById(id); // Método para obtener un asiento por ID
        if (asientoContable == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(asientoContable, HttpStatus.OK);
    }

    

   
}
