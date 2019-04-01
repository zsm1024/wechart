package com.gwm.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.8
 * 2016-11-22T08:37:48.559+08:00
 * Generated source version: 3.1.8
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "Service1Soap")
@XmlSeeAlso({ObjectFactory.class})
public interface Service1Soap {

    @WebResult(name = "parseXmlResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "parseXml", targetNamespace = "http://tempuri.org/", className = "com.gwm.webservice.ParseXml")
    @WebMethod(action = "http://tempuri.org/parseXml")
    @ResponseWrapper(localName = "parseXmlResponse", targetNamespace = "http://tempuri.org/", className = "com.gwm.webservice.ParseXmlResponse")
    public java.lang.String parseXml(
        @WebParam(name = "xml", targetNamespace = "http://tempuri.org/")
        java.lang.String xml
    );
}