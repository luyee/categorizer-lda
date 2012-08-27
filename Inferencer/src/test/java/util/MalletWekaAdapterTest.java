package util;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.InstanceList;
import org.apache.log4j.Logger;
import org.junit.Test;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 20/08/12
 * Time: 20:53
 * To change this template use File | Settings | File Templates.
 */
public class MalletWekaAdapterTest {

    private Logger logger = Logger.getLogger(MalletWekaAdapter.class);

    // TODO this is I.T test
    @Test
    public void testToInstances() throws Exception {
        //String trainingCsvPath ="/home/kacper/IdeaProjects/LDAModelCreator/src/test/testsDir/UTFtest/engfile";
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new CharSequenceLowercase());

        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{M}]+")));
        //pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList trainingInstances = new InstanceList(new SerialPipes(pipeList));

        String trainingCsvPath = "/home/kacper/dev/lda/categorizer-lda/data/12/12Data.txt";
        Reader fileReader = new InputStreamReader(new FileInputStream(new File(trainingCsvPath)), "UTF-8");
        trainingInstances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1)); // data, label, name fields

        MalletWekaAdapter malletWekaAdapter = new MalletWekaAdapter();
        Instances instances = malletWekaAdapter.toInstances(trainingInstances);

        assertEquals(instances.numInstances(),12*6);
        Set<String> cats = new HashSet<String>();

        for (int i=0;i<instances.numInstances();i++){
            Instance instance = instances.instance(i);
            logger.debug(instance.toString());
            cats.add(instance.toString(1));
        }
        String names ="AdventureSurvival, AncientHistory, Business, AccountingBankingMoney," +
                " CareersEmploymentHumanResources, ArmamentArmscontrol, AsianHistory," +
                "AustralianOceanianHistory, ArtArchitecturePhotography, AfricanHistory, Biology, " +
                "AlternativeMedicineNaturalHealing";
        assertEquals(cats.size(),12);
        for (String cat:cats){
            assertTrue(names.contains(cat));
        }

        String file = "'112 113 114 29 113 115 116 117 118 77 34 48 49 119 108 120 120 121 122 119 108 120 " +
                "123 124 125 118 104 126 127 124 128 127 129 130 131 129 120 132 133 63 134 135" +
                " 136 29 137 138 139 140 39 112 141 142 53 143 29 112 95 144 145 146 147 91 121 121 112 148" +
                " 149 142 150 151 152 151'";

        assertEquals(instances.instance(1).toString(0),file);

    }
    @Test
    public void testToInstances2() throws Exception {
        //String trainingCsvPath ="/home/kacper/IdeaProjects/LDAModelCreator/src/test/testsDir/UTFtest/engfile";
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add(new CharSequenceLowercase());

        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{M}]+")));
        //pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList trainingInstances = new InstanceList(new SerialPipes(pipeList));

        String trainingCsvPath = "/home/kacper/dev/lda/categorizer-lda/data/12/12Data.txt";
        Reader fileReader = new InputStreamReader(new FileInputStream(new File(trainingCsvPath)), "UTF-8");
        trainingInstances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1)); // data, label, name fields

        MalletWekaAdapter malletWekaAdapter = new MalletWekaAdapter();
        Instances instances = malletWekaAdapter.toInstances(trainingInstances);
        instances = malletWekaAdapter.tfidf(instances);

        assertEquals(instances.numInstances(),12*6);

        for (int i=0;i<instances.numInstances();i++){
            Instance instance = instances.instance(i);
            logger.debug(instance.numAttributes());
            logger.debug(Arrays.toString(instance.toDoubleArray()));
        }


    }

}




