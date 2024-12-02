package com.sg.facturacion.services;

import java.util.List;
import com.sg.facturacion.asientosws.WebServiceConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.sg.facturacion.asientosws.AsientoContableServiceClient;
import com.sg.facturacion.models.AsientoContable;
import com.sg.facturacion.repositories.AsientoContableRepository;
@Service
public class AsientoContableService {

    @Autowired
    private AsientoContableRepository asientoContableRepository;
    
    @Autowired
    private WebServiceTemplate webServiceTemplate;


    private static final Logger logger = LoggerFactory.getLogger(AsientoContableService.class);

    public List<AsientoContable> listAsientosContables() {
        return asientoContableRepository.findAll();
    }

    public AsientoContable getAsientoContableById(Integer id) {
        return asientoContableRepository.findById(id).orElse(null);
    }

    // Método para contabilizar un asiento contable
    public AsientoContable contabilizarAsiento(AsientoContable asientoContable) {
        try {
            String respuesta = (String)  webServiceTemplate.marshalSendAndReceive(asientoContable);

        } catch (Exception e) {
            logger.error("Error al contactar con el servicio SOAP", e);
            throw new RuntimeException("Error al registrar el asiento contable a través del servicio SOAP.");
        }

        return asientoContable;
    }

    // Guardar un nuevo asiento contable
    public void saveNew(AsientoContable asientoContable) {
        asientoContableRepository.save(asientoContable);
    }

    // Actualizar un asiento contable existente
    public void updateAsientoContable(AsientoContable asientoContable) {
        AsientoContable existingAsientoContable = asientoContableRepository.findById(asientoContable.getId()).orElse(null);
        if (existingAsientoContable != null) {
            existingAsientoContable.setDescripcion(asientoContable.getDescripcion());
            existingAsientoContable.setCuentadb(asientoContable.getCuentadb());
            existingAsientoContable.setTipoMovimiento(asientoContable.getTipoMovimiento());
            existingAsientoContable.setFecha(asientoContable.getFecha());
            existingAsientoContable.setMonto(asientoContable.getMonto());
            asientoContableRepository.save(existingAsientoContable);
        }
    }

    // Eliminar un asiento contable
    public void deleteAsientoContable(Integer id) {
        asientoContableRepository.deleteById(id);
    }
}
