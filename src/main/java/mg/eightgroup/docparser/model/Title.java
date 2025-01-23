package mg.eightgroup.docparser.model;

import java.util.ArrayList;

public class Title {
    private String value;
    private String text;
    private ArrayList<Chapter> chapters;
    private ArrayList<Article> articles;

    public Title(String value, String text, ArrayList<Chapter> chapters, ArrayList<Article> articles) {
        this.value = value;
        this.text = text;
        this.chapters = chapters;
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

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public ArrayList<Article> allArticles() {
        ArrayList<Article> allArticles = new ArrayList<>();
        if (articles != null) {
            allArticles.addAll(articles);
        }
        if (chapters != null) {
            for (Chapter ch : chapters) {
                if (ch.getArticles() != null) {
                    allArticles.addAll(ch.getArticles());
                }
                if (ch.getSections() != null) {
                    for (Section s : ch.getSections()) {
                        if (s.getArticles() != null) {
                            allArticles.addAll(s.getArticles());
                        }
                        if (s.getSubsections() != null) {
                            for (Subsection sub : s.getSubsections()) {
                                if (sub.getArticles() != null) {
                                    allArticles.addAll(sub.getArticles());
                                }
                                if (sub.getSymbols() != null) {
                                    for (Symbol sym : sub.getSymbols()) {
                                        allArticles.addAll(sym.getArticles());
                                    }
                                }
                            }
                        }
                        if (s.getSymbols() != null) {
                            for (Symbol sym : s.getSymbols()) {
                                allArticles.addAll(sym.getArticles());
                            }
                        }
                    }
                }
            }
        }
        return allArticles;
    }
}
