/**
 * EnResponse_Shop.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.gwm.www;

public class EnResponse_Shop  implements java.io.Serializable {
    private int sh_ID;

    private java.lang.String sh_number;

    private java.lang.String sh_name;

    private java.lang.String sh_shortName;

    private java.lang.String oz_number;

    private java.lang.String oz_shortname;

    private java.lang.String sh_storeName;

    private java.lang.String sh_storeAbbre;

    private java.lang.String sh_serviceStoreName;

    private java.lang.String sh_serviceAbbre;

    private java.lang.String sh_state;

    private java.lang.String sh_serviceState;

    private java.lang.String sh_type;

    private java.lang.String sh_grade;

    private java.lang.String sh_category;

    private java.lang.String sh_province;

    private java.lang.String sh_city;

    private java.lang.String sh_county;

    private java.lang.String sh_address;

    private java.lang.String sh_stationAddress;

    private float sh_salelongitude;

    private float sh_salelatitude;

    private float sh_longitude;

    private float sh_latitude;

    private java.lang.String agentBrands;

    private java.lang.String sh_serviceCategory;

    private java.lang.String sh_salesCooper;

    private java.lang.String sh_serviceendDate;

    private java.lang.String sh_createTime;

    private java.lang.String sh_lastUpdateTime;

    private java.lang.String sh_postCode;

    private java.lang.String sh_saleHotline;

    private java.lang.String sh_saleEmail;

    private java.lang.String sh_saleFax;

    private java.lang.String sh_serviceHotline;

    private java.lang.String sh_serviceFax;

    private java.lang.String sh_serviceEmail;

    private java.lang.String sh_serviceTime;

    private java.lang.String sh_serviceHours;

    private java.lang.String sh_stationMaster;

    private java.lang.String sh_stMasterTel;

    private java.lang.String sh_emergencyHotline;

    private java.lang.String sh_parent;

    private java.lang.String sh_parentid;

    private java.lang.String sh_servicecontactuser;

    private java.lang.String sh_replacementphone;

    private java.lang.String sh_remark;

    private java.lang.String sh_saleHours;

    private java.lang.String sh_cooperateDate;

    private java.lang.String sh_startDate;

    private java.lang.String sh_generalManager;

    private java.lang.String sh_generalTel;

    private java.lang.String sh_endDate;

    private java.lang.String sh_serviceWarningDate;

    private java.lang.String sh_buildType;

    private java.lang.String sh_layout;

    private java.lang.String sh_saleCarBrand;

    private java.lang.String sh_claimRejectedSeries;

    private java.lang.String sh_claimRight;

    private java.lang.String sh_maintenanceRight;

    private java.lang.String sh_orderRight;

    private java.lang.String sh_spareParts;

    private java.lang.String oz_ID;

    private java.lang.Integer rank;

    private java.lang.String sh_operationLife;

    private java.lang.String sh_serviceLife;

    private java.lang.String sh_financeCoorp;

    private java.lang.String sh_financeCoorpDate;

    private java.lang.String sh_financeCoorpEndDate;

    private java.lang.String sh_shopImage;

    private java.lang.Integer oz_IdGroup;

    private java.lang.String oz_GroupName;

    private java.lang.Integer oz_IdInvestors;

    private java.lang.String oz_InvestorsName;

    private java.lang.String sh_property;

    private java.lang.String sh_costomerMix;

    private java.lang.String msg;

    public EnResponse_Shop() {
    }

    public EnResponse_Shop(
           int sh_ID,
           java.lang.String sh_number,
           java.lang.String sh_name,
           java.lang.String sh_shortName,
           java.lang.String oz_number,
           java.lang.String oz_shortname,
           java.lang.String sh_storeName,
           java.lang.String sh_storeAbbre,
           java.lang.String sh_serviceStoreName,
           java.lang.String sh_serviceAbbre,
           java.lang.String sh_state,
           java.lang.String sh_serviceState,
           java.lang.String sh_type,
           java.lang.String sh_grade,
           java.lang.String sh_category,
           java.lang.String sh_province,
           java.lang.String sh_city,
           java.lang.String sh_county,
           java.lang.String sh_address,
           java.lang.String sh_stationAddress,
           float sh_salelongitude,
           float sh_salelatitude,
           float sh_longitude,
           float sh_latitude,
           java.lang.String agentBrands,
           java.lang.String sh_serviceCategory,
           java.lang.String sh_salesCooper,
           java.lang.String sh_serviceendDate,
           java.lang.String sh_createTime,
           java.lang.String sh_lastUpdateTime,
           java.lang.String sh_postCode,
           java.lang.String sh_saleHotline,
           java.lang.String sh_saleEmail,
           java.lang.String sh_saleFax,
           java.lang.String sh_serviceHotline,
           java.lang.String sh_serviceFax,
           java.lang.String sh_serviceEmail,
           java.lang.String sh_serviceTime,
           java.lang.String sh_serviceHours,
           java.lang.String sh_stationMaster,
           java.lang.String sh_stMasterTel,
           java.lang.String sh_emergencyHotline,
           java.lang.String sh_parent,
           java.lang.String sh_parentid,
           java.lang.String sh_servicecontactuser,
           java.lang.String sh_replacementphone,
           java.lang.String sh_remark,
           java.lang.String sh_saleHours,
           java.lang.String sh_cooperateDate,
           java.lang.String sh_startDate,
           java.lang.String sh_generalManager,
           java.lang.String sh_generalTel,
           java.lang.String sh_endDate,
           java.lang.String sh_serviceWarningDate,
           java.lang.String sh_buildType,
           java.lang.String sh_layout,
           java.lang.String sh_saleCarBrand,
           java.lang.String sh_claimRejectedSeries,
           java.lang.String sh_claimRight,
           java.lang.String sh_maintenanceRight,
           java.lang.String sh_orderRight,
           java.lang.String sh_spareParts,
           java.lang.String oz_ID,
           java.lang.Integer rank,
           java.lang.String sh_operationLife,
           java.lang.String sh_serviceLife,
           java.lang.String sh_financeCoorp,
           java.lang.String sh_financeCoorpDate,
           java.lang.String sh_financeCoorpEndDate,
           java.lang.String sh_shopImage,
           java.lang.Integer oz_IdGroup,
           java.lang.String oz_GroupName,
           java.lang.Integer oz_IdInvestors,
           java.lang.String oz_InvestorsName,
           java.lang.String sh_property,
           java.lang.String sh_costomerMix,
           java.lang.String msg) {
           this.sh_ID = sh_ID;
           this.sh_number = sh_number;
           this.sh_name = sh_name;
           this.sh_shortName = sh_shortName;
           this.oz_number = oz_number;
           this.oz_shortname = oz_shortname;
           this.sh_storeName = sh_storeName;
           this.sh_storeAbbre = sh_storeAbbre;
           this.sh_serviceStoreName = sh_serviceStoreName;
           this.sh_serviceAbbre = sh_serviceAbbre;
           this.sh_state = sh_state;
           this.sh_serviceState = sh_serviceState;
           this.sh_type = sh_type;
           this.sh_grade = sh_grade;
           this.sh_category = sh_category;
           this.sh_province = sh_province;
           this.sh_city = sh_city;
           this.sh_county = sh_county;
           this.sh_address = sh_address;
           this.sh_stationAddress = sh_stationAddress;
           this.sh_salelongitude = sh_salelongitude;
           this.sh_salelatitude = sh_salelatitude;
           this.sh_longitude = sh_longitude;
           this.sh_latitude = sh_latitude;
           this.agentBrands = agentBrands;
           this.sh_serviceCategory = sh_serviceCategory;
           this.sh_salesCooper = sh_salesCooper;
           this.sh_serviceendDate = sh_serviceendDate;
           this.sh_createTime = sh_createTime;
           this.sh_lastUpdateTime = sh_lastUpdateTime;
           this.sh_postCode = sh_postCode;
           this.sh_saleHotline = sh_saleHotline;
           this.sh_saleEmail = sh_saleEmail;
           this.sh_saleFax = sh_saleFax;
           this.sh_serviceHotline = sh_serviceHotline;
           this.sh_serviceFax = sh_serviceFax;
           this.sh_serviceEmail = sh_serviceEmail;
           this.sh_serviceTime = sh_serviceTime;
           this.sh_serviceHours = sh_serviceHours;
           this.sh_stationMaster = sh_stationMaster;
           this.sh_stMasterTel = sh_stMasterTel;
           this.sh_emergencyHotline = sh_emergencyHotline;
           this.sh_parent = sh_parent;
           this.sh_parentid = sh_parentid;
           this.sh_servicecontactuser = sh_servicecontactuser;
           this.sh_replacementphone = sh_replacementphone;
           this.sh_remark = sh_remark;
           this.sh_saleHours = sh_saleHours;
           this.sh_cooperateDate = sh_cooperateDate;
           this.sh_startDate = sh_startDate;
           this.sh_generalManager = sh_generalManager;
           this.sh_generalTel = sh_generalTel;
           this.sh_endDate = sh_endDate;
           this.sh_serviceWarningDate = sh_serviceWarningDate;
           this.sh_buildType = sh_buildType;
           this.sh_layout = sh_layout;
           this.sh_saleCarBrand = sh_saleCarBrand;
           this.sh_claimRejectedSeries = sh_claimRejectedSeries;
           this.sh_claimRight = sh_claimRight;
           this.sh_maintenanceRight = sh_maintenanceRight;
           this.sh_orderRight = sh_orderRight;
           this.sh_spareParts = sh_spareParts;
           this.oz_ID = oz_ID;
           this.rank = rank;
           this.sh_operationLife = sh_operationLife;
           this.sh_serviceLife = sh_serviceLife;
           this.sh_financeCoorp = sh_financeCoorp;
           this.sh_financeCoorpDate = sh_financeCoorpDate;
           this.sh_financeCoorpEndDate = sh_financeCoorpEndDate;
           this.sh_shopImage = sh_shopImage;
           this.oz_IdGroup = oz_IdGroup;
           this.oz_GroupName = oz_GroupName;
           this.oz_IdInvestors = oz_IdInvestors;
           this.oz_InvestorsName = oz_InvestorsName;
           this.sh_property = sh_property;
           this.sh_costomerMix = sh_costomerMix;
           this.msg = msg;
    }


    /**
     * Gets the sh_ID value for this EnResponse_Shop.
     * 
     * @return sh_ID
     */
    public int getSh_ID() {
        return sh_ID;
    }


    /**
     * Sets the sh_ID value for this EnResponse_Shop.
     * 
     * @param sh_ID
     */
    public void setSh_ID(int sh_ID) {
        this.sh_ID = sh_ID;
    }


    /**
     * Gets the sh_number value for this EnResponse_Shop.
     * 
     * @return sh_number
     */
    public java.lang.String getSh_number() {
        return sh_number;
    }


    /**
     * Sets the sh_number value for this EnResponse_Shop.
     * 
     * @param sh_number
     */
    public void setSh_number(java.lang.String sh_number) {
        this.sh_number = sh_number;
    }


    /**
     * Gets the sh_name value for this EnResponse_Shop.
     * 
     * @return sh_name
     */
    public java.lang.String getSh_name() {
        return sh_name;
    }


    /**
     * Sets the sh_name value for this EnResponse_Shop.
     * 
     * @param sh_name
     */
    public void setSh_name(java.lang.String sh_name) {
        this.sh_name = sh_name;
    }


    /**
     * Gets the sh_shortName value for this EnResponse_Shop.
     * 
     * @return sh_shortName
     */
    public java.lang.String getSh_shortName() {
        return sh_shortName;
    }


    /**
     * Sets the sh_shortName value for this EnResponse_Shop.
     * 
     * @param sh_shortName
     */
    public void setSh_shortName(java.lang.String sh_shortName) {
        this.sh_shortName = sh_shortName;
    }


    /**
     * Gets the oz_number value for this EnResponse_Shop.
     * 
     * @return oz_number
     */
    public java.lang.String getOz_number() {
        return oz_number;
    }


    /**
     * Sets the oz_number value for this EnResponse_Shop.
     * 
     * @param oz_number
     */
    public void setOz_number(java.lang.String oz_number) {
        this.oz_number = oz_number;
    }


    /**
     * Gets the oz_shortname value for this EnResponse_Shop.
     * 
     * @return oz_shortname
     */
    public java.lang.String getOz_shortname() {
        return oz_shortname;
    }


    /**
     * Sets the oz_shortname value for this EnResponse_Shop.
     * 
     * @param oz_shortname
     */
    public void setOz_shortname(java.lang.String oz_shortname) {
        this.oz_shortname = oz_shortname;
    }


    /**
     * Gets the sh_storeName value for this EnResponse_Shop.
     * 
     * @return sh_storeName
     */
    public java.lang.String getSh_storeName() {
        return sh_storeName;
    }


    /**
     * Sets the sh_storeName value for this EnResponse_Shop.
     * 
     * @param sh_storeName
     */
    public void setSh_storeName(java.lang.String sh_storeName) {
        this.sh_storeName = sh_storeName;
    }


    /**
     * Gets the sh_storeAbbre value for this EnResponse_Shop.
     * 
     * @return sh_storeAbbre
     */
    public java.lang.String getSh_storeAbbre() {
        return sh_storeAbbre;
    }


    /**
     * Sets the sh_storeAbbre value for this EnResponse_Shop.
     * 
     * @param sh_storeAbbre
     */
    public void setSh_storeAbbre(java.lang.String sh_storeAbbre) {
        this.sh_storeAbbre = sh_storeAbbre;
    }


    /**
     * Gets the sh_serviceStoreName value for this EnResponse_Shop.
     * 
     * @return sh_serviceStoreName
     */
    public java.lang.String getSh_serviceStoreName() {
        return sh_serviceStoreName;
    }


    /**
     * Sets the sh_serviceStoreName value for this EnResponse_Shop.
     * 
     * @param sh_serviceStoreName
     */
    public void setSh_serviceStoreName(java.lang.String sh_serviceStoreName) {
        this.sh_serviceStoreName = sh_serviceStoreName;
    }


    /**
     * Gets the sh_serviceAbbre value for this EnResponse_Shop.
     * 
     * @return sh_serviceAbbre
     */
    public java.lang.String getSh_serviceAbbre() {
        return sh_serviceAbbre;
    }


    /**
     * Sets the sh_serviceAbbre value for this EnResponse_Shop.
     * 
     * @param sh_serviceAbbre
     */
    public void setSh_serviceAbbre(java.lang.String sh_serviceAbbre) {
        this.sh_serviceAbbre = sh_serviceAbbre;
    }


    /**
     * Gets the sh_state value for this EnResponse_Shop.
     * 
     * @return sh_state
     */
    public java.lang.String getSh_state() {
        return sh_state;
    }


    /**
     * Sets the sh_state value for this EnResponse_Shop.
     * 
     * @param sh_state
     */
    public void setSh_state(java.lang.String sh_state) {
        this.sh_state = sh_state;
    }


    /**
     * Gets the sh_serviceState value for this EnResponse_Shop.
     * 
     * @return sh_serviceState
     */
    public java.lang.String getSh_serviceState() {
        return sh_serviceState;
    }


    /**
     * Sets the sh_serviceState value for this EnResponse_Shop.
     * 
     * @param sh_serviceState
     */
    public void setSh_serviceState(java.lang.String sh_serviceState) {
        this.sh_serviceState = sh_serviceState;
    }


    /**
     * Gets the sh_type value for this EnResponse_Shop.
     * 
     * @return sh_type
     */
    public java.lang.String getSh_type() {
        return sh_type;
    }


    /**
     * Sets the sh_type value for this EnResponse_Shop.
     * 
     * @param sh_type
     */
    public void setSh_type(java.lang.String sh_type) {
        this.sh_type = sh_type;
    }


    /**
     * Gets the sh_grade value for this EnResponse_Shop.
     * 
     * @return sh_grade
     */
    public java.lang.String getSh_grade() {
        return sh_grade;
    }


    /**
     * Sets the sh_grade value for this EnResponse_Shop.
     * 
     * @param sh_grade
     */
    public void setSh_grade(java.lang.String sh_grade) {
        this.sh_grade = sh_grade;
    }


    /**
     * Gets the sh_category value for this EnResponse_Shop.
     * 
     * @return sh_category
     */
    public java.lang.String getSh_category() {
        return sh_category;
    }


    /**
     * Sets the sh_category value for this EnResponse_Shop.
     * 
     * @param sh_category
     */
    public void setSh_category(java.lang.String sh_category) {
        this.sh_category = sh_category;
    }


    /**
     * Gets the sh_province value for this EnResponse_Shop.
     * 
     * @return sh_province
     */
    public java.lang.String getSh_province() {
        return sh_province;
    }


    /**
     * Sets the sh_province value for this EnResponse_Shop.
     * 
     * @param sh_province
     */
    public void setSh_province(java.lang.String sh_province) {
        this.sh_province = sh_province;
    }


    /**
     * Gets the sh_city value for this EnResponse_Shop.
     * 
     * @return sh_city
     */
    public java.lang.String getSh_city() {
        return sh_city;
    }


    /**
     * Sets the sh_city value for this EnResponse_Shop.
     * 
     * @param sh_city
     */
    public void setSh_city(java.lang.String sh_city) {
        this.sh_city = sh_city;
    }


    /**
     * Gets the sh_county value for this EnResponse_Shop.
     * 
     * @return sh_county
     */
    public java.lang.String getSh_county() {
        return sh_county;
    }


    /**
     * Sets the sh_county value for this EnResponse_Shop.
     * 
     * @param sh_county
     */
    public void setSh_county(java.lang.String sh_county) {
        this.sh_county = sh_county;
    }


    /**
     * Gets the sh_address value for this EnResponse_Shop.
     * 
     * @return sh_address
     */
    public java.lang.String getSh_address() {
        return sh_address;
    }


    /**
     * Sets the sh_address value for this EnResponse_Shop.
     * 
     * @param sh_address
     */
    public void setSh_address(java.lang.String sh_address) {
        this.sh_address = sh_address;
    }


    /**
     * Gets the sh_stationAddress value for this EnResponse_Shop.
     * 
     * @return sh_stationAddress
     */
    public java.lang.String getSh_stationAddress() {
        return sh_stationAddress;
    }


    /**
     * Sets the sh_stationAddress value for this EnResponse_Shop.
     * 
     * @param sh_stationAddress
     */
    public void setSh_stationAddress(java.lang.String sh_stationAddress) {
        this.sh_stationAddress = sh_stationAddress;
    }


    /**
     * Gets the sh_salelongitude value for this EnResponse_Shop.
     * 
     * @return sh_salelongitude
     */
    public float getSh_salelongitude() {
        return sh_salelongitude;
    }


    /**
     * Sets the sh_salelongitude value for this EnResponse_Shop.
     * 
     * @param sh_salelongitude
     */
    public void setSh_salelongitude(float sh_salelongitude) {
        this.sh_salelongitude = sh_salelongitude;
    }


    /**
     * Gets the sh_salelatitude value for this EnResponse_Shop.
     * 
     * @return sh_salelatitude
     */
    public float getSh_salelatitude() {
        return sh_salelatitude;
    }


    /**
     * Sets the sh_salelatitude value for this EnResponse_Shop.
     * 
     * @param sh_salelatitude
     */
    public void setSh_salelatitude(float sh_salelatitude) {
        this.sh_salelatitude = sh_salelatitude;
    }


    /**
     * Gets the sh_longitude value for this EnResponse_Shop.
     * 
     * @return sh_longitude
     */
    public float getSh_longitude() {
        return sh_longitude;
    }


    /**
     * Sets the sh_longitude value for this EnResponse_Shop.
     * 
     * @param sh_longitude
     */
    public void setSh_longitude(float sh_longitude) {
        this.sh_longitude = sh_longitude;
    }


    /**
     * Gets the sh_latitude value for this EnResponse_Shop.
     * 
     * @return sh_latitude
     */
    public float getSh_latitude() {
        return sh_latitude;
    }


    /**
     * Sets the sh_latitude value for this EnResponse_Shop.
     * 
     * @param sh_latitude
     */
    public void setSh_latitude(float sh_latitude) {
        this.sh_latitude = sh_latitude;
    }


    /**
     * Gets the agentBrands value for this EnResponse_Shop.
     * 
     * @return agentBrands
     */
    public java.lang.String getAgentBrands() {
        return agentBrands;
    }


    /**
     * Sets the agentBrands value for this EnResponse_Shop.
     * 
     * @param agentBrands
     */
    public void setAgentBrands(java.lang.String agentBrands) {
        this.agentBrands = agentBrands;
    }


    /**
     * Gets the sh_serviceCategory value for this EnResponse_Shop.
     * 
     * @return sh_serviceCategory
     */
    public java.lang.String getSh_serviceCategory() {
        return sh_serviceCategory;
    }


    /**
     * Sets the sh_serviceCategory value for this EnResponse_Shop.
     * 
     * @param sh_serviceCategory
     */
    public void setSh_serviceCategory(java.lang.String sh_serviceCategory) {
        this.sh_serviceCategory = sh_serviceCategory;
    }


    /**
     * Gets the sh_salesCooper value for this EnResponse_Shop.
     * 
     * @return sh_salesCooper
     */
    public java.lang.String getSh_salesCooper() {
        return sh_salesCooper;
    }


    /**
     * Sets the sh_salesCooper value for this EnResponse_Shop.
     * 
     * @param sh_salesCooper
     */
    public void setSh_salesCooper(java.lang.String sh_salesCooper) {
        this.sh_salesCooper = sh_salesCooper;
    }


    /**
     * Gets the sh_serviceendDate value for this EnResponse_Shop.
     * 
     * @return sh_serviceendDate
     */
    public java.lang.String getSh_serviceendDate() {
        return sh_serviceendDate;
    }


    /**
     * Sets the sh_serviceendDate value for this EnResponse_Shop.
     * 
     * @param sh_serviceendDate
     */
    public void setSh_serviceendDate(java.lang.String sh_serviceendDate) {
        this.sh_serviceendDate = sh_serviceendDate;
    }


    /**
     * Gets the sh_createTime value for this EnResponse_Shop.
     * 
     * @return sh_createTime
     */
    public java.lang.String getSh_createTime() {
        return sh_createTime;
    }


    /**
     * Sets the sh_createTime value for this EnResponse_Shop.
     * 
     * @param sh_createTime
     */
    public void setSh_createTime(java.lang.String sh_createTime) {
        this.sh_createTime = sh_createTime;
    }


    /**
     * Gets the sh_lastUpdateTime value for this EnResponse_Shop.
     * 
     * @return sh_lastUpdateTime
     */
    public java.lang.String getSh_lastUpdateTime() {
        return sh_lastUpdateTime;
    }


    /**
     * Sets the sh_lastUpdateTime value for this EnResponse_Shop.
     * 
     * @param sh_lastUpdateTime
     */
    public void setSh_lastUpdateTime(java.lang.String sh_lastUpdateTime) {
        this.sh_lastUpdateTime = sh_lastUpdateTime;
    }


    /**
     * Gets the sh_postCode value for this EnResponse_Shop.
     * 
     * @return sh_postCode
     */
    public java.lang.String getSh_postCode() {
        return sh_postCode;
    }


    /**
     * Sets the sh_postCode value for this EnResponse_Shop.
     * 
     * @param sh_postCode
     */
    public void setSh_postCode(java.lang.String sh_postCode) {
        this.sh_postCode = sh_postCode;
    }


    /**
     * Gets the sh_saleHotline value for this EnResponse_Shop.
     * 
     * @return sh_saleHotline
     */
    public java.lang.String getSh_saleHotline() {
        return sh_saleHotline;
    }


    /**
     * Sets the sh_saleHotline value for this EnResponse_Shop.
     * 
     * @param sh_saleHotline
     */
    public void setSh_saleHotline(java.lang.String sh_saleHotline) {
        this.sh_saleHotline = sh_saleHotline;
    }


    /**
     * Gets the sh_saleEmail value for this EnResponse_Shop.
     * 
     * @return sh_saleEmail
     */
    public java.lang.String getSh_saleEmail() {
        return sh_saleEmail;
    }


    /**
     * Sets the sh_saleEmail value for this EnResponse_Shop.
     * 
     * @param sh_saleEmail
     */
    public void setSh_saleEmail(java.lang.String sh_saleEmail) {
        this.sh_saleEmail = sh_saleEmail;
    }


    /**
     * Gets the sh_saleFax value for this EnResponse_Shop.
     * 
     * @return sh_saleFax
     */
    public java.lang.String getSh_saleFax() {
        return sh_saleFax;
    }


    /**
     * Sets the sh_saleFax value for this EnResponse_Shop.
     * 
     * @param sh_saleFax
     */
    public void setSh_saleFax(java.lang.String sh_saleFax) {
        this.sh_saleFax = sh_saleFax;
    }


    /**
     * Gets the sh_serviceHotline value for this EnResponse_Shop.
     * 
     * @return sh_serviceHotline
     */
    public java.lang.String getSh_serviceHotline() {
        return sh_serviceHotline;
    }


    /**
     * Sets the sh_serviceHotline value for this EnResponse_Shop.
     * 
     * @param sh_serviceHotline
     */
    public void setSh_serviceHotline(java.lang.String sh_serviceHotline) {
        this.sh_serviceHotline = sh_serviceHotline;
    }


    /**
     * Gets the sh_serviceFax value for this EnResponse_Shop.
     * 
     * @return sh_serviceFax
     */
    public java.lang.String getSh_serviceFax() {
        return sh_serviceFax;
    }


    /**
     * Sets the sh_serviceFax value for this EnResponse_Shop.
     * 
     * @param sh_serviceFax
     */
    public void setSh_serviceFax(java.lang.String sh_serviceFax) {
        this.sh_serviceFax = sh_serviceFax;
    }


    /**
     * Gets the sh_serviceEmail value for this EnResponse_Shop.
     * 
     * @return sh_serviceEmail
     */
    public java.lang.String getSh_serviceEmail() {
        return sh_serviceEmail;
    }


    /**
     * Sets the sh_serviceEmail value for this EnResponse_Shop.
     * 
     * @param sh_serviceEmail
     */
    public void setSh_serviceEmail(java.lang.String sh_serviceEmail) {
        this.sh_serviceEmail = sh_serviceEmail;
    }


    /**
     * Gets the sh_serviceTime value for this EnResponse_Shop.
     * 
     * @return sh_serviceTime
     */
    public java.lang.String getSh_serviceTime() {
        return sh_serviceTime;
    }


    /**
     * Sets the sh_serviceTime value for this EnResponse_Shop.
     * 
     * @param sh_serviceTime
     */
    public void setSh_serviceTime(java.lang.String sh_serviceTime) {
        this.sh_serviceTime = sh_serviceTime;
    }


    /**
     * Gets the sh_serviceHours value for this EnResponse_Shop.
     * 
     * @return sh_serviceHours
     */
    public java.lang.String getSh_serviceHours() {
        return sh_serviceHours;
    }


    /**
     * Sets the sh_serviceHours value for this EnResponse_Shop.
     * 
     * @param sh_serviceHours
     */
    public void setSh_serviceHours(java.lang.String sh_serviceHours) {
        this.sh_serviceHours = sh_serviceHours;
    }


    /**
     * Gets the sh_stationMaster value for this EnResponse_Shop.
     * 
     * @return sh_stationMaster
     */
    public java.lang.String getSh_stationMaster() {
        return sh_stationMaster;
    }


    /**
     * Sets the sh_stationMaster value for this EnResponse_Shop.
     * 
     * @param sh_stationMaster
     */
    public void setSh_stationMaster(java.lang.String sh_stationMaster) {
        this.sh_stationMaster = sh_stationMaster;
    }


    /**
     * Gets the sh_stMasterTel value for this EnResponse_Shop.
     * 
     * @return sh_stMasterTel
     */
    public java.lang.String getSh_stMasterTel() {
        return sh_stMasterTel;
    }


    /**
     * Sets the sh_stMasterTel value for this EnResponse_Shop.
     * 
     * @param sh_stMasterTel
     */
    public void setSh_stMasterTel(java.lang.String sh_stMasterTel) {
        this.sh_stMasterTel = sh_stMasterTel;
    }


    /**
     * Gets the sh_emergencyHotline value for this EnResponse_Shop.
     * 
     * @return sh_emergencyHotline
     */
    public java.lang.String getSh_emergencyHotline() {
        return sh_emergencyHotline;
    }


    /**
     * Sets the sh_emergencyHotline value for this EnResponse_Shop.
     * 
     * @param sh_emergencyHotline
     */
    public void setSh_emergencyHotline(java.lang.String sh_emergencyHotline) {
        this.sh_emergencyHotline = sh_emergencyHotline;
    }


    /**
     * Gets the sh_parent value for this EnResponse_Shop.
     * 
     * @return sh_parent
     */
    public java.lang.String getSh_parent() {
        return sh_parent;
    }


    /**
     * Sets the sh_parent value for this EnResponse_Shop.
     * 
     * @param sh_parent
     */
    public void setSh_parent(java.lang.String sh_parent) {
        this.sh_parent = sh_parent;
    }


    /**
     * Gets the sh_parentid value for this EnResponse_Shop.
     * 
     * @return sh_parentid
     */
    public java.lang.String getSh_parentid() {
        return sh_parentid;
    }


    /**
     * Sets the sh_parentid value for this EnResponse_Shop.
     * 
     * @param sh_parentid
     */
    public void setSh_parentid(java.lang.String sh_parentid) {
        this.sh_parentid = sh_parentid;
    }


    /**
     * Gets the sh_servicecontactuser value for this EnResponse_Shop.
     * 
     * @return sh_servicecontactuser
     */
    public java.lang.String getSh_servicecontactuser() {
        return sh_servicecontactuser;
    }


    /**
     * Sets the sh_servicecontactuser value for this EnResponse_Shop.
     * 
     * @param sh_servicecontactuser
     */
    public void setSh_servicecontactuser(java.lang.String sh_servicecontactuser) {
        this.sh_servicecontactuser = sh_servicecontactuser;
    }


    /**
     * Gets the sh_replacementphone value for this EnResponse_Shop.
     * 
     * @return sh_replacementphone
     */
    public java.lang.String getSh_replacementphone() {
        return sh_replacementphone;
    }


    /**
     * Sets the sh_replacementphone value for this EnResponse_Shop.
     * 
     * @param sh_replacementphone
     */
    public void setSh_replacementphone(java.lang.String sh_replacementphone) {
        this.sh_replacementphone = sh_replacementphone;
    }


    /**
     * Gets the sh_remark value for this EnResponse_Shop.
     * 
     * @return sh_remark
     */
    public java.lang.String getSh_remark() {
        return sh_remark;
    }


    /**
     * Sets the sh_remark value for this EnResponse_Shop.
     * 
     * @param sh_remark
     */
    public void setSh_remark(java.lang.String sh_remark) {
        this.sh_remark = sh_remark;
    }


    /**
     * Gets the sh_saleHours value for this EnResponse_Shop.
     * 
     * @return sh_saleHours
     */
    public java.lang.String getSh_saleHours() {
        return sh_saleHours;
    }


    /**
     * Sets the sh_saleHours value for this EnResponse_Shop.
     * 
     * @param sh_saleHours
     */
    public void setSh_saleHours(java.lang.String sh_saleHours) {
        this.sh_saleHours = sh_saleHours;
    }


    /**
     * Gets the sh_cooperateDate value for this EnResponse_Shop.
     * 
     * @return sh_cooperateDate
     */
    public java.lang.String getSh_cooperateDate() {
        return sh_cooperateDate;
    }


    /**
     * Sets the sh_cooperateDate value for this EnResponse_Shop.
     * 
     * @param sh_cooperateDate
     */
    public void setSh_cooperateDate(java.lang.String sh_cooperateDate) {
        this.sh_cooperateDate = sh_cooperateDate;
    }


    /**
     * Gets the sh_startDate value for this EnResponse_Shop.
     * 
     * @return sh_startDate
     */
    public java.lang.String getSh_startDate() {
        return sh_startDate;
    }


    /**
     * Sets the sh_startDate value for this EnResponse_Shop.
     * 
     * @param sh_startDate
     */
    public void setSh_startDate(java.lang.String sh_startDate) {
        this.sh_startDate = sh_startDate;
    }


    /**
     * Gets the sh_generalManager value for this EnResponse_Shop.
     * 
     * @return sh_generalManager
     */
    public java.lang.String getSh_generalManager() {
        return sh_generalManager;
    }


    /**
     * Sets the sh_generalManager value for this EnResponse_Shop.
     * 
     * @param sh_generalManager
     */
    public void setSh_generalManager(java.lang.String sh_generalManager) {
        this.sh_generalManager = sh_generalManager;
    }


    /**
     * Gets the sh_generalTel value for this EnResponse_Shop.
     * 
     * @return sh_generalTel
     */
    public java.lang.String getSh_generalTel() {
        return sh_generalTel;
    }


    /**
     * Sets the sh_generalTel value for this EnResponse_Shop.
     * 
     * @param sh_generalTel
     */
    public void setSh_generalTel(java.lang.String sh_generalTel) {
        this.sh_generalTel = sh_generalTel;
    }


    /**
     * Gets the sh_endDate value for this EnResponse_Shop.
     * 
     * @return sh_endDate
     */
    public java.lang.String getSh_endDate() {
        return sh_endDate;
    }


    /**
     * Sets the sh_endDate value for this EnResponse_Shop.
     * 
     * @param sh_endDate
     */
    public void setSh_endDate(java.lang.String sh_endDate) {
        this.sh_endDate = sh_endDate;
    }


    /**
     * Gets the sh_serviceWarningDate value for this EnResponse_Shop.
     * 
     * @return sh_serviceWarningDate
     */
    public java.lang.String getSh_serviceWarningDate() {
        return sh_serviceWarningDate;
    }


    /**
     * Sets the sh_serviceWarningDate value for this EnResponse_Shop.
     * 
     * @param sh_serviceWarningDate
     */
    public void setSh_serviceWarningDate(java.lang.String sh_serviceWarningDate) {
        this.sh_serviceWarningDate = sh_serviceWarningDate;
    }


    /**
     * Gets the sh_buildType value for this EnResponse_Shop.
     * 
     * @return sh_buildType
     */
    public java.lang.String getSh_buildType() {
        return sh_buildType;
    }


    /**
     * Sets the sh_buildType value for this EnResponse_Shop.
     * 
     * @param sh_buildType
     */
    public void setSh_buildType(java.lang.String sh_buildType) {
        this.sh_buildType = sh_buildType;
    }


    /**
     * Gets the sh_layout value for this EnResponse_Shop.
     * 
     * @return sh_layout
     */
    public java.lang.String getSh_layout() {
        return sh_layout;
    }


    /**
     * Sets the sh_layout value for this EnResponse_Shop.
     * 
     * @param sh_layout
     */
    public void setSh_layout(java.lang.String sh_layout) {
        this.sh_layout = sh_layout;
    }


    /**
     * Gets the sh_saleCarBrand value for this EnResponse_Shop.
     * 
     * @return sh_saleCarBrand
     */
    public java.lang.String getSh_saleCarBrand() {
        return sh_saleCarBrand;
    }


    /**
     * Sets the sh_saleCarBrand value for this EnResponse_Shop.
     * 
     * @param sh_saleCarBrand
     */
    public void setSh_saleCarBrand(java.lang.String sh_saleCarBrand) {
        this.sh_saleCarBrand = sh_saleCarBrand;
    }


    /**
     * Gets the sh_claimRejectedSeries value for this EnResponse_Shop.
     * 
     * @return sh_claimRejectedSeries
     */
    public java.lang.String getSh_claimRejectedSeries() {
        return sh_claimRejectedSeries;
    }


    /**
     * Sets the sh_claimRejectedSeries value for this EnResponse_Shop.
     * 
     * @param sh_claimRejectedSeries
     */
    public void setSh_claimRejectedSeries(java.lang.String sh_claimRejectedSeries) {
        this.sh_claimRejectedSeries = sh_claimRejectedSeries;
    }


    /**
     * Gets the sh_claimRight value for this EnResponse_Shop.
     * 
     * @return sh_claimRight
     */
    public java.lang.String getSh_claimRight() {
        return sh_claimRight;
    }


    /**
     * Sets the sh_claimRight value for this EnResponse_Shop.
     * 
     * @param sh_claimRight
     */
    public void setSh_claimRight(java.lang.String sh_claimRight) {
        this.sh_claimRight = sh_claimRight;
    }


    /**
     * Gets the sh_maintenanceRight value for this EnResponse_Shop.
     * 
     * @return sh_maintenanceRight
     */
    public java.lang.String getSh_maintenanceRight() {
        return sh_maintenanceRight;
    }


    /**
     * Sets the sh_maintenanceRight value for this EnResponse_Shop.
     * 
     * @param sh_maintenanceRight
     */
    public void setSh_maintenanceRight(java.lang.String sh_maintenanceRight) {
        this.sh_maintenanceRight = sh_maintenanceRight;
    }


    /**
     * Gets the sh_orderRight value for this EnResponse_Shop.
     * 
     * @return sh_orderRight
     */
    public java.lang.String getSh_orderRight() {
        return sh_orderRight;
    }


    /**
     * Sets the sh_orderRight value for this EnResponse_Shop.
     * 
     * @param sh_orderRight
     */
    public void setSh_orderRight(java.lang.String sh_orderRight) {
        this.sh_orderRight = sh_orderRight;
    }


    /**
     * Gets the sh_spareParts value for this EnResponse_Shop.
     * 
     * @return sh_spareParts
     */
    public java.lang.String getSh_spareParts() {
        return sh_spareParts;
    }


    /**
     * Sets the sh_spareParts value for this EnResponse_Shop.
     * 
     * @param sh_spareParts
     */
    public void setSh_spareParts(java.lang.String sh_spareParts) {
        this.sh_spareParts = sh_spareParts;
    }


    /**
     * Gets the oz_ID value for this EnResponse_Shop.
     * 
     * @return oz_ID
     */
    public java.lang.String getOz_ID() {
        return oz_ID;
    }


    /**
     * Sets the oz_ID value for this EnResponse_Shop.
     * 
     * @param oz_ID
     */
    public void setOz_ID(java.lang.String oz_ID) {
        this.oz_ID = oz_ID;
    }


    /**
     * Gets the rank value for this EnResponse_Shop.
     * 
     * @return rank
     */
    public java.lang.Integer getRank() {
        return rank;
    }


    /**
     * Sets the rank value for this EnResponse_Shop.
     * 
     * @param rank
     */
    public void setRank(java.lang.Integer rank) {
        this.rank = rank;
    }


    /**
     * Gets the sh_operationLife value for this EnResponse_Shop.
     * 
     * @return sh_operationLife
     */
    public java.lang.String getSh_operationLife() {
        return sh_operationLife;
    }


    /**
     * Sets the sh_operationLife value for this EnResponse_Shop.
     * 
     * @param sh_operationLife
     */
    public void setSh_operationLife(java.lang.String sh_operationLife) {
        this.sh_operationLife = sh_operationLife;
    }


    /**
     * Gets the sh_serviceLife value for this EnResponse_Shop.
     * 
     * @return sh_serviceLife
     */
    public java.lang.String getSh_serviceLife() {
        return sh_serviceLife;
    }


    /**
     * Sets the sh_serviceLife value for this EnResponse_Shop.
     * 
     * @param sh_serviceLife
     */
    public void setSh_serviceLife(java.lang.String sh_serviceLife) {
        this.sh_serviceLife = sh_serviceLife;
    }


    /**
     * Gets the sh_financeCoorp value for this EnResponse_Shop.
     * 
     * @return sh_financeCoorp
     */
    public java.lang.String getSh_financeCoorp() {
        return sh_financeCoorp;
    }


    /**
     * Sets the sh_financeCoorp value for this EnResponse_Shop.
     * 
     * @param sh_financeCoorp
     */
    public void setSh_financeCoorp(java.lang.String sh_financeCoorp) {
        this.sh_financeCoorp = sh_financeCoorp;
    }


    /**
     * Gets the sh_financeCoorpDate value for this EnResponse_Shop.
     * 
     * @return sh_financeCoorpDate
     */
    public java.lang.String getSh_financeCoorpDate() {
        return sh_financeCoorpDate;
    }


    /**
     * Sets the sh_financeCoorpDate value for this EnResponse_Shop.
     * 
     * @param sh_financeCoorpDate
     */
    public void setSh_financeCoorpDate(java.lang.String sh_financeCoorpDate) {
        this.sh_financeCoorpDate = sh_financeCoorpDate;
    }


    /**
     * Gets the sh_financeCoorpEndDate value for this EnResponse_Shop.
     * 
     * @return sh_financeCoorpEndDate
     */
    public java.lang.String getSh_financeCoorpEndDate() {
        return sh_financeCoorpEndDate;
    }


    /**
     * Sets the sh_financeCoorpEndDate value for this EnResponse_Shop.
     * 
     * @param sh_financeCoorpEndDate
     */
    public void setSh_financeCoorpEndDate(java.lang.String sh_financeCoorpEndDate) {
        this.sh_financeCoorpEndDate = sh_financeCoorpEndDate;
    }


    /**
     * Gets the sh_shopImage value for this EnResponse_Shop.
     * 
     * @return sh_shopImage
     */
    public java.lang.String getSh_shopImage() {
        return sh_shopImage;
    }


    /**
     * Sets the sh_shopImage value for this EnResponse_Shop.
     * 
     * @param sh_shopImage
     */
    public void setSh_shopImage(java.lang.String sh_shopImage) {
        this.sh_shopImage = sh_shopImage;
    }


    /**
     * Gets the oz_IdGroup value for this EnResponse_Shop.
     * 
     * @return oz_IdGroup
     */
    public java.lang.Integer getOz_IdGroup() {
        return oz_IdGroup;
    }


    /**
     * Sets the oz_IdGroup value for this EnResponse_Shop.
     * 
     * @param oz_IdGroup
     */
    public void setOz_IdGroup(java.lang.Integer oz_IdGroup) {
        this.oz_IdGroup = oz_IdGroup;
    }


    /**
     * Gets the oz_GroupName value for this EnResponse_Shop.
     * 
     * @return oz_GroupName
     */
    public java.lang.String getOz_GroupName() {
        return oz_GroupName;
    }


    /**
     * Sets the oz_GroupName value for this EnResponse_Shop.
     * 
     * @param oz_GroupName
     */
    public void setOz_GroupName(java.lang.String oz_GroupName) {
        this.oz_GroupName = oz_GroupName;
    }


    /**
     * Gets the oz_IdInvestors value for this EnResponse_Shop.
     * 
     * @return oz_IdInvestors
     */
    public java.lang.Integer getOz_IdInvestors() {
        return oz_IdInvestors;
    }


    /**
     * Sets the oz_IdInvestors value for this EnResponse_Shop.
     * 
     * @param oz_IdInvestors
     */
    public void setOz_IdInvestors(java.lang.Integer oz_IdInvestors) {
        this.oz_IdInvestors = oz_IdInvestors;
    }


    /**
     * Gets the oz_InvestorsName value for this EnResponse_Shop.
     * 
     * @return oz_InvestorsName
     */
    public java.lang.String getOz_InvestorsName() {
        return oz_InvestorsName;
    }


    /**
     * Sets the oz_InvestorsName value for this EnResponse_Shop.
     * 
     * @param oz_InvestorsName
     */
    public void setOz_InvestorsName(java.lang.String oz_InvestorsName) {
        this.oz_InvestorsName = oz_InvestorsName;
    }


    /**
     * Gets the sh_property value for this EnResponse_Shop.
     * 
     * @return sh_property
     */
    public java.lang.String getSh_property() {
        return sh_property;
    }


    /**
     * Sets the sh_property value for this EnResponse_Shop.
     * 
     * @param sh_property
     */
    public void setSh_property(java.lang.String sh_property) {
        this.sh_property = sh_property;
    }


    /**
     * Gets the sh_costomerMix value for this EnResponse_Shop.
     * 
     * @return sh_costomerMix
     */
    public java.lang.String getSh_costomerMix() {
        return sh_costomerMix;
    }


    /**
     * Sets the sh_costomerMix value for this EnResponse_Shop.
     * 
     * @param sh_costomerMix
     */
    public void setSh_costomerMix(java.lang.String sh_costomerMix) {
        this.sh_costomerMix = sh_costomerMix;
    }


    /**
     * Gets the msg value for this EnResponse_Shop.
     * 
     * @return msg
     */
    public java.lang.String getMsg() {
        return msg;
    }


    /**
     * Sets the msg value for this EnResponse_Shop.
     * 
     * @param msg
     */
    public void setMsg(java.lang.String msg) {
        this.msg = msg;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EnResponse_Shop)) return false;
        EnResponse_Shop other = (EnResponse_Shop) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.sh_ID == other.getSh_ID() &&
            ((this.sh_number==null && other.getSh_number()==null) || 
             (this.sh_number!=null &&
              this.sh_number.equals(other.getSh_number()))) &&
            ((this.sh_name==null && other.getSh_name()==null) || 
             (this.sh_name!=null &&
              this.sh_name.equals(other.getSh_name()))) &&
            ((this.sh_shortName==null && other.getSh_shortName()==null) || 
             (this.sh_shortName!=null &&
              this.sh_shortName.equals(other.getSh_shortName()))) &&
            ((this.oz_number==null && other.getOz_number()==null) || 
             (this.oz_number!=null &&
              this.oz_number.equals(other.getOz_number()))) &&
            ((this.oz_shortname==null && other.getOz_shortname()==null) || 
             (this.oz_shortname!=null &&
              this.oz_shortname.equals(other.getOz_shortname()))) &&
            ((this.sh_storeName==null && other.getSh_storeName()==null) || 
             (this.sh_storeName!=null &&
              this.sh_storeName.equals(other.getSh_storeName()))) &&
            ((this.sh_storeAbbre==null && other.getSh_storeAbbre()==null) || 
             (this.sh_storeAbbre!=null &&
              this.sh_storeAbbre.equals(other.getSh_storeAbbre()))) &&
            ((this.sh_serviceStoreName==null && other.getSh_serviceStoreName()==null) || 
             (this.sh_serviceStoreName!=null &&
              this.sh_serviceStoreName.equals(other.getSh_serviceStoreName()))) &&
            ((this.sh_serviceAbbre==null && other.getSh_serviceAbbre()==null) || 
             (this.sh_serviceAbbre!=null &&
              this.sh_serviceAbbre.equals(other.getSh_serviceAbbre()))) &&
            ((this.sh_state==null && other.getSh_state()==null) || 
             (this.sh_state!=null &&
              this.sh_state.equals(other.getSh_state()))) &&
            ((this.sh_serviceState==null && other.getSh_serviceState()==null) || 
             (this.sh_serviceState!=null &&
              this.sh_serviceState.equals(other.getSh_serviceState()))) &&
            ((this.sh_type==null && other.getSh_type()==null) || 
             (this.sh_type!=null &&
              this.sh_type.equals(other.getSh_type()))) &&
            ((this.sh_grade==null && other.getSh_grade()==null) || 
             (this.sh_grade!=null &&
              this.sh_grade.equals(other.getSh_grade()))) &&
            ((this.sh_category==null && other.getSh_category()==null) || 
             (this.sh_category!=null &&
              this.sh_category.equals(other.getSh_category()))) &&
            ((this.sh_province==null && other.getSh_province()==null) || 
             (this.sh_province!=null &&
              this.sh_province.equals(other.getSh_province()))) &&
            ((this.sh_city==null && other.getSh_city()==null) || 
             (this.sh_city!=null &&
              this.sh_city.equals(other.getSh_city()))) &&
            ((this.sh_county==null && other.getSh_county()==null) || 
             (this.sh_county!=null &&
              this.sh_county.equals(other.getSh_county()))) &&
            ((this.sh_address==null && other.getSh_address()==null) || 
             (this.sh_address!=null &&
              this.sh_address.equals(other.getSh_address()))) &&
            ((this.sh_stationAddress==null && other.getSh_stationAddress()==null) || 
             (this.sh_stationAddress!=null &&
              this.sh_stationAddress.equals(other.getSh_stationAddress()))) &&
            this.sh_salelongitude == other.getSh_salelongitude() &&
            this.sh_salelatitude == other.getSh_salelatitude() &&
            this.sh_longitude == other.getSh_longitude() &&
            this.sh_latitude == other.getSh_latitude() &&
            ((this.agentBrands==null && other.getAgentBrands()==null) || 
             (this.agentBrands!=null &&
              this.agentBrands.equals(other.getAgentBrands()))) &&
            ((this.sh_serviceCategory==null && other.getSh_serviceCategory()==null) || 
             (this.sh_serviceCategory!=null &&
              this.sh_serviceCategory.equals(other.getSh_serviceCategory()))) &&
            ((this.sh_salesCooper==null && other.getSh_salesCooper()==null) || 
             (this.sh_salesCooper!=null &&
              this.sh_salesCooper.equals(other.getSh_salesCooper()))) &&
            ((this.sh_serviceendDate==null && other.getSh_serviceendDate()==null) || 
             (this.sh_serviceendDate!=null &&
              this.sh_serviceendDate.equals(other.getSh_serviceendDate()))) &&
            ((this.sh_createTime==null && other.getSh_createTime()==null) || 
             (this.sh_createTime!=null &&
              this.sh_createTime.equals(other.getSh_createTime()))) &&
            ((this.sh_lastUpdateTime==null && other.getSh_lastUpdateTime()==null) || 
             (this.sh_lastUpdateTime!=null &&
              this.sh_lastUpdateTime.equals(other.getSh_lastUpdateTime()))) &&
            ((this.sh_postCode==null && other.getSh_postCode()==null) || 
             (this.sh_postCode!=null &&
              this.sh_postCode.equals(other.getSh_postCode()))) &&
            ((this.sh_saleHotline==null && other.getSh_saleHotline()==null) || 
             (this.sh_saleHotline!=null &&
              this.sh_saleHotline.equals(other.getSh_saleHotline()))) &&
            ((this.sh_saleEmail==null && other.getSh_saleEmail()==null) || 
             (this.sh_saleEmail!=null &&
              this.sh_saleEmail.equals(other.getSh_saleEmail()))) &&
            ((this.sh_saleFax==null && other.getSh_saleFax()==null) || 
             (this.sh_saleFax!=null &&
              this.sh_saleFax.equals(other.getSh_saleFax()))) &&
            ((this.sh_serviceHotline==null && other.getSh_serviceHotline()==null) || 
             (this.sh_serviceHotline!=null &&
              this.sh_serviceHotline.equals(other.getSh_serviceHotline()))) &&
            ((this.sh_serviceFax==null && other.getSh_serviceFax()==null) || 
             (this.sh_serviceFax!=null &&
              this.sh_serviceFax.equals(other.getSh_serviceFax()))) &&
            ((this.sh_serviceEmail==null && other.getSh_serviceEmail()==null) || 
             (this.sh_serviceEmail!=null &&
              this.sh_serviceEmail.equals(other.getSh_serviceEmail()))) &&
            ((this.sh_serviceTime==null && other.getSh_serviceTime()==null) || 
             (this.sh_serviceTime!=null &&
              this.sh_serviceTime.equals(other.getSh_serviceTime()))) &&
            ((this.sh_serviceHours==null && other.getSh_serviceHours()==null) || 
             (this.sh_serviceHours!=null &&
              this.sh_serviceHours.equals(other.getSh_serviceHours()))) &&
            ((this.sh_stationMaster==null && other.getSh_stationMaster()==null) || 
             (this.sh_stationMaster!=null &&
              this.sh_stationMaster.equals(other.getSh_stationMaster()))) &&
            ((this.sh_stMasterTel==null && other.getSh_stMasterTel()==null) || 
             (this.sh_stMasterTel!=null &&
              this.sh_stMasterTel.equals(other.getSh_stMasterTel()))) &&
            ((this.sh_emergencyHotline==null && other.getSh_emergencyHotline()==null) || 
             (this.sh_emergencyHotline!=null &&
              this.sh_emergencyHotline.equals(other.getSh_emergencyHotline()))) &&
            ((this.sh_parent==null && other.getSh_parent()==null) || 
             (this.sh_parent!=null &&
              this.sh_parent.equals(other.getSh_parent()))) &&
            ((this.sh_parentid==null && other.getSh_parentid()==null) || 
             (this.sh_parentid!=null &&
              this.sh_parentid.equals(other.getSh_parentid()))) &&
            ((this.sh_servicecontactuser==null && other.getSh_servicecontactuser()==null) || 
             (this.sh_servicecontactuser!=null &&
              this.sh_servicecontactuser.equals(other.getSh_servicecontactuser()))) &&
            ((this.sh_replacementphone==null && other.getSh_replacementphone()==null) || 
             (this.sh_replacementphone!=null &&
              this.sh_replacementphone.equals(other.getSh_replacementphone()))) &&
            ((this.sh_remark==null && other.getSh_remark()==null) || 
             (this.sh_remark!=null &&
              this.sh_remark.equals(other.getSh_remark()))) &&
            ((this.sh_saleHours==null && other.getSh_saleHours()==null) || 
             (this.sh_saleHours!=null &&
              this.sh_saleHours.equals(other.getSh_saleHours()))) &&
            ((this.sh_cooperateDate==null && other.getSh_cooperateDate()==null) || 
             (this.sh_cooperateDate!=null &&
              this.sh_cooperateDate.equals(other.getSh_cooperateDate()))) &&
            ((this.sh_startDate==null && other.getSh_startDate()==null) || 
             (this.sh_startDate!=null &&
              this.sh_startDate.equals(other.getSh_startDate()))) &&
            ((this.sh_generalManager==null && other.getSh_generalManager()==null) || 
             (this.sh_generalManager!=null &&
              this.sh_generalManager.equals(other.getSh_generalManager()))) &&
            ((this.sh_generalTel==null && other.getSh_generalTel()==null) || 
             (this.sh_generalTel!=null &&
              this.sh_generalTel.equals(other.getSh_generalTel()))) &&
            ((this.sh_endDate==null && other.getSh_endDate()==null) || 
             (this.sh_endDate!=null &&
              this.sh_endDate.equals(other.getSh_endDate()))) &&
            ((this.sh_serviceWarningDate==null && other.getSh_serviceWarningDate()==null) || 
             (this.sh_serviceWarningDate!=null &&
              this.sh_serviceWarningDate.equals(other.getSh_serviceWarningDate()))) &&
            ((this.sh_buildType==null && other.getSh_buildType()==null) || 
             (this.sh_buildType!=null &&
              this.sh_buildType.equals(other.getSh_buildType()))) &&
            ((this.sh_layout==null && other.getSh_layout()==null) || 
             (this.sh_layout!=null &&
              this.sh_layout.equals(other.getSh_layout()))) &&
            ((this.sh_saleCarBrand==null && other.getSh_saleCarBrand()==null) || 
             (this.sh_saleCarBrand!=null &&
              this.sh_saleCarBrand.equals(other.getSh_saleCarBrand()))) &&
            ((this.sh_claimRejectedSeries==null && other.getSh_claimRejectedSeries()==null) || 
             (this.sh_claimRejectedSeries!=null &&
              this.sh_claimRejectedSeries.equals(other.getSh_claimRejectedSeries()))) &&
            ((this.sh_claimRight==null && other.getSh_claimRight()==null) || 
             (this.sh_claimRight!=null &&
              this.sh_claimRight.equals(other.getSh_claimRight()))) &&
            ((this.sh_maintenanceRight==null && other.getSh_maintenanceRight()==null) || 
             (this.sh_maintenanceRight!=null &&
              this.sh_maintenanceRight.equals(other.getSh_maintenanceRight()))) &&
            ((this.sh_orderRight==null && other.getSh_orderRight()==null) || 
             (this.sh_orderRight!=null &&
              this.sh_orderRight.equals(other.getSh_orderRight()))) &&
            ((this.sh_spareParts==null && other.getSh_spareParts()==null) || 
             (this.sh_spareParts!=null &&
              this.sh_spareParts.equals(other.getSh_spareParts()))) &&
            ((this.oz_ID==null && other.getOz_ID()==null) || 
             (this.oz_ID!=null &&
              this.oz_ID.equals(other.getOz_ID()))) &&
            ((this.rank==null && other.getRank()==null) || 
             (this.rank!=null &&
              this.rank.equals(other.getRank()))) &&
            ((this.sh_operationLife==null && other.getSh_operationLife()==null) || 
             (this.sh_operationLife!=null &&
              this.sh_operationLife.equals(other.getSh_operationLife()))) &&
            ((this.sh_serviceLife==null && other.getSh_serviceLife()==null) || 
             (this.sh_serviceLife!=null &&
              this.sh_serviceLife.equals(other.getSh_serviceLife()))) &&
            ((this.sh_financeCoorp==null && other.getSh_financeCoorp()==null) || 
             (this.sh_financeCoorp!=null &&
              this.sh_financeCoorp.equals(other.getSh_financeCoorp()))) &&
            ((this.sh_financeCoorpDate==null && other.getSh_financeCoorpDate()==null) || 
             (this.sh_financeCoorpDate!=null &&
              this.sh_financeCoorpDate.equals(other.getSh_financeCoorpDate()))) &&
            ((this.sh_financeCoorpEndDate==null && other.getSh_financeCoorpEndDate()==null) || 
             (this.sh_financeCoorpEndDate!=null &&
              this.sh_financeCoorpEndDate.equals(other.getSh_financeCoorpEndDate()))) &&
            ((this.sh_shopImage==null && other.getSh_shopImage()==null) || 
             (this.sh_shopImage!=null &&
              this.sh_shopImage.equals(other.getSh_shopImage()))) &&
            ((this.oz_IdGroup==null && other.getOz_IdGroup()==null) || 
             (this.oz_IdGroup!=null &&
              this.oz_IdGroup.equals(other.getOz_IdGroup()))) &&
            ((this.oz_GroupName==null && other.getOz_GroupName()==null) || 
             (this.oz_GroupName!=null &&
              this.oz_GroupName.equals(other.getOz_GroupName()))) &&
            ((this.oz_IdInvestors==null && other.getOz_IdInvestors()==null) || 
             (this.oz_IdInvestors!=null &&
              this.oz_IdInvestors.equals(other.getOz_IdInvestors()))) &&
            ((this.oz_InvestorsName==null && other.getOz_InvestorsName()==null) || 
             (this.oz_InvestorsName!=null &&
              this.oz_InvestorsName.equals(other.getOz_InvestorsName()))) &&
            ((this.sh_property==null && other.getSh_property()==null) || 
             (this.sh_property!=null &&
              this.sh_property.equals(other.getSh_property()))) &&
            ((this.sh_costomerMix==null && other.getSh_costomerMix()==null) || 
             (this.sh_costomerMix!=null &&
              this.sh_costomerMix.equals(other.getSh_costomerMix()))) &&
            ((this.msg==null && other.getMsg()==null) || 
             (this.msg!=null &&
              this.msg.equals(other.getMsg())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getSh_ID();
        if (getSh_number() != null) {
            _hashCode += getSh_number().hashCode();
        }
        if (getSh_name() != null) {
            _hashCode += getSh_name().hashCode();
        }
        if (getSh_shortName() != null) {
            _hashCode += getSh_shortName().hashCode();
        }
        if (getOz_number() != null) {
            _hashCode += getOz_number().hashCode();
        }
        if (getOz_shortname() != null) {
            _hashCode += getOz_shortname().hashCode();
        }
        if (getSh_storeName() != null) {
            _hashCode += getSh_storeName().hashCode();
        }
        if (getSh_storeAbbre() != null) {
            _hashCode += getSh_storeAbbre().hashCode();
        }
        if (getSh_serviceStoreName() != null) {
            _hashCode += getSh_serviceStoreName().hashCode();
        }
        if (getSh_serviceAbbre() != null) {
            _hashCode += getSh_serviceAbbre().hashCode();
        }
        if (getSh_state() != null) {
            _hashCode += getSh_state().hashCode();
        }
        if (getSh_serviceState() != null) {
            _hashCode += getSh_serviceState().hashCode();
        }
        if (getSh_type() != null) {
            _hashCode += getSh_type().hashCode();
        }
        if (getSh_grade() != null) {
            _hashCode += getSh_grade().hashCode();
        }
        if (getSh_category() != null) {
            _hashCode += getSh_category().hashCode();
        }
        if (getSh_province() != null) {
            _hashCode += getSh_province().hashCode();
        }
        if (getSh_city() != null) {
            _hashCode += getSh_city().hashCode();
        }
        if (getSh_county() != null) {
            _hashCode += getSh_county().hashCode();
        }
        if (getSh_address() != null) {
            _hashCode += getSh_address().hashCode();
        }
        if (getSh_stationAddress() != null) {
            _hashCode += getSh_stationAddress().hashCode();
        }
        _hashCode += new Float(getSh_salelongitude()).hashCode();
        _hashCode += new Float(getSh_salelatitude()).hashCode();
        _hashCode += new Float(getSh_longitude()).hashCode();
        _hashCode += new Float(getSh_latitude()).hashCode();
        if (getAgentBrands() != null) {
            _hashCode += getAgentBrands().hashCode();
        }
        if (getSh_serviceCategory() != null) {
            _hashCode += getSh_serviceCategory().hashCode();
        }
        if (getSh_salesCooper() != null) {
            _hashCode += getSh_salesCooper().hashCode();
        }
        if (getSh_serviceendDate() != null) {
            _hashCode += getSh_serviceendDate().hashCode();
        }
        if (getSh_createTime() != null) {
            _hashCode += getSh_createTime().hashCode();
        }
        if (getSh_lastUpdateTime() != null) {
            _hashCode += getSh_lastUpdateTime().hashCode();
        }
        if (getSh_postCode() != null) {
            _hashCode += getSh_postCode().hashCode();
        }
        if (getSh_saleHotline() != null) {
            _hashCode += getSh_saleHotline().hashCode();
        }
        if (getSh_saleEmail() != null) {
            _hashCode += getSh_saleEmail().hashCode();
        }
        if (getSh_saleFax() != null) {
            _hashCode += getSh_saleFax().hashCode();
        }
        if (getSh_serviceHotline() != null) {
            _hashCode += getSh_serviceHotline().hashCode();
        }
        if (getSh_serviceFax() != null) {
            _hashCode += getSh_serviceFax().hashCode();
        }
        if (getSh_serviceEmail() != null) {
            _hashCode += getSh_serviceEmail().hashCode();
        }
        if (getSh_serviceTime() != null) {
            _hashCode += getSh_serviceTime().hashCode();
        }
        if (getSh_serviceHours() != null) {
            _hashCode += getSh_serviceHours().hashCode();
        }
        if (getSh_stationMaster() != null) {
            _hashCode += getSh_stationMaster().hashCode();
        }
        if (getSh_stMasterTel() != null) {
            _hashCode += getSh_stMasterTel().hashCode();
        }
        if (getSh_emergencyHotline() != null) {
            _hashCode += getSh_emergencyHotline().hashCode();
        }
        if (getSh_parent() != null) {
            _hashCode += getSh_parent().hashCode();
        }
        if (getSh_parentid() != null) {
            _hashCode += getSh_parentid().hashCode();
        }
        if (getSh_servicecontactuser() != null) {
            _hashCode += getSh_servicecontactuser().hashCode();
        }
        if (getSh_replacementphone() != null) {
            _hashCode += getSh_replacementphone().hashCode();
        }
        if (getSh_remark() != null) {
            _hashCode += getSh_remark().hashCode();
        }
        if (getSh_saleHours() != null) {
            _hashCode += getSh_saleHours().hashCode();
        }
        if (getSh_cooperateDate() != null) {
            _hashCode += getSh_cooperateDate().hashCode();
        }
        if (getSh_startDate() != null) {
            _hashCode += getSh_startDate().hashCode();
        }
        if (getSh_generalManager() != null) {
            _hashCode += getSh_generalManager().hashCode();
        }
        if (getSh_generalTel() != null) {
            _hashCode += getSh_generalTel().hashCode();
        }
        if (getSh_endDate() != null) {
            _hashCode += getSh_endDate().hashCode();
        }
        if (getSh_serviceWarningDate() != null) {
            _hashCode += getSh_serviceWarningDate().hashCode();
        }
        if (getSh_buildType() != null) {
            _hashCode += getSh_buildType().hashCode();
        }
        if (getSh_layout() != null) {
            _hashCode += getSh_layout().hashCode();
        }
        if (getSh_saleCarBrand() != null) {
            _hashCode += getSh_saleCarBrand().hashCode();
        }
        if (getSh_claimRejectedSeries() != null) {
            _hashCode += getSh_claimRejectedSeries().hashCode();
        }
        if (getSh_claimRight() != null) {
            _hashCode += getSh_claimRight().hashCode();
        }
        if (getSh_maintenanceRight() != null) {
            _hashCode += getSh_maintenanceRight().hashCode();
        }
        if (getSh_orderRight() != null) {
            _hashCode += getSh_orderRight().hashCode();
        }
        if (getSh_spareParts() != null) {
            _hashCode += getSh_spareParts().hashCode();
        }
        if (getOz_ID() != null) {
            _hashCode += getOz_ID().hashCode();
        }
        if (getRank() != null) {
            _hashCode += getRank().hashCode();
        }
        if (getSh_operationLife() != null) {
            _hashCode += getSh_operationLife().hashCode();
        }
        if (getSh_serviceLife() != null) {
            _hashCode += getSh_serviceLife().hashCode();
        }
        if (getSh_financeCoorp() != null) {
            _hashCode += getSh_financeCoorp().hashCode();
        }
        if (getSh_financeCoorpDate() != null) {
            _hashCode += getSh_financeCoorpDate().hashCode();
        }
        if (getSh_financeCoorpEndDate() != null) {
            _hashCode += getSh_financeCoorpEndDate().hashCode();
        }
        if (getSh_shopImage() != null) {
            _hashCode += getSh_shopImage().hashCode();
        }
        if (getOz_IdGroup() != null) {
            _hashCode += getOz_IdGroup().hashCode();
        }
        if (getOz_GroupName() != null) {
            _hashCode += getOz_GroupName().hashCode();
        }
        if (getOz_IdInvestors() != null) {
            _hashCode += getOz_IdInvestors().hashCode();
        }
        if (getOz_InvestorsName() != null) {
            _hashCode += getOz_InvestorsName().hashCode();
        }
        if (getSh_property() != null) {
            _hashCode += getSh_property().hashCode();
        }
        if (getSh_costomerMix() != null) {
            _hashCode += getSh_costomerMix().hashCode();
        }
        if (getMsg() != null) {
            _hashCode += getMsg().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EnResponse_Shop.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.gwm.cn/", "EnResponse_Shop"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_number");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_shortName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_shortName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oz_number");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "oz_number"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oz_shortname");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "oz_shortname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_storeName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_storeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_storeAbbre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_storeAbbre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceStoreName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceStoreName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceAbbre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceAbbre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_state");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_state"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_grade");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_grade"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_category");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_category"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_province");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_province"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_city");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_city"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_county");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_county"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_stationAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_stationAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_salelongitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_salelongitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_salelatitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_salelatitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_longitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_longitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_latitude");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_latitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("agentBrands");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "AgentBrands"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceCategory");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceCategory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_salesCooper");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_salesCooper"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceendDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceendDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_createTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_createTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_lastUpdateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_lastUpdateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_postCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_postCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_saleHotline");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_saleHotline"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_saleEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_saleEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_saleFax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_saleFax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceHotline");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceHotline"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceFax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceFax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceHours");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceHours"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_stationMaster");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_stationMaster"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_stMasterTel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_stMasterTel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_emergencyHotline");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_emergencyHotline"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_parent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_parent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_parentid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_parentid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_servicecontactuser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_servicecontactuser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_replacementphone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_replacementphone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_remark");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_remark"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_saleHours");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_saleHours"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_cooperateDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_cooperateDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_startDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_generalManager");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_generalManager"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_generalTel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_generalTel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_endDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceWarningDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceWarningDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_buildType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_buildType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_layout");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_layout"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_saleCarBrand");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_saleCarBrand"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_claimRejectedSeries");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_claimRejectedSeries"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_claimRight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_claimRight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_maintenanceRight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_maintenanceRight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_orderRight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_orderRight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_spareParts");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_spareParts"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oz_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "oz_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rank");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "rank"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_operationLife");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_operationLife"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_serviceLife");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_serviceLife"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_financeCoorp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_financeCoorp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_financeCoorpDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_financeCoorpDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_financeCoorpEndDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_financeCoorpEndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_shopImage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_shopImage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oz_IdGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "oz_IdGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oz_GroupName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "oz_GroupName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oz_IdInvestors");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "oz_IdInvestors"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oz_InvestorsName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "oz_InvestorsName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_property");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_property"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sh_costomerMix");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "sh_costomerMix"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.gwm.cn/", "msg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
