package com.sg.facturacion.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sg.facturacion.models.AsientoContable;
import com.sg.facturacion.models.Facturacion;
import com.sg.facturacion.repositories.AsientoContableRepository;
import com.sg.facturacion.repositories.FacturacionRepository;

@Service
public class FacturacionService {

    @Autowired
    private FacturacionRepository facturacionRepository;
    @Autowired
    private AsientoContableRepository asientoContableRepository;

    // Retorna la lista de facturaciones
    public List<Facturacion> listFacturaciones() {
        return facturacionRepository.findAll();
    }

    // Obtiene una facturación por su ID
    public Facturacion getFacturacionById(Integer id) {
        return facturacionRepository.findById(id).orElse(null);
    }

    // Guarda una nueva facturación
    public void saveNew(Facturacion facturacion) {
        facturacionRepository.save(facturacion);
    }

    // Actualiza una facturación existente
    public void updateFacturacion(Facturacion facturacion) {
        Facturacion existingFacturacion = facturacionRepository.findById(facturacion.getId()).orElse(null);
        if (existingFacturacion != null) {
            existingFacturacion.setArticulo(facturacion.getArticulo());
            existingFacturacion.setCliente(facturacion.getCliente());
            existingFacturacion.setVendedor(facturacion.getVendedor());
            existingFacturacion.setFecha(facturacion.getFecha());
            existingFacturacion.setCantidad(facturacion.getCantidad());
            existingFacturacion.setComentario(facturacion.getComentario());
            existingFacturacion.setMontoTotal(facturacion.getMontoTotal());
            facturacionRepository.save(existingFacturacion);
        }
    }
    
    public void asentarFactura(Facturacion factura) {
        AsientoContable asientoContable = new AsientoContable();

        asientoContable.setDescripcion("Asiento generado para la factura #" + factura.getId());
        asientoContable.setMonto(factura.getMontoTotal());
        asientoContable.setCuenta(factura.getMontoTotal());         
        asientoContable.setTipoMovimiento("DEBITO"); // Para facturas, solo es débito
        asientoContable.setFecha(new Date());

        // Guardar el asiento en la base de datos
        asientoContableRepository.save(asientoContable);

        // Opcional: Marca la factura como asentada
        factura.setAsentada(true);
        facturacionRepository.save(factura);
    }

    // Elimina una facturación por su ID
    public void deleteFacturacion(Integer id) {
        facturacionRepository.deleteById(id);
    }
}
