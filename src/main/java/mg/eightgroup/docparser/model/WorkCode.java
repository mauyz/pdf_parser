package mg.eightgroup.docparser.model;

import java.util.ArrayList;

public class WorkCode {
    private String introduction;
    private ArrayList<Title> titles;

    public WorkCode(String introduction, ArrayList<Title> titles) {
        this.introduction = introduction;
        this.titles = titles;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public ArrayList<Title> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<Title> titles) {
        this.titles = titles;
    }
}
