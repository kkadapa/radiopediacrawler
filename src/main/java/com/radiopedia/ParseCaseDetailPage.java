package com.radiopedia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by karthik on 4/1/15.
 */
public class ParseCaseDetailPage {

    public static void parseCaseDetails(String url){

        ArrayList<ArrayList> dataList = new ArrayList<ArrayList>();
        ArrayList hyperLinksList = new ArrayList();
        ArrayList caseIdList = new ArrayList();
        ArrayList titleTextList = new ArrayList();

        try{

            Document doc = Jsoup.connect(url).timeout(60 * 1000).get();

            Element content = doc.getElementById("content").getElementById("case-index");

            int ulsize = content.select("ul.column").size();
            Elements colElement = content.select("ul.column");

            for (int col = 0; col < ulsize; col++) {
                Elements getColumn = colElement.get(col).select("li.case");

                for (int li = 0; li < getColumn.size(); li++) {
                    Element liElements = getColumn.get(li);

                    caseIdList.add(liElements.select("li").attr("id"));

                    String caseId = liElements.select("li").attr("id");

                    Elements getHyperLink = liElements.getElementsByClass("case-link").select("a[href]");

                    for (Element el : getHyperLink) {
                        String curLink = el.attr("abs:href");
                        //  System.out.println("link is "+curLink);

                        parseDetailPageFromLink(curLink,caseId);
                        hyperLinksList.add(curLink);
                    }
                    String titleText = liElements.select("span.title-text").text();
                    titleTextList.add(titleText);

                    //  System.out.println("span title is "+titleText);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Logger.getLogger(ParseCaseReports.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * @param curLink this is the caseLink
     * @param caseId this is the high level case id for each case
     */
    private static void parseDetailPageFromLink(String curLink,String caseId) {

        ArrayList ageList = new ArrayList();
        ArrayList genderList = new ArrayList();
        ArrayList raceList = new ArrayList();
        ArrayList patientDataList = new ArrayList();
        ArrayList dataRefList = new ArrayList();
        ArrayList modalityList = new ArrayList();
        ArrayList imgList = new ArrayList();
        ArrayList subSectionDataList = new ArrayList();
        Map<String,ArrayList> listMap = new HashMap<String,ArrayList>();
        Map<String,Map<String,ArrayList>> mapMap = new HashMap<String,Map<String,ArrayList>>();


        try{
            Document doc = Jsoup.connect(curLink).timeout(60 * 1000).get();

            Element content = doc.getElementById("content-header");

            String title = content.select("h1").text();

            System.out.println("title is"+title);

            Element casePresentation = doc.getElementById("case-patient-presentation");

            String presentationDetail = casePresentation.select("p").text();

            System.out.println("presentationDetail are"+presentationDetail);

            Element casePatientData = doc.getElementById("case-patient-data");

            if(casePatientData!=null){

                Elements casePatientContent = casePatientData.select("div.content").select("div.data-item");

                System.out.println("details are "+casePatientContent.size());

                for(int j=0;j<casePatientContent.size();j++){
                    System.out.println("legends are "+casePatientContent.get(j).text());

                    if(casePatientContent.get(j).text().contains("Age")){
                        ageList.add(casePatientContent.get(j).text());
                        patientDataList.add(ageList);
                    }else if(casePatientContent.get(j).text().contains("Gender")){
                        genderList.add(casePatientContent.get(j).text());
                        patientDataList.add(genderList);
                    }else if(casePatientContent.get(j).text().contains("Race")){
                        raceList.add(casePatientContent.get(j).text());
                        patientDataList.add(raceList);
                    }
                }
            }

            Element content1 = doc.getElementById("content");

            Elements caseSection = content1.select("div.case-section.case-study");

            System.out.println("caseSection count is "+caseSection.size());
            String pageId = "";
            for(int c=0;c<caseSection.size();c++){

                String dataRef = caseSection.get(c).attr("data-ref");
                System.out.println("dataRef is"+dataRef);

                //This distinguishes each image case in a single page.....
                dataRefList.add(dataRef);

                Elements subSection = caseSection.get(c).select("div.sub-section.study-desc");

                if(subSection.size()!=0){
                    String headText = subSection.select("h2").text();
                    System.out.println("headText is "+headText);
                }

                String subSectionModality = caseSection.get(c).select("div.sub-section.study-modality").text();
                System.out.println("subSectionModality---- "+subSectionModality);
                modalityList.add(subSectionModality);

                Element imageGallery = caseSection.get(c).getElementById("case-images");



               //The below code is for jcarousel
                Elements jcarousel = imageGallery.select("ul.carousel.jcarousel-skin-radiopaedia");

                System.out.println("jcarousel images target  "+jcarousel);

                String dataUrl = "";

                for(Element element:jcarousel){
                    dataUrl = element.absUrl("data-resource-url");
                    System.out.println("dataurl is "+dataUrl);

                    //Getting PageID from URL
                    pageId = dataUrl.substring(dataUrl.indexOf("studies/")+8,dataUrl.indexOf("/carousel"));

                    listMap =  getCaseImagesForStoring(dataUrl);
                    mapMap.put(pageId, listMap);
                }

               //The below works for cases without jcarousels




            }
            saveImagesToDirectory(caseId,mapMap);
        }catch (Exception e){
            e.printStackTrace();
            Logger.getLogger(ParseCaseReports.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static Map getCaseImagesForStoring(String dataUrl) {


        ArrayList imgStackList = new ArrayList();
        Map<String,ArrayList> listMap = new HashMap<String,ArrayList>();

        try {

            Document doc = Jsoup.connect(dataUrl).timeout(60 * 1000).get();

            Elements liElements = doc.select("li[position]");

            int liPositionSize = liElements.size();

           // Elements ulStack = liElements.select("ul[class=stack]");

            System.out.println("ulStack is "+liPositionSize);

            String srcUrl = "";
            String imgStackId = "";
            String positionId = "";

            //for (Element el : ulStack) {

            for(int i=0;i<liPositionSize;i++) {

                Elements ulStack = liElements.get(i).select("ul[class=stack]");

                    Elements lipositionhref = liElements.get(i).select("a[href]");

                    positionId = lipositionhref.first().attr("abs:href");

                    positionId = positionId.substring(positionId.indexOf("images/")+7);

                    System.out.println("positionId is "+positionId);

                int liSize = ulStack.select("li").size();
                ArrayList srcURLList = new ArrayList();
                for(int licount=0;licount<liSize;licount++){

                    Elements srcElements = ulStack.select("a[href]");

                    srcUrl = srcElements.first().attr("abs:href");

                    System.out.println("alt Img link is src " + srcUrl);

                    srcURLList.add(srcUrl);
                }
                listMap.put(positionId,srcURLList);
            }
            //Persist the image case with the page number here
            //saveImagesToDirectory(caseId, pageId,listMap);

        }catch (Exception e){
            e.printStackTrace();
            Logger.getLogger(ParseCaseReports.class.getName()).log(Level.SEVERE, null, e);
        }

        return listMap;
    }

    /**
     * @param caseId for saving
     * @param mapMap which is a hashmap pageid and with folder of hashmaps (hashmaps have image id's with list of src image url's)
     */
    private static void saveImagesToDirectory(String caseId,Map mapMap) throws IOException {


        System.out.println("Caseid is"+caseId);
        String folder = "/Users/karthik/git/RadiopediaImages/";

        System.out.println("MapList is"+mapMap.toString());
        //System.out.println("listMap is"+listMap.toString());

        File dir = new File(folder, caseId);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String dataStack = "";
        String dataStack1 = "";
        File secdir;
        File thirddir;

        Map<String,ArrayList> mapData = new HashMap<String,ArrayList>();
        List mapList = new ArrayList();
        for (int i=0;i<mapMap.size();i++){

            Iterator iterator = mapMap.entrySet().iterator();

            while (iterator.hasNext()){
                Map.Entry pair = (Map.Entry)iterator.next();

                dataStack = pair.getKey().toString();
                mapData = (Map)pair.getValue();
                iterator.remove();

                secdir = new File(dir,dataStack);
                if (!secdir.exists()) {
                    secdir.mkdirs();
                }

                for (int j=0;j< mapData.size();j++) {
                    Iterator innerIterator = mapData.entrySet().iterator();

                    while (innerIterator.hasNext()) {

                        Map.Entry innerPair = (Map.Entry) innerIterator.next();
                        dataStack1 = innerPair.getKey().toString();
                        mapList = (List)innerPair.getValue();
                        innerIterator.remove();

                        thirddir = new File(secdir,dataStack1);
                        if (!thirddir.exists()) {
                            thirddir.mkdirs();
                        }

                        for(int k=0;k<mapList.size();k++){

                            String imgurl = mapList.get(k).toString();

                            System.out.println("Img url is "+imgurl);
                            String imgname = imgurl.substring(imgurl.indexOf("images/")+7,imgurl.lastIndexOf("/"));
                            //Open a URL Stream
                            URL url = new URL(imgurl);
                            InputStream in = url.openStream();

                            OutputStream out = new BufferedOutputStream(new FileOutputStream(thirddir+"/"+imgname+".jpg"));
                            for (int b; (b = in.read()) != -1; ) {
                                out.write(b);
                            }
                            out.close();
                            in.close();
                        }
                    }
                }
            }
        }
    }
}