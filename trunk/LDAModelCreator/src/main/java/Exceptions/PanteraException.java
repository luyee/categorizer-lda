package Exceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 1/19/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class PanteraException extends Exception{

    public PanteraException(String s, IOException e) {
        super(s,e);
    }

    public PanteraException(String s) {
       super(s);
    }

    public static void PanteraExceptionChecker(InputStream is) throws PanteraException, IOException {
        BufferedReader bre = new BufferedReader
                (new InputStreamReader(is));
        String err =new String();
        String line;
        try {
            while ((line = bre.readLine()) != null) {
                err += (line +"\n");
            }
        } catch (IOException e) {
            throw new  PanteraException("Can not read Pantera error stream",e);
        }
        if (! err.isEmpty()){
            throw new  PanteraException("Pantera exception "+err);
        }
        bre.close();
    }

}
