package com.sg.facturacion.models;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;

public class ObjectFactory {

    private final static QName _AsientoContable_QNAME = new QName("http://tempuri.org/", "AsientoContable");

    public AsientoContable createAsientoContable() {
        return new AsientoContable();
    }

    public JAXBElement<AsientoContable> createAsientoContable(AsientoContable value) {
        return new JAXBElement<AsientoContable>(_AsientoContable_QNAME, AsientoContable.class, null, value);
    }
}
