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
 * <p>Java class for CHLOutcome_displayName.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CHLOutcome_displayName">
 *   &lt;restriction base="{urn:hl7-org:v3}st">
 *     &lt;enumeration value="Requires referral for Full Assessment for NHS Continuing Healthcare"/>
 *     &lt;enumeration value="Does not require referral for Full Assessment for NHS Continuing Healthcare"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CHLOutcome_displayName")
@XmlEnum
public enum CHLOutcomeDisplayName {

    @XmlEnumValue("Requires referral for Full Assessment for NHS Continuing Healthcare")
    REQUIRES_REFERRAL_FOR_FULL_ASSESSMENT_FOR_NHS_CONTINUING_HEALTHCARE("Requires referral for Full Assessment for NHS Continuing Healthcare"),
    @XmlEnumValue("Does not require referral for Full Assessment for NHS Continuing Healthcare")
    DOES_NOT_REQUIRE_REFERRAL_FOR_FULL_ASSESSMENT_FOR_NHS_CONTINUING_HEALTHCARE("Does not require referral for Full Assessment for NHS Continuing Healthcare");
    private final String value;

    CHLOutcomeDisplayName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CHLOutcomeDisplayName fromValue(String v) {
        for (CHLOutcomeDisplayName c: CHLOutcomeDisplayName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
