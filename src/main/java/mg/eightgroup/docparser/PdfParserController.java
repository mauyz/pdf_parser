package mg.eightgroup.docparser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXML;
import mg.eightgroup.docparser.model.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfParserController {

    @FXML
    protected void onLoadButtonClick() {

        try (PDDocument pdDocument = Loader.loadPDF(new RandomAccessReadBufferedFile(Objects.requireNonNull(getClass()
                .getResource("/code.pdf")).getPath()))) {
            PDFTextStripper textStripper = new PDFTextStripper();
            int totalPage = pdDocument.getNumberOfPages();
            StringBuilder introduction = new StringBuilder();
            StringBuilder codeContent = new StringBuilder();
            for (int page = 1; page <= totalPage; page++) {
                textStripper.setStartPage(page);
                textStripper.setEndPage(page);
                String content = textStripper.getText(pdDocument).replaceFirst("^.*\\R", "");
                if (page < 3) {
                    introduction.append(content.strip());
                } else {
                    codeContent.append(content.strip());
                }
            }
            String regex = "\\bTITRE\\s+[A-Z]+(?:\\\\s+[A-Z])*\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(codeContent);
            ArrayList<Title> titles = new ArrayList<>();
            while (matcher.find()) {
                int matchEndIndex = matcher.end();
                int nextLineStart = codeContent.indexOf("\n", matchEndIndex) + 1; // Start of the next line
                int nextLineEnd = codeContent.indexOf("\n", nextLineStart);
                String titleValue = codeContent.substring(nextLineStart, nextLineEnd);
                int endTitle = codeContent.indexOf("TITRE", nextLineStart);
                String titleContent = endTitle == -1 ? codeContent.substring(nextLineStart)
                        : codeContent.substring(nextLineStart, endTitle);
                ArrayList<Article> articles = null;
                ArrayList<Chapter> chapters = null;
                if (titleContent.contains("DES DISPOSITIONS TRANSITOIRES ET FINALES")) {
                    articles = getArticles(titleContent.trim());
                } else {
                    chapters = getChapters(titleContent.trim());
                }
                Title title = new Title(matcher.group(), titleValue.strip(),
                        chapters, articles);
                titles.add(title);
            }
            WorkCode workCode = new WorkCode(introduction.toString(), titles);
            saveJson(workCode);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveJson(WorkCode code) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter("code.json")) {
            gson.toJson(code, writer);
            System.out.println("Object saved as JSON file: ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Chapter> getChapters(String content) {
        ArrayList<Chapter> chapters = new ArrayList<>();
        String regex = "\\bCHAPITRE\\s+[A-Z]+(?:\\s+[A-Z])*\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int matchEndIndex = matcher.end();
            String rgx = "([A-ZÉÈÀÙÂÊÎÔÛÄËÏÖÜÇ][A-Z'’,\\s]*)";
            Pattern p = Pattern.compile(rgx);
            Matcher m = p.matcher(content.substring(matchEndIndex));
            if (m.find()) {
                String chapterValue = m.group(0)
                        .replace("\nA", "")
                        .replace("\nS", "").strip();

                int beginChapter = content.indexOf(chapterValue) + chapterValue.length();
                int endChapter = content.indexOf("CHAPITRE", beginChapter);
                String chapterContent = (endChapter == -1 ? content.substring(beginChapter)
                        : content.substring(beginChapter, endChapter)).trim();
                ArrayList<Section> sections = null;
                ArrayList<Article> articles = null;
                if (chapterContent.startsWith("Article") && chapterContent.contains("Section 1")) {
                    int sectionStart = chapterContent.indexOf("Section 1");
                    articles = getArticles(chapterContent.substring(0, sectionStart - 1).trim());
                    sections = getSections(chapterContent.substring(sectionStart).trim());
                } else if (chapterContent.startsWith("Section")) {
                    sections = getSections(chapterContent);
                } else {
                    articles = getArticles(chapterContent);
                }
                Chapter chapter = new Chapter(matcher.group(), chapterValue, sections, articles);
                chapters.add(chapter);
            }
        }
        return chapters;
    }

    private ArrayList<Section> getSections(String content) {
        ArrayList<Section> sections = new ArrayList<>();
        String regex = "(?<!\\bla\\s)\\b(Sections?)\\s+(première|\\d+)?\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int matchEndIndex = matcher.end() + 1;
            String rgx = "(Sous-section|Article|§[1-9][0-9]*)";
            Pattern p = Pattern.compile(rgx);
            Matcher m = p.matcher(content.substring(matchEndIndex));
            if (m.find()) {
                int sectionValueStart = content.substring(matchEndIndex).indexOf(m.group(0));
                String sectionValue = content.substring(matchEndIndex).substring(0, sectionValueStart);
                int beginSection = content.indexOf(sectionValue) + sectionValue.length();
                int endSection = content.indexOf("Section", beginSection);
                String sectionContent = endSection == -1 ? content.substring(beginSection).trim()
                        : content.substring(beginSection, endSection).trim();
                ArrayList<Subsection> subsections = null;
                ArrayList<Symbol> symbols = null;
                ArrayList<Article> articles = null;
                if (sectionContent.startsWith("Sous-section")) {
                    subsections = getSubSections(sectionContent);
                } else if (sectionContent.startsWith("§")) {
                    symbols = getSymbols(sectionContent);
                } else if (sectionContent.startsWith("Article") && sectionContent.contains("§")) {
                    int sectionStart = sectionContent.indexOf("§1");
                    articles = getArticles(sectionContent.substring(0, sectionStart - 1).trim());
                    symbols = getSymbols(sectionContent.substring(sectionStart).trim());
                } else {
                    articles = getArticles(sectionContent);
                }
                Section section = new Section(matcher.group(), sectionValue.replace(":", "").trim(), subsections, symbols, articles);
                sections.add(section);
            }
        }
        return sections;

    }

    private ArrayList<Subsection> getSubSections(String content) {
        ArrayList<Subsection> sections = new ArrayList<>();
        String regex = "\\b(Sous-section)\\s+(première|\\d)*+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int matchEndIndex = matcher.end() + 1;
            String rgx = "(Article|§[1-9][0-9]*)";
            Pattern p = Pattern.compile(rgx);
            Matcher m = p.matcher(content.substring(matchEndIndex));
            if (m.find()) {
                int sectionValueStart = m.start();
                String sectionValue = content.substring(matchEndIndex).substring(0, sectionValueStart).replace(":", "");
                int beginSection = content.indexOf(sectionValue) + sectionValue.length();
                int endSection = content.indexOf("Sous-section", beginSection);
                String sectionContent = endSection == -1 ? content.substring(beginSection).trim()
                        : content.substring(beginSection, endSection).trim();

                ArrayList<Symbol> symbols = null;
                ArrayList<Article> articles = null;
                if (sectionContent.startsWith("§")) {
                    symbols = getSymbols(sectionContent);
                } else {
                    articles = getArticles(sectionContent);
                }
                Subsection subsection = new Subsection(matcher.group(), sectionValue.trim(), symbols, articles);
                sections.add(subsection);
            }
        }
        return sections;
    }

    private ArrayList<Symbol> getSymbols(String content) {
        ArrayList<Symbol> sections = new ArrayList<>();
        String regex = "§[1-9]*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int matchEndIndex = matcher.end() + 1;
            String rgx = "\\bArticle\\b";
            Pattern p = Pattern.compile(rgx);
            Matcher m = p.matcher(content.substring(matchEndIndex));
            if (m.find()) {
                int sectionValueStart = m.start();
                String sectionValue = content.substring(matchEndIndex).substring(0, sectionValueStart).replace(":", "");
                int beginSection = content.indexOf(sectionValue) + sectionValue.length();
                int endSection = content.indexOf("§", beginSection);
                String sectionContent = endSection == -1 ? content.substring(beginSection).trim()
                        : content.substring(beginSection, endSection).trim();
                Symbol symbol = new Symbol(matcher.group(), sectionValue.strip(), getArticles(sectionContent));
                sections.add(symbol);
            }
        }
        return sections;
    }

    private ArrayList<Article> getArticles(String content) {
        ArrayList<Article> articles = new ArrayList<>();
        String regex = "\\b(Article|Art.)\\s+(premier|\\d)*+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            int beginArticleContent = matcher.end() + 1;
            int endTArticle = content.indexOf("Article", beginArticleContent);
            if (endTArticle == -1) {
                endTArticle = content.indexOf("Art.", beginArticleContent);
            }
            String articleContent = endTArticle == -1 ? content.substring(beginArticleContent).replaceFirst("-", "").trim()
                    : content.substring(beginArticleContent, endTArticle).replaceFirst("-", "").trim();
            Article article = new Article(matcher.group(), articleContent);
            articles.add(article);
        }
        return articles;
    }
}