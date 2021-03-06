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
 * <p>Java class for NHS111TriageDeviceType_displayName.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NHS111TriageDeviceType_displayName">
 *   &lt;restriction base="{urn:hl7-org:v3}st">
 *     &lt;enumeration value="TriageSystem Software"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NHS111TriageDeviceType_displayName")
@XmlEnum
public enum NHS111TriageDeviceTypeDisplayName {

    @XmlEnumValue("TriageSystem Software")
    TRIAGE_SYSTEM_SOFTWARE("TriageSystem Software");
    private final String value;

    NHS111TriageDeviceTypeDisplayName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NHS111TriageDeviceTypeDisplayName fromValue(String v) {
        for (NHS111TriageDeviceTypeDisplayName c: NHS111TriageDeviceTypeDisplayName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
