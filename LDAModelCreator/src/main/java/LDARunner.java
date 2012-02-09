import org.apache.commons.cli.*;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 1/19/12
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class LDARunner {

    public static final String TRAINING_DIR = "trainingDir";
    private String trainingDir;
    private static final String MALLR_FILE = "malletFile";
    private String malletFile;
    private static final String CONVERT = "convert";
    private boolean covnert;


    public LDARunner(CommandLine line) {
        this.trainingDir = line.getOptionValue(TRAINING_DIR);
        this.malletFile = line.getOptionValue(MALLR_FILE);
        this.covnert = line.hasOption(CONVERT);

    }

    private void run() throws Exception {
        System.out.println("taking data from: \n\t" + trainingDir);
        System.out.println("Saving model to: \n\t"+ malletFile);
        createMalletInputFile();
    }

    public void createMalletInputFile() throws Exception {
        DocumentsLoader documentsLoader = new DocumentsLoader();

        if (this.covnert) documentsLoader.setCp1250toUtf(true);

        Vector<SourceFile> sourceFiles= documentsLoader.getFiles(trainingDir);

        PanteraFile panteraFile;

        SourceFilesToPanteraFiles sourceFilesToPanteraFiles =
                SourceFilesToPanteraFiles.getInstance();

        PanteraFileToMalletFormat  panteraFileToMalletFormat
                = new PanteraFileToMalletFormat(malletFile);

        for (SourceFile sourceFile: sourceFiles){

            panteraFile =
                    sourceFilesToPanteraFiles.sourceFileToPanteraFile(sourceFile);

            panteraFileToMalletFormat.addToCvs(panteraFile);
        }

        panteraFileToMalletFormat.close();

    }
    



    public static void main(String args[]) throws Exception {
        Option trainingDirOpt   = OptionBuilder.withArgName(TRAINING_DIR)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of training directory")
                .create(TRAINING_DIR);

        Option malletFileOpt   = OptionBuilder.withArgName(MALLR_FILE)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of training directory")
                .create(MALLR_FILE);

        Option convert   = OptionBuilder.withArgName(CONVERT)
                .withDescription("specify the location of training directory")
                .create(CONVERT);


       Options options = new Options();

       options.addOption( trainingDirOpt );
       options.addOption(malletFileOpt);
        options.addOption(convert);

       CommandLineParser parser = new GnuParser();
       try {
            // parse the command line arguments
           CommandLine line = parser.parse( options, args );
           LDARunner ldaRunner = new LDARunner(line);
           ldaRunner.run();
       }
       catch( ParseException exp ) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
       }



    }



}


