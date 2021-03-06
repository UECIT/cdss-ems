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
 * <p>Java class for AuthorFunctionType_displayName.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AuthorFunctionType_displayName">
 *   &lt;restriction base="{urn:hl7-org:v3}st">
 *     &lt;enumeration value="Originating Author"/>
 *     &lt;enumeration value="Sealing Author"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AuthorFunctionType_displayName")
@XmlEnum
public enum AuthorFunctionTypeDisplayName {

    @XmlEnumValue("Originating Author")
    ORIGINATING_AUTHOR("Originating Author"),
    @XmlEnumValue("Sealing Author")
    SEALING_AUTHOR("Sealing Author");
    private final String value;

    AuthorFunctionTypeDisplayName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AuthorFunctionTypeDisplayName fromValue(String v) {
        for (AuthorFunctionTypeDisplayName c: AuthorFunctionTypeDisplayName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
