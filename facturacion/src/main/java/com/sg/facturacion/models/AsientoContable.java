package com.sg.facturacion.models;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import jakarta.persistence.TemporalType;
import java.util.List;

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
public class AsientoContable {
    
  
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Descripcion", nullable = false)
    private String descripcion;
    
    @Column(name = "idAsiento", nullable = true)
    private Integer idAsiento;

    public Integer getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(Integer idAsiento) {
        this.idAsiento = idAsiento;
    }

    @Column(name = "IdAuxiliar", nullable = false)
    private Integer idAuxiliar=3;

    @Column(name = "Cuenta", nullable = true)
    private Integer cuenta;


    @Column(name = "Monto", nullable = false)
    private int monto;

    @Column(name = "tipoMovimiento", nullable = false)
    private String tipoMovimiento;

    @Column(name = "Fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
   

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


    public Integer getCuenta() {
	return cuenta;
    }

    public void setCuenta(Integer cuenta) {
	this.cuenta= cuenta;
    }

    public int getMonto() {
	return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

}
