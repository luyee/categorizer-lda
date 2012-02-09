import cc.mallet.types.TokenSequence;
import org.w3c.dom.Document;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/2/12
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class PanteraFile {
    
    private File sourceFile=null;
    private String label;

    public File getSourceFile() {
        return sourceFile;
    }

    public Document getPanteraFile() {
        return panteraFile;
    }

    private Document panteraFile=null;
    private TokenSequence tokenSequence;

    public  boolean hasPanteraFile(){
        return panteraFile!=null;
    }

    public PanteraFile(File sourceFile,Document panterFile, String label){
        this.sourceFile = sourceFile;
        this.panteraFile = panterFile;
        this.label = label;

    }


    public TokenSequence getTokenSequence(){
        if (tokenSequence == null){
        tokenSequence =
                DocumentsToTokensParser.getInstance().
                        panteraXMLtoTokenSequence(panteraFile);
        }
        return tokenSequence;
    }


    public String getLabel() {
        return label;
    }
}
