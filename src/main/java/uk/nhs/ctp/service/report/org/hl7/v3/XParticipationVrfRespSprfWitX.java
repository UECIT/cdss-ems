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
 * <p>Java class for x_ParticipationVrfRespSprfWit_X.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="x_ParticipationVrfRespSprfWit_X">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="RESP"/>
 *     &lt;enumeration value="SPRF"/>
 *     &lt;enumeration value="VRF"/>
 *     &lt;enumeration value="WIT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "x_ParticipationVrfRespSprfWit_X")
@XmlEnum
public enum XParticipationVrfRespSprfWitX {

    RESP,
    SPRF,
    VRF,
    WIT;

    public String value() {
        return name();
    }

    public static XParticipationVrfRespSprfWitX fromValue(String v) {
        return valueOf(v);
    }

}
