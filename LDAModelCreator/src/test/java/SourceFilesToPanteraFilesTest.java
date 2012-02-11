import Exceptions.PanteraException;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import junit.framework.TestCase;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/4/12
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SourceFilesToPanteraFilesTest extends TestCase {

    private String sourcePathname;
    private String disambPathname;

    /*
    mockito cannot mock/spy following:
  - anonymous classes
    We have to do class
     */
    private class SourceFilesToPanteraFileNotRunningPAntera
        extends  SourceFilesToPanteraFiles{
        @Override
        protected void runPantera(String cmd){
            System.out.println("Not running Pantera in tests");
        }
    }

    private SourceFilesToPanteraFiles srcToPnt =
            new SourceFilesToPanteraFileNotRunningPAntera();

    protected void setUp() throws Exception {
        super.setUp();
        sourcePathname =
            this.getClass().getClassLoader().getResource("Drama/Drama/file1.txt").getPath();
        disambPathname =
            this.getClass().getClassLoader().getResource("Drama/Drama/file1.txt.disamb").getPath();


    }

    @Test
    public void testApplyPanteraToFile() throws Exception {

        File testFolder = new File(sourcePathname);

        srcToPnt.applyPanteraToFile(testFolder);
    }

    @Test
    public void testXmlFileToXMLDocument() throws IOException, SAXException, ParserConfigurationException {
        File disambFile = new File(disambPathname);
        srcToPnt.xmlFileToXMLDocument(disambFile);

    }
    
    @Test
    public void testSourceFileToPanteraFile_ApplyPantera() throws IOException, PanteraException, SAXException, InterruptedException, ParserConfigurationException {
        SourceFilesToPanteraFiles spy = spy(srcToPnt);
        File srcfile = new File(sourcePathname);
        Document disambDoc = new DocumentImpl();

        when(spy.applyPanteraToFile(srcfile)).thenReturn(disambDoc);
        SourceFile sourceFile=new SourceFile(srcfile);

        assertEquals(
                spy.sourceFileToPanteraFile(sourceFile).getPanteraFile(),
                disambDoc);
    }
    
    @Test
    public void testSourceFileToPanteraFile_FiletoXml() throws IOException, PanteraException, SAXException, InterruptedException, ParserConfigurationException {
        SourceFilesToPanteraFiles spy = spy(srcToPnt);
        File srcfile = new File(sourcePathname);
        File disambFile = new File(disambPathname);
        Document disambDoc = new DocumentImpl();


        when(spy.xmlFileToXMLDocument(disambFile)).thenReturn(disambDoc);
        SourceFile sourceFile=new SourceFile(srcfile,disambFile,"label");


        assertEquals(
                spy.sourceFileToPanteraFile(sourceFile).getPanteraFile(),
                disambDoc);
    }
}
