import cc.mallet.types.TokenSequence;
import junit.framework.TestCase;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 1/19/12
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentsToTokensParserTest extends TestCase {

    public static final String PATHNAME = "/home/kacper/IdeaProjects/LDAModelCreator/src/test/testsDir/DocParserTest/ala.disamb";


    private DocumentsToTokensParser documentsToTokensParser;
    private TokenSequence tokenSequence;
    private Document doc;

    protected void setUp() throws Exception {
        super.setUp();
        File testFile = new File(PATHNAME);
        documentsToTokensParser = DocumentsToTokensParser.getInstance();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();


        docBuilder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId)
                    throws SAXException, IOException {
                if (systemId.contains("xcesAnaIPI.dtd")) {
                    return new InputSource(new StringReader(""));
                } else {
                    return null;
                }
            }
        });

        doc = docBuilder.parse(testFile);


    }

    @Test
    public void testTokenSequenceSize() throws Exception {
        tokenSequence = documentsToTokensParser.panteraXMLtoTokenSequence(doc);
        assertEquals(tokenSequence.size(),2);
    }

    @Test
    public void testTokenSequenceInside() {
        tokenSequence = documentsToTokensParser.panteraXMLtoTokenSequence(doc);
        Vector<String> names = new Vector<String>();
        names.add(tokenSequence.get(0).getText());
        names.add(tokenSequence.get(1).getText());
        assertTrue(names.contains("al"));
        assertTrue(names.contains("kuc"));
    }
}
