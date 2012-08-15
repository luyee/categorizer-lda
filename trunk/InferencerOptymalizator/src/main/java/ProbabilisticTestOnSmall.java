import java.util.Arrays;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProbabilisticTestOnSmall {

    public static  void main(String args[]) throws Exception {
        String topic0 =
                "darwin ewolucja teoria gatunek dobór cecha człowiek praca populacja proces to mechanizm badanie czas rok temat";
        String topic1=
                "architektura sztuka modernizm budynek architekt dzieło rok forma gaudí to fotografia rozwój definicja materiał projekt idea czas dom";

        //Inference inference = new Inference();

        Vector<String> tp0 = new Vector<String>(Arrays.asList(topic0.split("\\s+")));
        Vector<String> tp1 = new Vector<String>(Arrays.asList(topic1.split("\\s+")));

        //Vector<String> res = inference.inferenceCategories(tp0);


        //System.out.println(res.toString());

        //res =   inference.inferenceCategories(tp1);

        //System.out.println(res.toString());

    }

}
