package com.sg.facturacion.controllers;

import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sg.facturacion.models.Articulos;
import com.sg.facturacion.models.AsientoContable;
import com.sg.facturacion.models.Clientes;
import com.sg.facturacion.models.Facturacion;
import com.sg.facturacion.models.Vendedores;
import com.sg.facturacion.services.ArticulosService;
import com.sg.facturacion.services.AsientoContableService;
import com.sg.facturacion.services.ClienteService;
import com.sg.facturacion.services.VendedoresService;

import com.sg.facturacion.services.FacturacionService;

import org.slf4j.Logger;

@Controller

public class FacturacionController {

    @Autowired
    private FacturacionService facturacionService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VendedoresService vendedorService;
    
    @Autowired
    private AsientoContableService asientoContableService;

    @Autowired
    private ArticulosService articuloService;

    private static final Logger logger = LoggerFactory.getLogger(FacturacionController.class);

    @GetMapping("/facturacion")
    public String getFacturaciones(Model model) {
        List<Facturacion> facturasList = facturacionService.listFacturaciones();
        List<Clientes> clientesList = clienteService.listClientes();
        List<Vendedores> vendedoresList = vendedorService.listVendedores();
        List<Articulos> articulosList = articuloService.listArticulos();

        model.addAttribute("facturaciones", facturasList);
        model.addAttribute("clientes", clientesList);
        model.addAttribute("vendedores", vendedoresList);
        model.addAttribute("articulos", articulosList);

        return "facturacion";
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping("/facturacion/addnew")
    public String addNew(Facturacion facturacion, Model model) {
        try {
            facturacionService.saveNew(facturacion);
            return "redirect:/facturacion";
        } catch (Exception e) {
            logger.error("Error al guardar la facturación", e);
            model.addAttribute("errorMessage", "Error al guardar la facturación: " + e.getMessage());
            model.addAttribute("facturacion", facturacion);

            // Reagregar listas en caso de error para mantenerlas en la vista
            model.addAttribute("clientes", clienteService.listClientes());
            model.addAttribute("vendedores", vendedorService.listVendedores());
            model.addAttribute("articulos", articuloService.listArticulos());

            return "crearFacturacion";
        }
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        logger.error("Error en el sistema", e);
        model.addAttribute("errorMessage", "Error en el sistema: " + e.getMessage());
        return "error";
    }

    @GetMapping("/facturacion/edit/{id}")
    @ResponseBody
    public ResponseEntity<Facturacion> getEditFacturacionForm(@PathVariable("id") Integer id) {
        Facturacion facturacion = facturacionService.getFacturacionById(id);
        if (facturacion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(facturacion, HttpStatus.OK);
    }

    @PostMapping("/facturacion/update")
    public String updateFacturacion(@ModelAttribute Facturacion facturacion) {
        facturacionService.updateFacturacion(facturacion);
        return "redirect:/facturacion";
    }
    
    @PostMapping("/facturacion/asentar/{id}")
    public String asentarFactura(@PathVariable("id") Integer id, Model model) {
        try {
            logger.debug("Iniciando proceso de asentar la factura con ID: {}", id);
            Facturacion factura = facturacionService.getFacturacionById(id);
            if (factura == null) {
                model.addAttribute("errorMessage", "Factura no encontrada");
                logger.error("Factura con ID {} no encontrada", id);
                return "error"; 
            }

            if (factura.getAsentada()) {
                model.addAttribute("errorMessage", "La factura ya está asentada");
                logger.error("La factura con ID {} ya está asentada", id);
                return "error";
            }

            // Lógica para generar el asiento contable...
            // (aquí agregarías más detalles de logs según sea necesario)
            logger.debug("Generando asiento contable para la factura con ID: {}", id);
            AsientoContable asientoContable = new AsientoContable();
            asientoContable.setDescripcion("Asiento generado para la factura ID: " + factura.getId());
            asientoContable.setMonto(factura.getMontoTotal());
            asientoContable.setCuenta((int)factura.getMontoTotal());
            asientoContable.setTipoMovimiento("DEBITO");
            asientoContable.setFecha(factura.getFecha());

            asientoContableService.saveNew(asientoContable);
            factura.setAsentada(true);
            facturacionService.updateFacturacion(factura);

            logger.debug("Factura con ID: {} ha sido asentada correctamente", id);
            return "redirect:/asientos"; 
        } catch (Exception e) {
            logger.error("Error al asentar la factura con ID: {}", id, e);
            model.addAttribute("errorMessage", "Error al asentar la factura: " + e.getMessage());
            return "error";
        }
    }
    

    @PostMapping("/facturacion/delete/{id}")
    public String deleteFacturacion(@PathVariable("id") Integer id) {
        facturacionService.deleteFacturacion(id);
        return "redirect:/facturacion";
    }
}
