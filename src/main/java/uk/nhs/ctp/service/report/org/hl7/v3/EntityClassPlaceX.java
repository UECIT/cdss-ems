//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.15 at 03:22:47 PM GMT 
//


package uk.nhs.ctp.service.report.org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntityClassPlace_X.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EntityClassPlace_X">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PROVINCE"/>
 *     &lt;enumeration value="COUNTY"/>
 *     &lt;enumeration value="COUNTRY"/>
 *     &lt;enumeration value="CITY"/>
 *     &lt;enumeration value="PLC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EntityClassPlace_X")
@XmlEnum
public enum EntityClassPlaceX {

    PROVINCE,
    COUNTY,
    COUNTRY,
    CITY,
    PLC;

    public String value() {
        return name();
    }

    public static EntityClassPlaceX fromValue(String v) {
        return valueOf(v);
    }

}
