
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabor
 */
public class comparableMediaFile{
    File file;
    meta meta;

    comparableMediaFile(File fileIn, meta metaIn) {
        this.file = fileIn;
        this.meta = metaIn;
    }
}              

