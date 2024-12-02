package com.sg.facturacion.models;

import java.util.Date;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@XmlRootElement(name = "AsientoContable")  // Asegura que la clase sea reconocida como un root element de JAXB
@XmlType(propOrder = {"descripcion", "cuentadb", "cuentacr", "monto"})  // Especifica el orden de los campos en el XML

public class AsientoContable {
    
  
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @XmlElement  // Anotación JAXB para que este campo sea mapeado
    @Column(name = "Descripcion", nullable = false)
    private String descripcion;
    
    @Column(name = "idAsiento", nullable = true)
    private Integer idAsiento;

    @Column(name = "IdAuxiliar", nullable = false)
    private Integer idAuxiliar=3;
    @XmlElement  // Anotación JAXB para que este campo sea mapeado

    @Column(name = "CuentaDB", nullable = true)
    private Integer cuentadb;
    @XmlElement  // Anotación JAXB para que este campo sea mapeado

    
    @Column(name = "CuentaCR", nullable = true)
    private Integer cuentacr = 0;

    @Column(name = "Monto", nullable = false)
    private double monto;
    @XmlElement  // Anotación JAXB para que este campo sea mapeado

    @Column(name = "tipoMovimiento", nullable = false)
    private String tipoMovimiento;

    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    

    public Integer getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Integer idAsiento) {
        this.idAsiento = idAsiento;
    }

    public Integer getCuentadb() {
        return cuentadb;
    }

    public void setCuentadb(Integer cuentadb) {
        this.cuentadb = cuentadb;
    }

    public Integer getCuentacr() {
        return cuentacr;
    }

    public void setCuentacr(Integer cuentacr) {
        this.cuentacr = cuentacr;
    }

    public void setIdAuxiliar(Integer idAuxiliar) {
        this.idAuxiliar = idAuxiliar;
    }
   

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getDescripcion() {
	return descripcion;
    }

    public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
    }


    public String getTipoMovimiento() {
	return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
	this.tipoMovimiento = tipoMovimiento;
    }

    public Date getFecha() {
	return fecha;
    }

    public void setFecha(Date fecha) {
	this.fecha = fecha;
    }

    public Integer getIdAuxiliar() {
	return idAuxiliar;
    }


    public double getMonto() {
	return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

}
