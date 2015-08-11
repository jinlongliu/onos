package org.onosproject.pcepio.types;

import java.util.LinkedList;
import java.util.ListIterator;

import org.onosproject.pcepio.protocol.PcepLabelObject;
import org.onosproject.pcepio.protocol.PcepLspObject;
import org.onosproject.pcepio.protocol.PcepSrpObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Provides Pcep Label.
 * REference :draft-zhao-pce-pcep-extension-for-pce-controller-01.
 */
public class PcepLabelDownload {

    protected static final Logger log = LoggerFactory.getLogger(PcepLabelDownload.class);

    //PCEP SPR Object
    PcepSrpObject srpObject;
    //PCEP LSP Object
    PcepLspObject lspObject;
    //LinkList of Labels
    LinkedList<PcepLabelObject> llLabelList;

    /*
     * Returns SRP Object.
     *
     * @return PCEP SRP Object
     */
    public PcepSrpObject getSrpObject() {
        return srpObject;
    }

    /*
     * Sets the Pcep Srp Object.
     *
     * @param srpobj PCEP SRP Object
     */
    public void setSrpObject(PcepSrpObject srpobj) {
        this.srpObject = srpobj;
    }

    /*
     * Returns LSP Object.
     *
     * @return PCEP LSP Object
     */
    public PcepLspObject getLspObject() {
        return lspObject;
    }

    /*
     * Sets the Pcep LSP Object.
     *
     * @param lspObject PCEP LSP Object
     */
    public void setLspObject(PcepLspObject lspObject) {
        this.lspObject = lspObject;
    }

    /*
     * Returns a list of labels.
     *
     * @return llLabelList list of pcep label objects
     */
    public LinkedList<PcepLabelObject> getLabelList() {
        return llLabelList;
    }

    /*
     * set the llLabelList list of type PcepLableObject.
     *
     * @param llLabelList list of pcep label objects
     */
    public void setLabelList(LinkedList<PcepLabelObject> llLabelList) {
        this.llLabelList = llLabelList;
    }

    /*
     * Prints the attribute of PcepLableObject.
     */
    public void print() {
        log.debug("LABEL DOWNLOAD:");
        srpObject.print();
        lspObject.print();

        log.debug("label-list:");
        ListIterator<PcepLabelObject> listIterator = llLabelList.listIterator();
        while (listIterator.hasNext()) {
            listIterator.next().print();
        }
    }
}