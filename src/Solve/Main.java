package Solve;

import Elements.CombinationString;
import Elements.DictionaryNode;
import ReadXMLFile.Constant;
import ReadXMLFile.LoadDictionaries;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args){
        ArrayList<DictionaryNode> dictionaries = new ArrayList<>();
        LoadDictionaries.load(dictionaries);

        int maxLength = 0;
        for(int i = 0; i < dictionaries.size(); i++){
            int currentLength = dictionaries.get(i).getHeadWord().split(" ").length;
            maxLength = Math.max(currentLength, maxLength);
        }

        try{
            File inputFile = new File(Constant.INPUT_FILE_PATH);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
            String inputText = reader.readLine();
            inputText = inputText.replace(",", " ,").replace(".", " .").replace("?", " ?").replace("...", " ...").replace("!", " !").replace(")", " )").replace("(", " (");
            String[] words = inputText.split(" ");
            ArrayList<String>wordsList = new ArrayList<>(Arrays.asList(words));

            int offset = 0;
            while (true){
                if (!Character.isUpperCase(wordsList.get(offset).charAt(0))) {
                    offset++;
                    continue;
                }
                while (Character.isUpperCase(wordsList.get(offset + 1).charAt(0))){
                    String word = wordsList.get(offset) + " " + wordsList.get(offset + 1);
                    wordsList.set(offset, word);
                    wordsList.remove(offset + 1);
                }
                offset++;
                if(offset == wordsList.size()-1) break;
            }

            wordsList.set(0, wordsList.get(0).substring(0,1).toLowerCase() + wordsList.get(0).substring(1));

            int combinationSize =  maxLength;
            while(combinationSize > 0){
                ArrayList<CombinationString> combinationStringsList = new ArrayList<>();
                if(wordsList.size() >= combinationSize){
                    for(int i = 0; i < wordsList.size() - combinationSize + 1; i++){
                        String combinationString = "";
                        for(int j = i; j < i + combinationSize; j++){
                            combinationString += wordsList.get(j) + " ";
                        }
                        combinationString = combinationString.substring(0, combinationString.length() - 1);
                        CombinationString combinationStringObject = new CombinationString();
                        combinationStringObject.setString(combinationString);
                        combinationStringObject.setFrom(i);
                        combinationStringObject.setTo(i+combinationSize);
                        combinationStringsList.add(combinationStringObject);
                    }
                }

                int startPosition = 0;
                int numberOfCombination = 0;
                int[] from = new int[Constant.MAX_NUMBER_OF_COMBINATION];
                int[] to = new int[Constant.MAX_NUMBER_OF_COMBINATION];

                for(CombinationString combinationString : combinationStringsList){
                    if(combinationString.getFrom() >= startPosition){
                        for(DictionaryNode dictionaryNode : dictionaries){
                            if(dictionaryNode.getNumberOfWords() == combinationSize){
                                if(dictionaryNode.getHeadWord().equals(combinationString.getString())){
                                    wordsList.set(combinationString.getFrom(), combinationString.getString());
                                    from[numberOfCombination] = combinationString.getFrom();
                                    to[numberOfCombination] = combinationString.getTo() - 1;
                                    numberOfCombination++;
                                    startPosition = combinationString.getTo();
                                    break;
                                }
                            }
                        }
                    }
                }
                int numberRemove = 0;
                for(int i = 0; i < numberOfCombination; i++){
                    for(int j = from[i]; j < to[i]; j++){
                        wordsList.remove(from[i] + 1 - numberRemove);
                    }
                    numberRemove += to[i] - from[i];
                }
                combinationSize--;
            }

            boolean fixed = false;
            while(!fixed){
                for(int i = 0; i < wordsList.size(); i++){
                    if(wordsList.get(i).split(" ").length == 1){
                        boolean isBeforeWord = false;
                        boolean isAfterWord = false;
                        if(i > 0){
                            for(DictionaryNode dictionaryNode : dictionaries){
                                if(dictionaryNode.getHeadWord().equals(wordsList.get(i-1))){
                                    for(String afterWord : dictionaryNode.getAfterWords()){
                                        if(afterWord.equals(wordsList.get(i))){
                                            isAfterWord = true;
                                        }
                                    }
                                }
                                if(isAfterWord) break;
                            }
                        }
                        if(i < wordsList.size()-1){
                            for(DictionaryNode dictionaryNode : dictionaries){
                                if(dictionaryNode.getHeadWord().equals(wordsList.get(i+1))){
                                    for(String beforeWord : dictionaryNode.getBeforeWords()){
                                        if(beforeWord.equals(wordsList.get(i))){
                                            isBeforeWord = true;
                                        }
                                    }
                                }
                                if(isBeforeWord) break;
                            }
                        }

                        if(isAfterWord){
                            wordsList.set(i, wordsList.get(i-1) + " " + wordsList.get(i));
                            wordsList.remove(wordsList.get(i-1));
                        }
                        if(isBeforeWord){
                            wordsList.set(i, wordsList.get(i) + " " + wordsList.get(i+1));
                            wordsList.remove(wordsList.get(i+1));
                        }
                        if(isAfterWord || isBeforeWord) break;
                    }
                    if(i == wordsList.size()-1) fixed = true;
                }
            }
            wordsList.set(0, wordsList.get(0).substring(0,1).toUpperCase() + wordsList.get(0).substring(1));

            for(String word : wordsList){
                word = word.replace(" ,", ",").replace(" .", ".").replace(" ?", "?").replace(" ...", "...").replace(" !", "!").replace(" )", ")").replace(" (", "(");
                word = word.replace(" ", "_") + " ";
                System.out.print(word);
            }
        } catch(Exception e){
            e.printStackTrace();
        }


    }
}
