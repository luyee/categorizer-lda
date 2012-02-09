package util;

import java.io.*;
import java.util.UUID;

public class InputStremToTmpFile {
    public InputStremToTmpFile() {
    }

    public File convertToTmpFile(InputStream inputStream) throws IOException {

        File tmp = File.createTempFile(UUID.randomUUID().toString(), ".mallet");
        tmp.deleteOnExit();
        OutputStream out = new FileOutputStream(tmp);

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = inputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }

        inputStream.close();
        out.flush();
        out.close();

        return tmp;

    }
}