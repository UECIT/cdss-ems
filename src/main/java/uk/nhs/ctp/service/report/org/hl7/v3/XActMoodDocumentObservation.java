//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.05.30 at 12:27:07 PM BST 
//


package uk.nhs.ctp.service.report.org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for x_ActMoodDocumentObservation.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ActMoodDocumentObservation">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="INT"/>
 *     &lt;enumeration value="DEF"/>
 *     &lt;enumeration value="EVN"/>
 *     &lt;enumeration value="GOL"/>
 *     &lt;enumeration value="PRMS"/>
 *     &lt;enumeration value="PRP"/>
 *     &lt;enumeration value="RQO"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ActMoodDocumentObservation")
@XmlEnum
public enum XActMoodDocumentObservation {

    INT,
    DEF,
    EVN,
    GOL,
    PRMS,
    PRP,
    RQO;

    public String value() {
        return name();
    }

    public static XActMoodDocumentObservation fromValue(String v) {
        return valueOf(v);
    }

}
