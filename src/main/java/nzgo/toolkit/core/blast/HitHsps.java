//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.05 at 10:15:55 AM NZDT 
//


package nzgo.toolkit.core.blast;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "hsp"
})
@XmlRootElement(name = "Hit_hsps")
public class HitHsps {

    @XmlElement(name = "Hsp")
    protected List<Hsp> hsp;

    /**
     * Gets the value of the hsp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hsp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHsp().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Hsp }
     * 
     * 
     */
    public List<Hsp> getHsp() {
        if (hsp == null) {
            hsp = new ArrayList<Hsp>();
        }
        return this.hsp;
    }

}
