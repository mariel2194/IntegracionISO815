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

import java.util.List;

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
    
    @PostMapping("/asientos/contabilizar")
    public String contabilizar(@RequestParam("id") Integer id) {
        // Fetch the AsientoContable from the database by its ID
        AsientoContable asiento = asientoContableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid AsientoContable ID"));

        // Send the data to the SOAP service

        // Redirect back to the page or return the appropriate response
        return "redirect:/asientos"; // Adjust as per your page structure
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
