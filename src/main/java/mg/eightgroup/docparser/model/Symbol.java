package mg.eightgroup.docparser.model;

import java.util.ArrayList;

public class Symbol {
    private String value;
    private String text;
    private ArrayList<Article> articles;

    public Symbol(String value, String text, ArrayList<Article> articles) {
        this.value = value;
        this.text = text;
        this.articles = articles;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
