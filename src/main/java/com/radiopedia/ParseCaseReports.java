package com.radiopedia;

import com.data.ArchiveHomePageData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by karthik on 3/30/15.
 */
public class ParseCaseReports {


    public static void parseAllCases(String url) {

        ArrayList<ArrayList> dataList = new ArrayList<ArrayList>();
        ArrayList hyperLinksList = new ArrayList();
        ArrayList caseIdList = new ArrayList();
        ArrayList titleTextList = new ArrayList();
        ArrayList thumbImgList = new ArrayList();

        try {
            Document doc = Jsoup.connect(url).timeout(60 * 1000).get();

            Element content = doc.getElementById("content").getElementById("case-index");

            int ulsize = content.select("ul.column").size();
            Elements colElement = content.select("ul.column");

            for (int col = 0; col < ulsize; col++) {
                Elements getColumn = colElement.get(col).select("li.case");

                for (int li = 0; li < getColumn.size(); li++) {
                    Element liElements = getColumn.get(li);

                    caseIdList.add(liElements.select("li").attr("id"));

                    Elements getHyperLink = liElements.getElementsByClass("case-link").select("a[href]");

                    for (Element el : getHyperLink) {
                        String curLink = el.attr("abs:href");
                        //  System.out.println("link is "+curLink);
                        hyperLinksList.add(curLink);
                    }

                    String titleText = liElements.select("span.title-text").text();
                    titleTextList.add(titleText);

                    //  System.out.println("span title is "+titleText);

                    Elements altImg = liElements.select("img");

                    String src = "";

                    for (Element el : altImg) {
                        src = el.absUrl("src");
                        thumbImgList.add(src);
                       // System.out.println("alt Img link is src " + src);
                    }
                }
            }

            dataList.add(caseIdList);
            dataList.add(hyperLinksList);
            dataList.add(thumbImgList);
            dataList.add(titleTextList);

            ArchiveHomePageData.archiveHomePageData(dataList);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(ParseCaseReports.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
