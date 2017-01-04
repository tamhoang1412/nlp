package ReadXMLFile;

import Elements.DictionaryNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadDictionaries {
    public static void getListWords (String s, ArrayList<String> listWords){
        if(s.length() == 0 || !s.contains(":")) return;
        String string = s;
        while(string.charAt(0) != ':'){
            string = string.substring(1);
        }
        if(string.length() < 2) return;
        string = string.substring(2);
        String[]listStrings = string.split(", ");
        for(String s1 : listStrings){
            listWords.add(s1);
        }
    }
    public static void load(ArrayList<DictionaryNode> dictionary){
        try{
            File folder = new File(Constant.DICTIONARIES_PATH);
            for(File file : folder.listFiles()){
                InputStream inputStream= new FileInputStream(file);
                Reader reader = new InputStreamReader(inputStream,"UTF-8");
                InputSource is = new InputSource(reader);
                is.setEncoding("UTF-8");

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document document = dBuilder.parse(is);

                NodeList listWord = document.getElementsByTagName("Entry");

                for(int i = 0; i < listWord.getLength(); i++){
                    Node wordElement = listWord.item(i);
                    if(wordElement.getNodeType() == Node.ELEMENT_NODE){
                        DictionaryNode dictionaryNode = new DictionaryNode();
                        Element element = (Element) wordElement;

                        String headWord = element.getElementsByTagName("HeadWord").item(0).getTextContent();
                        dictionaryNode.setHeadWord(headWord);

                        dictionaryNode.setNumberOfWords(headWord.split(" ").length);

                        if(element.getElementsByTagName("Syntactic").getLength() > 0){
                            Element syntactic = (Element) element.getElementsByTagName("Syntactic").item(0);

                            if(syntactic.getElementsByTagName("Category").getLength() > 0){
                                String type = syntactic.getElementsByTagName("Category").item(0).getTextContent();
                                dictionaryNode.setType(type);
                            }

                            if(syntactic.getElementsByTagName("After").getLength() > 0){
                                String afterWordsString = syntactic.getElementsByTagName("After").item(0).getTextContent();
                                ArrayList<String> afterWords = new ArrayList<>();
                                getListWords(afterWordsString, afterWords);
                                dictionaryNode.setAfterWords(afterWords);
                            }
                            if(syntactic.getElementsByTagName("Before").getLength() > 0){
                                String beforeWordsString = syntactic.getElementsByTagName("Before").item(0).getTextContent();
                                ArrayList<String> beforeWords = new ArrayList<>();
                                getListWords(beforeWordsString, beforeWords);
                                dictionaryNode.setBeforeWords(beforeWords);
                            }
                        }

                        dictionary.add(dictionaryNode);
                    }
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
