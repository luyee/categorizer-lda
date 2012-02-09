import cc.mallet.types.Token;
import cc.mallet.types.TokenSequence;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 1/19/12
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentsToTokensParser {

    public static final String DISAMB = "disamb";
    public static final String BASE = "base";
    public static final String CTAG = "ctag";
    public static final String SUBST = "subst";

    private static final DocumentsToTokensParser DOCUMENTS_TO_TOKENS_PARSER = new DocumentsToTokensParser();

    private DocumentsToTokensParser(){}

    public static DocumentsToTokensParser getInstance(){
        return DOCUMENTS_TO_TOKENS_PARSER;
    }

    public TokenSequence panteraXMLtoTokenSequence( Document panteraXml){

        TokenSequence tokenSequence = new TokenSequence();

        //panteraXml.normalize();
        //System.out.println(panteraXml.toString());


        NodeList nList = panteraXml.getElementsByTagName("lex");


        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element lexElement = (Element) nNode;

                if (lexElement.hasAttribute(DISAMB)){

                    addToken(lexElement, tokenSequence);
                };

            }
        }
        return tokenSequence;

    }

    private void addToken(Element lexElement, TokenSequence tokenSequence) {
        if ( retrieveInformation(lexElement, CTAG).contains(SUBST) ){
             tokenSequence.add( new Token(retrieveInformation(lexElement,BASE)));
        }
    }

    private String retrieveInformation(Element lexElement, String attitude) {
        NodeList nlList = lexElement.getElementsByTagName(attitude).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue().toLowerCase().trim();
    }

}
