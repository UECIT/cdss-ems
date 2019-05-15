//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.03.15 at 03:22:47 PM GMT 
//


package uk.nhs.ctp.service.report.org.hl7.v3;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for COCD_TP145203GB03.Organization complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="COCD_TP145203GB03.Organization">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{urn:hl7-org:v3}II">
 *                 &lt;attribute name="root" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{urn:hl7-org:v3}uid">
 *                       &lt;enumeration value="2.16.840.1.113883.2.1.3.2.4.19.1"/>
 *                       &lt;enumeration value="2.16.840.1.113883.2.1.3.2.4.19.2"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="extension" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{urn:hl7-org:v3}st">
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="name" type="{urn:hl7-org:v3}ON"/>
 *         &lt;element name="standardIndustryClassCode" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{urn:hl7-org:v3}CV">
 *                 &lt;attribute name="code" use="required" type="{urn:hl7-org:v3}CDAOrganizationType_code" />
 *                 &lt;attribute name="displayName" use="required" type="{urn:hl7-org:v3}CDAOrganizationType_displayName" />
 *                 &lt;attribute name="codeSystem" use="required" type="{urn:hl7-org:v3}uid" fixed="2.16.840.1.113883.2.1.3.2.4.17.191" />
 *                 &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}cs_NullFlavor" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="templateId">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{urn:hl7-org:v3}II">
 *                 &lt;attribute name="root" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{urn:hl7-org:v3}uid">
 *                       &lt;enumeration value="2.16.840.1.113883.2.1.3.2.4.18.2"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="extension" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{urn:hl7-org:v3}st">
 *                       &lt;enumeration value="COCD_TP145203GB03#representedOrganization"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}cs" fixed="ORG" />
 *       &lt;attribute name="determinerCode" use="required" type="{urn:hl7-org:v3}cs" fixed="INSTANCE" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCD_TP145203GB03.Organization", propOrder = {
    "id",
    "name",
    "standardIndustryClassCode",
    "templateId"
})
public class COCDTP145203GB03Organization {
	
    @XmlElement(required = true)
    protected COCDTP145203GB03Organization.Id id;
    @XmlElement(required = true)
    protected ON name;
    protected COCDTP145203GB03Organization.StandardIndustryClassCode standardIndustryClassCode;
    @XmlElement(required = true)
    protected COCDTP145203GB03Organization.TemplateId templateId;
    @XmlAttribute(name = "classCode", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String classCode;
    @XmlAttribute(name = "determinerCode", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String determinerCode;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "updateMode")
    protected CsUpdateMode updateMode;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link COCDTP145203GB03Organization.Id }
     *     
     */
    public COCDTP145203GB03Organization.Id getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCDTP145203GB03Organization.Id }
     *     
     */
    public void setId(COCDTP145203GB03Organization.Id value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link ON }
     *     
     */
    public ON getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link ON }
     *     
     */
    public void setName(ON value) {
        this.name = value;
    }

    /**
     * Gets the value of the standardIndustryClassCode property.
     * 
     * @return
     *     possible object is
     *     {@link COCDTP145203GB03Organization.StandardIndustryClassCode }
     *     
     */
    public COCDTP145203GB03Organization.StandardIndustryClassCode getStandardIndustryClassCode() {
        return standardIndustryClassCode;
    }

    /**
     * Sets the value of the standardIndustryClassCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCDTP145203GB03Organization.StandardIndustryClassCode }
     *     
     */
    public void setStandardIndustryClassCode(COCDTP145203GB03Organization.StandardIndustryClassCode value) {
        this.standardIndustryClassCode = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * @return
     *     possible object is
     *     {@link COCDTP145203GB03Organization.TemplateId }
     *     
     */
    public COCDTP145203GB03Organization.TemplateId getTemplateId() {
        return templateId;
    }

    /**
     * Sets the value of the templateId property.
     * 
     * @param value
     *     allowed object is
     *     {@link COCDTP145203GB03Organization.TemplateId }
     *     
     */
    public void setTemplateId(COCDTP145203GB03Organization.TemplateId value) {
        this.templateId = value;
    }

    /**
     * Gets the value of the classCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassCode() {
        if (classCode == null) {
            return "ORG";
        } else {
            return classCode;
        }
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassCode(String value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the determinerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeterminerCode() {
        if (determinerCode == null) {
            return "INSTANCE";
        } else {
            return determinerCode;
        }
    }

    /**
     * Sets the value of the determinerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeterminerCode(String value) {
        this.determinerCode = value;
    }

    /**
     * Gets the value of the nullFlavor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nullFlavor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNullFlavor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

    /**
     * Gets the value of the updateMode property.
     * 
     * @return
     *     possible object is
     *     {@link CsUpdateMode }
     *     
     */
    public CsUpdateMode getUpdateMode() {
        return updateMode;
    }

    /**
     * Sets the value of the updateMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CsUpdateMode }
     *     
     */
    public void setUpdateMode(CsUpdateMode value) {
        this.updateMode = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{urn:hl7-org:v3}II">
     *       &lt;attribute name="root" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{urn:hl7-org:v3}uid">
     *             &lt;enumeration value="2.16.840.1.113883.2.1.3.2.4.19.1"/>
     *             &lt;enumeration value="2.16.840.1.113883.2.1.3.2.4.19.2"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="extension" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{urn:hl7-org:v3}st">
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Id
        extends II
    {


    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{urn:hl7-org:v3}CV">
     *       &lt;attribute name="code" use="required" type="{urn:hl7-org:v3}CDAOrganizationType_code" />
     *       &lt;attribute name="displayName" use="required" type="{urn:hl7-org:v3}CDAOrganizationType_displayName" />
     *       &lt;attribute name="codeSystem" use="required" type="{urn:hl7-org:v3}uid" fixed="2.16.840.1.113883.2.1.3.2.4.17.191" />
     *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}cs_NullFlavor" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class StandardIndustryClassCode
        extends CV
    {


    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{urn:hl7-org:v3}II">
     *       &lt;attribute name="root" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{urn:hl7-org:v3}uid">
     *             &lt;enumeration value="2.16.840.1.113883.2.1.3.2.4.18.2"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="extension" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{urn:hl7-org:v3}st">
     *             &lt;enumeration value="COCD_TP145203GB03#representedOrganization"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class TemplateId
        extends II
    {


    }

}
