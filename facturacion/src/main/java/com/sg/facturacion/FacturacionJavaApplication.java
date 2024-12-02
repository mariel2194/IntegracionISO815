package com.sg.facturacion;

import javax.xml.transform.stream.StreamResult;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.sg.facturacion.asientosws.RegistrarAsientoRequest;
import com.sg.facturacion.asientosws.RegistrarAsientoResponse;

@SpringBootApplication
public class FacturacionJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacturacionJavaApplication.class, args);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		    marshaller.setContextPath("com.sg.facturacion.asientosws");

		    RegistrarAsientoRequest request = new RegistrarAsientoRequest();
		    request.setidAuxiliar(3);
		    request.setDescripcion("Test Asiento Facturacion");
		    request.setCuentadb(100);
		    request.setCuentacr(0);
		    request.setMonto(5000.0);


		    try {
		        marshaller.marshal(request, new StreamResult(System.out));
		        RegistrarAsientoResponse response = new RegistrarAsientoResponse();
		        System.out.println("Id de asiento" + response.getidAsiento());
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

	

}
