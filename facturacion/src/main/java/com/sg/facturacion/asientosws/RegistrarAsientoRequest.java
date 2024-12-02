package com.sg.facturacion.asientosws;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.sg.facturacion.models.AsientoContable;

@XmlRootElement(name = "RegistrarAsiento", namespace = "http://tempuri.org/")
public class RegistrarAsientoRequest {

    public RegistrarAsientoRequest() {
	super();
    }

    private Integer idAuxiliar;
    private Integer cuentadb;
    private Integer cuentacr;
    private double monto;
    private String descripcion;
    
    @XmlElement(name = "idAuxiliar")
    public Integer getidAuxiliar() {
        return idAuxiliar;
    }
    
    public void setidAuxiliar(Integer idAuxiliar) {
        this.idAuxiliar = idAuxiliar;
    }

    @XmlElement(name = "CuentaDB")
    public Integer getCuentadb() {
        return cuentadb;
    }

    public void setCuentadb(Integer cuentadb) {
        this.cuentadb = cuentadb;
    }

    @XmlElement(name = "CuentaCR")
    public Integer getCuentacr() {
        return 0;
    }

    public void setCuentacr(Integer cuentacr) {
        this.cuentacr = 0;
    }

    @XmlElement(name = "Monto")
    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    @XmlElement(name = "Descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Constructor to map from AsientoContable JPA entity
    public RegistrarAsientoRequest(AsientoContable asiento) {
        this.idAuxiliar = asiento.getIdAuxiliar();
	this.cuentadb = asiento.getCuentadb();
        this.cuentacr = asiento.getCuentacr();
        this.monto = asiento.getMonto();
        this.descripcion = asiento.getDescripcion();
    }
}

