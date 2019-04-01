/**
 * DDCServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.gwm.www;

public class DDCServiceLocator extends org.apache.axis.client.Service implements cn.gwm.www.DDCService {

    public DDCServiceLocator() {
    }


    public DDCServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DDCServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DDCServiceSoap
    private java.lang.String DDCServiceSoap_address = "http://10.255.16.157:5678/DDC/DDCService.asmx";

    public java.lang.String getDDCServiceSoapAddress() {
        return DDCServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DDCServiceSoapWSDDServiceName = "DDCServiceSoap";

    public java.lang.String getDDCServiceSoapWSDDServiceName() {
        return DDCServiceSoapWSDDServiceName;
    }

    public void setDDCServiceSoapWSDDServiceName(java.lang.String name) {
        DDCServiceSoapWSDDServiceName = name;
    }

    public cn.gwm.www.DDCServiceSoap getDDCServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DDCServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDDCServiceSoap(endpoint);
    }

    public cn.gwm.www.DDCServiceSoap getDDCServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.gwm.www.DDCServiceSoapStub _stub = new cn.gwm.www.DDCServiceSoapStub(portAddress, this);
            _stub.setPortName(getDDCServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDDCServiceSoapEndpointAddress(java.lang.String address) {
        DDCServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cn.gwm.www.DDCServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.gwm.www.DDCServiceSoapStub _stub = new cn.gwm.www.DDCServiceSoapStub(new java.net.URL(DDCServiceSoap_address), this);
                _stub.setPortName(getDDCServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("DDCServiceSoap".equals(inputPortName)) {
            return getDDCServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.gwm.cn/", "DDCService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.gwm.cn/", "DDCServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DDCServiceSoap".equals(portName)) {
            setDDCServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
