import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/2/12
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SourceFile {

    public String getLabel(){
        return label;
    }
    public File getSourcefile() {
        return sourcefile;
    }

    public void setSourcefile(File sourcefile) {
        this.sourcefile = sourcefile;
    }

    public File getDisambFile() {
        return disambFile;
    }

    public void setDisambFile(File disambFile) {
        this.disambFile = disambFile;
    }

    public SourceFile( File sourceFile, File disambFile, String label) {
        this.label = label;
        this.disambFile = disambFile;
        this.sourcefile = sourceFile;
    }

    public SourceFile(File sourceFile) {
        this.sourcefile = sourceFile;
    }

    private File sourcefile =null;
    private File disambFile=null;
    private String label="-";
    
}
