import cc.mallet.types.Token;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/5/12
 * Time: 12:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PanteraFileToMalletFormat {

    private String csvPathname;
    private BufferedWriter out;
    private Integer id;

    public PanteraFileToMalletFormat(String csvPath) throws IOException {
        this.csvPathname = csvPath;
        out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(csvPath), "UTF-8"));
        id = new Integer(1);
        
    }


    public void addToCvs(PanteraFile panteraFile) throws IOException {
        out.write(id.toString()+"_"+
                panteraFile.getLabel()+"_"+
                panteraFile.getSourceFile().getName());
        out.write("\t");
        out.write(panteraFile.getLabel());
        out.write("\t");
        for (Token token : panteraFile.getTokenSequence()){
            out.write(token.getText()+" ");
        };
        out.write(".\n");
        id = new Integer(id.intValue()+1);
    }

    public void close() throws IOException {
        out.close();
    }

}
