package com.mcnz.spring.soaprest.Configuration;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

@Configuration
@EnableWs
public class WebServiceConfigurationClub {

    @Bean(name = "scoreMessageDispatcherServlet")
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean
    public XsdSchema clubSchema()
    {
        return new SimpleXsdSchema(new ClassPathResource("club.xsd"));
    }

    @Bean(name = "club")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema clubSchema)
    {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ClubPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://soap.jee.mcnz.com/");
        wsdl11Definition.setSchema(clubSchema);
        return wsdl11Definition;
    }

    @Bean
    public PayloadValidatingInterceptor validatingInterceptor() {
        PayloadValidatingInterceptor interceptor = new PayloadValidatingInterceptor();
        // Ги користи вашите XSD дефиниции (класа: ClassPathResource)
        interceptor.setSchema(new ClassPathResource("club.xsd"));
        // Доколку сакате, исто така може да ја валида и response:
        // interceptor.setValidateResponse(true);
        return interceptor;
    }

    /**
     * Регистрирање на нашиот интерцептор во листа од Interceptor-и.
     * Тука ќе се повика validateInterceptor() за секоја влезна порака.
     */

    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(validatingInterceptor());
    }


}
