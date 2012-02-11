import Exceptions.PanteraException;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/2/12
 * Time: 6:34 PM
 * The aim of this class is to apply Pantera tagger for polish language to
 * source files hold by @SourceFile. If there exist disamb file (xml file
 * with Pantera output) Pantera is not called but instead @File representing
 * disamb file is converted to @Document. Otherwise Pantera is called and
 * produces disamb file which then is converted to Document. The whole
 * output is packed into @PanteraFile.
 */
public class SourceFilesToPanteraFiles {


    private String command = "pantera -o xces-disamb -t nkjp @ --no-guesser";
    public static final String XCES_ANA_IPI_DTD = "xcesAnaIPI.dtd";
    public static final String DISAMB = ".disamb";
    private static final  SourceFilesToPanteraFiles
            sourceFilesToPanteraFiles = new SourceFilesToPanteraFiles();

    //private SourceFilesToPanteraFiles(){};

    protected SourceFilesToPanteraFiles(){};

    public static SourceFilesToPanteraFiles getInstance(){
        return sourceFilesToPanteraFiles;
    }

    /*
    Process @SourceFile -> @PanteraFile. SourceFile.sourceFile  field
    should not be null;
     */
    public PanteraFile sourceFileToPanteraFile(SourceFile sourceFile) throws IOException, SAXException, ParserConfigurationException, PanteraException, InterruptedException {
        if (sourceFile.getDisambFile() != null)
            return new PanteraFile(sourceFile.getSourcefile()
                    ,xmlFileToXMLDocument(sourceFile.getDisambFile()),
                    sourceFile.getLabel());
        return new PanteraFile(sourceFile.getSourcefile(),
                applyPanteraToFile(sourceFile.getSourcefile()),
                sourceFile.getLabel());
    }
    
    protected Document xmlFileToXMLDocument(File disambXml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder docBuilder = buildPanteraXMLDoc();
        return docBuilder.parse(disambXml.getAbsolutePath());
    }

    protected Document applyPanteraToFile(File file ) throws ParserConfigurationException, SAXException, IOException, PanteraException, InterruptedException {

        String line;
        String cmd = command.replaceAll("@",file.getAbsolutePath());

        System.out.println("pantera on: " +file.getName());
        runPantera(cmd);

        DocumentBuilder docBuilder = buildPanteraXMLDoc();
        Document doc;


        doc = docBuilder.parse(file.getAbsolutePath()+ DISAMB);

        //PanteraException.PanteraExceptionChecker(p.getErrorStream());

        return doc;

    }

    protected void runPantera(String cmd) throws IOException, InterruptedException {

        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
    }

    /*
    Hack for not reading dtd in Pantera output.
     */
    private DocumentBuilder buildPanteraXMLDoc() throws ParserConfigurationException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        docBuilder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {
                if (systemId.contains(XCES_ANA_IPI_DTD)) {
                    return new InputSource(new StringReader(""));
                } else {
                    return null;
                }
            }
        });

        Document doc;
        return docBuilder;
    }
}
