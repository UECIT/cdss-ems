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
 * <p>Java class for ActClassRoot_X.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActClassRoot_X">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="INVE"/>
 *     &lt;enumeration value="PROC"/>
 *     &lt;enumeration value="CONS"/>
 *     &lt;enumeration value="SUBST"/>
 *     &lt;enumeration value="REFR"/>
 *     &lt;enumeration value="TRNS"/>
 *     &lt;enumeration value="LIST"/>
 *     &lt;enumeration value="ENC"/>
 *     &lt;enumeration value="XACT"/>
 *     &lt;enumeration value="SBADM"/>
 *     &lt;enumeration value="DISPACT"/>
 *     &lt;enumeration value="ACCT"/>
 *     &lt;enumeration value="CTTEVENT"/>
 *     &lt;enumeration value="CONTREG"/>
 *     &lt;enumeration value="INVE"/>
 *     &lt;enumeration value="SPCTRT"/>
 *     &lt;enumeration value="REG"/>
 *     &lt;enumeration value="ACCM"/>
 *     &lt;enumeration value="ACSN"/>
 *     &lt;enumeration value="ADJUD"/>
 *     &lt;enumeration value="INFRM"/>
 *     &lt;enumeration value="PCPR"/>
 *     &lt;enumeration value="INC"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActClassRoot_X")
@XmlEnum
public enum ActClassRootX {

    INVE,
    PROC,
    CONS,
    SUBST,
    REFR,
    TRNS,
    LIST,
    ENC,
    XACT,
    SBADM,
    DISPACT,
    ACCT,
    CTTEVENT,
    CONTREG,
    SPCTRT,
    REG,
    ACCM,
    ACSN,
    ADJUD,
    INFRM,
    PCPR,
    INC;

    public String value() {
        return name();
    }

    public static ActClassRootX fromValue(String v) {
        return valueOf(v);
    }

}
