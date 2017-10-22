/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class textMeta {
    public String filename;
    public String date;
    public String model;
    public String odID;
    public String dID;
    public String note;

    public textMeta(String filename, String model, String date, String note, String dID, String odID) {
        this.filename = filename;
        this.model = model;
        this.date = date;
        this.note = note;
        this.odID = odID;
        this.dID = dID;
    }
}
