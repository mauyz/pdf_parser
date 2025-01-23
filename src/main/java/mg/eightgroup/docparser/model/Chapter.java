package mg.eightgroup.docparser.model;

import java.util.ArrayList;

public class Chapter {
    private String value;
    private String text;
    private  ArrayList<Section> sections;
    private ArrayList<Article> articles;

    public Chapter(String value, String text, ArrayList<Section> sections, ArrayList<Article> articles) {
        this.value = value;
        this.text = text;
        this.sections = sections;
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

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
