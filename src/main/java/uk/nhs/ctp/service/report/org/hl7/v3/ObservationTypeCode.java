//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.15 at 03:22:47 PM GMT 
//


package uk.nhs.ctp.service.report.org.hl7.v3;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObservationType_code.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObservationType_code">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="ANDP"/>
 *     &lt;enumeration value="DNDP"/>
 *     &lt;enumeration value="MCC"/>
 *     &lt;enumeration value="RR"/>
 *     &lt;enumeration value="ESS117"/>
 *     &lt;enumeration value="CHCS"/>
 *     &lt;enumeration value="DNDT"/>
 *     &lt;enumeration value="MCPC"/>
 *     &lt;enumeration value="STD"/>
 *     &lt;enumeration value="AGTCN"/>
 *     &lt;enumeration value="ANRR"/>
 *     &lt;enumeration value="DNDR"/>
 *     &lt;enumeration value="DNAR"/>
 *     &lt;enumeration value="MDTSTD"/>
 *     &lt;enumeration value="DNCRC"/>
 *     &lt;enumeration value="ADRRC"/>
 *     &lt;enumeration value="ADRRT"/>
 *     &lt;enumeration value="CHCRC"/>
 *     &lt;enumeration value="CHCRT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObservationType_code")
@XmlEnum
public enum ObservationTypeCode {

    ANDP("ANDP"),
    DNDP("DNDP"),
    MCC("MCC"),
    RR("RR"),
    @XmlEnumValue("ESS117")
    ESS_117("ESS117"),
    CHCS("CHCS"),
    DNDT("DNDT"),
    MCPC("MCPC"),
    STD("STD"),
    AGTCN("AGTCN"),
    ANRR("ANRR"),
    DNDR("DNDR"),
    DNAR("DNAR"),
    MDTSTD("MDTSTD"),
    DNCRC("DNCRC"),
    ADRRC("ADRRC"),
    ADRRT("ADRRT"),
    CHCRC("CHCRC"),
    CHCRT("CHCRT");
    private final String value;

    ObservationTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ObservationTypeCode fromValue(String v) {
        for (ObservationTypeCode c: ObservationTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
