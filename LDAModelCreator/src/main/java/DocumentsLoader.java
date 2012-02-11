import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 1/17/12
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentsLoader {


    public static final String DISAMB = ".disamb";
    private Vector<SourceFile> filesToParse ;
    private Set<String> possibleCats;

    private boolean cp1250toUtf=false;
    private String iconv =
            "iconv --from-code=cp1250 --to-code=utf-8 @fromFile@";

    public DocumentsLoader() throws IOException {
        possibleCats = new HashSet<String>();
        InputStream is =this.getClass().getClassLoader().getResourceAsStream("cat.txt");
        DataInputStream in = new DataInputStream(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        
        while ((strLine = br.readLine()) != null)   {
            strLine.trim();
            possibleCats.add(strLine);
        }
        //Close the input stream
        in.close();

    }

    public void setCp1250toUtf(boolean cp1250toUtf) {
        this.cp1250toUtf = cp1250toUtf;
    }

    public Vector<SourceFile> getFiles(File folder) throws Exception {
        filesToParse = new Vector<SourceFile>();
        getFilesFromFolder(folder);
        
        return  filesToParse;
    }


    private void getFilesFromFolder(File folder) throws Exception {
         if (!folder.exists()){
             throw  new IOException("folder does not exist:" +folder.getAbsolutePath());
         }

        if (folder.isDirectory()) {
            for (final File fileEntry : folder.listFiles()) {

                getFilesFromFolder(fileEntry);
            }
        } else{
            if (!folder.getName().contains(".txt")){
                throw new Exception("only txt files :"+ folder.getAbsolutePath()
                        +"folder is dir: " +folder.isDirectory());
            }
            if (!folder.getName().contains(DISAMB)  ){
                if(cp1250toUtf){
                    String name = folder.getAbsolutePath();
                    convert(folder);
                    folder = new File(name);
                }
                if(! possibleCats.contains(folder.getParentFile().getName())){
                    throw new Exception("no such category : "+folder.getParentFile().getName() );
                }
                File disamb=hasDISAMBPanteraFile(folder);
                filesToParse.add(new SourceFile(folder,disamb,folder.getParentFile().getName()));
            }

        }
        
    }

    protected void convert(File file) throws IOException, InterruptedException {

        String name = file.getAbsolutePath();
        String cmd = iconv.replace("@fromFile@",file.getAbsolutePath());
        String tmpFile = file.getParentFile().getAbsolutePath()+"/tmp";

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(tmpFile), "UTF-8"));

        System.out.println(cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        BufferedReader bri = new BufferedReader
                (new InputStreamReader(p.getInputStream()));

        String line;
        while ((line = bri.readLine()) != null) {
            out.write(line);
            out.write("\n");
        }
        bri.close();
        out.close();

        p.waitFor();

        File tmp = new File(tmpFile);
        file.delete();
        File newUtfFile = new File(name);
        tmp.renameTo(newUtfFile);
        tmp.delete();
    }

    private File hasDISAMBPanteraFile( File file){
        String fileName = file.getName();
        for (final File fileEntry: file.getParentFile().listFiles() ){
            if (fileEntry.getName().equals(fileName+DISAMB) ){
                return fileEntry;
            }

        }
        return null;
    }


    public Vector<SourceFile> getFiles(String docsToBeLoaded) throws Exception {
        return this.getFiles(new File(docsToBeLoaded));
    }
}
