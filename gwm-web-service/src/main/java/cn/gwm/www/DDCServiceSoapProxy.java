package cn.gwm.www;

public class DDCServiceSoapProxy implements cn.gwm.www.DDCServiceSoap {
  private String _endpoint = null;
  private cn.gwm.www.DDCServiceSoap dDCServiceSoap = null;
  
  public DDCServiceSoapProxy() {
    _initDDCServiceSoapProxy();
  }
  
  public DDCServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initDDCServiceSoapProxy();
  }
  
  private void _initDDCServiceSoapProxy() {
    try {
      dDCServiceSoap = (new cn.gwm.www.DDCServiceLocator()).getDDCServiceSoap();
      if (dDCServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)dDCServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)dDCServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (dDCServiceSoap != null)
      ((javax.xml.rpc.Stub)dDCServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cn.gwm.www.DDCServiceSoap getDDCServiceSoap() {
    if (dDCServiceSoap == null)
      _initDDCServiceSoapProxy();
    return dDCServiceSoap;
  }
  
  public cn.gwm.www.EnResponse_Shop[] queryShop(cn.gwm.www.User user) throws java.rmi.RemoteException{
    if (dDCServiceSoap == null)
      _initDDCServiceSoapProxy();
    return dDCServiceSoap.queryShop(user);
  }
  
  public cn.gwm.www.EnResponse_Shop[] queryShopByTime(java.lang.String time, cn.gwm.www.User user) throws java.rmi.RemoteException{
    if (dDCServiceSoap == null)
      _initDDCServiceSoapProxy();
    return dDCServiceSoap.queryShopByTime(time, user);
  }
  
  public cn.gwm.www.EnResponse_Shop[] queryShopByTimeTo(java.lang.String timeFrom, java.lang.String timeTo, cn.gwm.www.User user) throws java.rmi.RemoteException{
    if (dDCServiceSoap == null)
      _initDDCServiceSoapProxy();
    return dDCServiceSoap.queryShopByTimeTo(timeFrom, timeTo, user);
  }
  
  public cn.gwm.www.EnResponse_Organization[] queryOrganizaiton(cn.gwm.www.User user) throws java.rmi.RemoteException{
    if (dDCServiceSoap == null)
      _initDDCServiceSoapProxy();
    return dDCServiceSoap.queryOrganizaiton(user);
  }
  
  public cn.gwm.www.EnResponse_Organization[] queryOrganizaitonByTime(java.lang.String time, cn.gwm.www.User user) throws java.rmi.RemoteException{
    if (dDCServiceSoap == null)
      _initDDCServiceSoapProxy();
    return dDCServiceSoap.queryOrganizaitonByTime(time, user);
  }
  
  public cn.gwm.www.EnResponse_Partner[] queryPartner(cn.gwm.www.User user) throws java.rmi.RemoteException{
    if (dDCServiceSoap == null)
      _initDDCServiceSoapProxy();
    return dDCServiceSoap.queryPartner(user);
  }
  
  
}