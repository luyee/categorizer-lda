package evaluator;

import java.io.*;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/9/12
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataLoader {

    private String evaluationPath;
    private DataInputStream inputStream;
    private BufferedReader bufferedReader;

    public DataLoader(String path) throws FileNotFoundException {
        evaluationPath = path;
        FileInputStream fstream = new FileInputStream(evaluationPath);
        inputStream = new DataInputStream(fstream);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }


    public DataLoader(InputStream resourceAsStream) {
        inputStream = new DataInputStream(resourceAsStream);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }


    protected void close() throws IOException {
        inputStream.close();
    }

    protected String getNext() throws IOException {
        String strLine;
        if ((strLine = bufferedReader.readLine()) != null) {
            return strLine;
        }
        return null;

    }

    public Vector<String> getEvaluationInsatnces() throws IOException {
        Vector<String> ret = new Vector<String>();

        String strLine;

        while ((strLine = getNext()) != null) {
            ret.add(strLine);
        }

        close();
        return ret;
    }
}
