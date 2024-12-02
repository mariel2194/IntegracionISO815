package com.sg.facturacion.asientosws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class WebServiceConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.sg.facturacion.asientosws"); // Adjust to your package
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.sg.facturacion.asientosws");

        WebServiceTemplate template = new WebServiceTemplate(marshaller);
        template.setDefaultUri("http://asientocontablews.somee.com/AsientoContableService.asmx");
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);

        return template;
}
}

