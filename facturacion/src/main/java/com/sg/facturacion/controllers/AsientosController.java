package com.sg.facturacion.controllers;

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
    private AsientoContableRepository asientoContableRepository; 

    @Autowired
    private ClienteService clienteService; 

    private static final Logger logger = LoggerFactory.getLogger(AsientosController.class);

    @GetMapping
    public String getAsientos(Model model) {
        List<AsientoContable> asientosList = asientoContableService.listAsientosContables(); 
        model.addAttribute("asientos", asientosList);
        return "asientos"; 
    }
    
    @PostMapping("/contabilizar")
    @ResponseBody
    public ResponseEntity<String> contabilizarAsiento(@RequestParam int id) {
        try {
            // Buscar y contabilizar el asiento mediante el servicio
            AsientoContable asiento = asientoContableRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Asiento no encontrado."));

            asientoContableService.contabilizarAsiento(asiento);
            return ResponseEntity.ok("Asiento contabilizado exitosamente con ID SOAP: " + asiento.getIdAsiento());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al contabilizar el asiento: " + e.getMessage());
        }
    }
    

    @PostMapping("/addnew")
    public String addNew(@ModelAttribute AsientoContable asientoContable, Model model) {
        try {
            asientoContableService.saveNew(asientoContable); 
            return "redirect:/asientos";
        } catch (Exception e) {
            logger.error("Error al guardar el asiento contable", e);
            model.addAttribute("errorMessage", "Error al guardar el asiento contable: " + e.getMessage());
            model.addAttribute("asiento", asientoContable);
            return "crearAsiento"; // Nombre de la vista para crear un nuevo asiento
        }
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
