package mg.eightgroup.docparser.model;

import java.util.ArrayList;

public class Subsection {

    private String value;
    private String text;
    private ArrayList<Symbol> symbols;
    private ArrayList<Article> articles;

    public Subsection(String value, String text, ArrayList<Symbol> symbols, ArrayList<Article> articles) {
        this.value = value;
        this.text = text;
        this.symbols = symbols;
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

    public ArrayList<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(ArrayList<Symbol> symbols) {
        this.symbols = symbols;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
