package mg.eightgroup.docparser.model;

import java.util.ArrayList;

public class Section {

    private String value;
    private String text;
    private ArrayList<Subsection> subsections;
    private ArrayList<Symbol> symbols;
    private ArrayList<Article> articles;


    public Section(String value, String text, ArrayList<Subsection> subsections, ArrayList<Symbol> symbols, ArrayList<Article> articles) {
        this.value = value;
        this.text = text;
        this.subsections = subsections;
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

    public ArrayList<Subsection> getSubsections() {
        return subsections;
    }

    public void setSubsections(ArrayList<Subsection> subsections) {
        this.subsections = subsections;
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
