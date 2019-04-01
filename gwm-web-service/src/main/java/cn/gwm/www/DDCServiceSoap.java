/**
 * DDCServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.gwm.www;

public interface DDCServiceSoap extends java.rmi.Remote {
    public cn.gwm.www.EnResponse_Shop[] queryShop(cn.gwm.www.User user) throws java.rmi.RemoteException;
    public cn.gwm.www.EnResponse_Shop[] queryShopByTime(java.lang.String time, cn.gwm.www.User user) throws java.rmi.RemoteException;
    public cn.gwm.www.EnResponse_Shop[] queryShopByTimeTo(java.lang.String timeFrom, java.lang.String timeTo, cn.gwm.www.User user) throws java.rmi.RemoteException;
    public cn.gwm.www.EnResponse_Organization[] queryOrganizaiton(cn.gwm.www.User user) throws java.rmi.RemoteException;
    public cn.gwm.www.EnResponse_Organization[] queryOrganizaitonByTime(java.lang.String time, cn.gwm.www.User user) throws java.rmi.RemoteException;
    public cn.gwm.www.EnResponse_Partner[] queryPartner(cn.gwm.www.User user) throws java.rmi.RemoteException;
}
