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
 * <p>Java class for ActRelationshipPertains_X.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ActRelationshipPertains_X">
 *   &lt;restriction base="{urn:hl7-org:v3}cs">
 *     &lt;enumeration value="PERT"/>
 *     &lt;enumeration value="NAME"/>
 *     &lt;enumeration value="AUTH"/>
 *     &lt;enumeration value="COVBY"/>
 *     &lt;enumeration value="EXPL"/>
 *     &lt;enumeration value="PREV"/>
 *     &lt;enumeration value="REFV"/>
 *     &lt;enumeration value="SUBJ"/>
 *     &lt;enumeration value="CAUS"/>
 *     &lt;enumeration value="DRIV"/>
 *     &lt;enumeration value="ITEMSLOC"/>
 *     &lt;enumeration value="MFST"/>
 *     &lt;enumeration value="LIMIT"/>
 *     &lt;enumeration value="REFR"/>
 *     &lt;enumeration value="SUMM"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ActRelationshipPertains_X")
@XmlEnum
public enum ActRelationshipPertainsX {

    PERT,
    NAME,
    AUTH,
    COVBY,
    EXPL,
    PREV,
    REFV,
    SUBJ,
    CAUS,
    DRIV,
    ITEMSLOC,
    MFST,
    LIMIT,
    REFR,
    SUMM;

    public String value() {
        return name();
    }

    public static ActRelationshipPertainsX fromValue(String v) {
        return valueOf(v);
    }

}
