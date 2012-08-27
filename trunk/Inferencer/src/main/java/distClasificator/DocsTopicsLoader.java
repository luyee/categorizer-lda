package distClasificator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 3:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocsTopicsLoader {

    private String docsTopicsPath = null;
    private BufferedReader bufferedReader;
    private DataInputStream inputStream;


    public DocsTopicsLoader(String path) throws FileNotFoundException {
        docsTopicsPath = path;
        FileInputStream fstream = new FileInputStream(docsTopicsPath);
        inputStream = new DataInputStream(fstream);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public DocsTopicsLoader() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public DocsTopicsLoader(InputStream resourceAsStream) {
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

    public Map<String, Map<Integer, Double>> getItMaped() throws IOException {
        Map<String, Map<Integer, Double>> map = new HashMap<String, Map<Integer, Double>>();

        boolean jump = true;
        String strLine = getNext();

        while ((strLine = getNext()) != null) {

            String[] arr = strLine.split("\\s+");
            String cat = arr[1];

            Map<Integer, Double> mp = new TreeMap<Integer, Double>();
            for (int i = 2; i < arr.length; i = i + 2) {
                mp.put(new Integer(arr[i]), new Double(arr[i + 1]));
            }

            map.put(cat, mp);


        }

        close();
        return map;
    }


}
