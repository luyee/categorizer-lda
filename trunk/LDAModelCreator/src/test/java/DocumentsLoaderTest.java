import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
* Created by IntelliJ IDEA.
* User: kacper
* Date: 1/17/12
* Time: 2:44 PM
* To change this template use File | Settings | File Templates.
*/
public class DocumentsLoaderTest extends junit.framework.TestCase {
    DocumentsLoader documentsLoader = new DocumentsLoader();
    private String pathname;

    protected void setUp() throws Exception {
        super.setUp();
        pathname = "/home/kacper/IdeaProjects/LDAModelCreator/src/test/testsDir/testDir";
    }


    @Test
    public void testSizeGetFiles() throws Exception {
        File testFolder = new File(pathname);
        Vector<SourceFile> result = documentsLoader.getFiles(testFolder);
        assertEquals(result.size(),2);
    }

    @Test
    public void  testNamesGetFiles() throws Exception {
        File testFolder = new File(pathname);
        Vector<SourceFile> result = documentsLoader.getFiles(testFolder);
        Vector<String> names = new Vector<String>();
        names.add(result.get(0).getSourcefile().getName());
        names.add(result.get(1).getSourcefile().getName());
        assertTrue(names.contains("file1.txt"));
        assertTrue(names.contains("file2.txt"));
    }

    @Test
    public void testConvertFile() throws InterruptedException, IOException {
        File testFolder = new File("/home/kacper/IdeaProjects/LDAModelCreator/src/test/testsDir/convertDir/ala.txt");
        documentsLoader.convert(testFolder);

    }




}
