package Elements;

import java.util.ArrayList;

public class DictionaryNode {
    String headWord = "";
    String type = "";
    ArrayList<String> afterWords = new ArrayList<>();
    ArrayList<String> beforeWords = new ArrayList<>();
    int numberOfWords;

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public String getHeadWord() {
        return headWord;
    }

    public void setHeadWord(String headWord) {
        this.headWord = headWord;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getAfterWords() {
        return afterWords;
    }

    public void setAfterWords(ArrayList<String> afterWords) {
        this.afterWords = afterWords;
    }

    public ArrayList<String> getBeforeWords() {
        return beforeWords;
    }

    public void setBeforeWords(ArrayList<String> beforeWords) {
        this.beforeWords = beforeWords;
    }
}
