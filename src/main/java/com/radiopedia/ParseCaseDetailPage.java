package com.radiopedia;

import com.Model.DetailsDataModel;
import com.data.RadiopediaDetailsData;
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

        try{

            Document doc = Jsoup.connect(url).timeout(60 * 1000).get();

            Element content = doc.getElementById("content").getElementById("case-index");

            int ulsize = content.select("ul.column").size();
            Elements colElement = content.select("ul.column");

            for (int col = 0; col < ulsize; col++) {
                Elements getColumn = colElement.get(col).select("li.case");

                for (int li = 0; li < getColumn.size(); li++) {
                    Element liElements = getColumn.get(li);

                    //CaseId
                    String caseId = liElements.select("li").attr("id");

                    Elements getHyperLink = liElements.getElementsByClass("case-link").select("a[href]");

                    //caseTitle
                    String titleText = liElements.select("span.title-text").text();
                    //CaseURL
                    for (Element el : getHyperLink) {
                        String curLink = el.attr("abs:href");

                        parseDetailPageFromLink(curLink,caseId,titleText);
                    }
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
    private static void parseDetailPageFromLink(String curLink,String caseId,String titleText) {

        List patientDataList = new ArrayList();
        Map<String,String> patientMap = new HashMap<String, String>();

        List dataRefList = new ArrayList();
        List modalityList = new ArrayList();
        List<String> subDescList = new ArrayList<String>();
        Map<String,ArrayList> listMap;
        Map<String,Map<String,ArrayList>> mapMap = new HashMap<String,Map<String,ArrayList>>();


        try{
            Document doc = Jsoup.connect(curLink).timeout(60 * 1000).get();

            Element content = doc.getElementById("content-header");

            String title = content.select("h1").text();

            System.out.println("title is"+title);

            Element casePresentation = doc.getElementById("case-patient-presentation");

            String presentationDetail = "";
            //CasePresentation is the Presentation Data Title
            if(casePresentation!=null){

                 presentationDetail = casePresentation.select("p").text();
                System.out.println("presentationDetail are"+presentationDetail);
            }

            // Patient's Age,Gender,Race Details
            Element casePatientData = doc.getElementById("case-patient-data");

            if(casePatientData!=null){

                if(casePatientData!=null){

                    Elements casePatientContent = casePatientData.select("div.content").select("div.data-item");

                    System.out.println("details are "+casePatientContent.size());

                    for(int j=0;j<casePatientContent.size();j++){
                        System.out.println("legends are "+casePatientContent.get(j).text());

                        if(casePatientContent.get(j).text().contains("Age")){

                            //patientDataList.add(casePatientContent.get(j).text());
                            patientMap.put("Age",casePatientContent.get(j).text());
                        }else if(casePatientContent.get(j).text().contains("Gender")){

                            //patientDataList.add(casePatientContent.get(j).text());
                            patientMap.put("Gender",casePatientContent.get(j).text());

                        }else if(casePatientContent.get(j).text().contains("Race")){

                            //patientDataList.add(casePatientContent.get(j).text());
                            patientMap.put("Race",casePatientContent.get(j).text());
                            }
                        }
                    }
                }

            //CaseSection Images starting
            Element content1 = doc.getElementById("content");

            Elements caseSection = content1.select("div.case-section.case-study");
            DetailsDataModel detailsDataModel = new DetailsDataModel();

            System.out.println("caseSection count is "+caseSection.size());
            String pageId = "";
            for(int c=0;c<caseSection.size();c++){

                String dataRef = caseSection.get(c).attr("data-ref");
                System.out.println("dataRef is"+dataRef);

                //This distinguishes each image case in a single page.....
                dataRefList.add(dataRef);

                Elements subSection = caseSection.get(c).select("div.sub-section.study-desc");

                String headText = "";
                if(subSection.size()!=0){
                    headText = subSection.select("h2").text();
                    System.out.println("headText is "+headText);
                }



                String subSectionModality = caseSection.get(c).select("div.sub-section.study-modality").text();

                if(subSectionModality!=null){
                    System.out.println("subSectionModality---- "+subSectionModality);
                    modalityList.add(subSectionModality);
                }

                Elements subSectionDescription = caseSection.get(c).select("div.sub-section.study-findings.body");

                int subDescPSize = subSectionDescription.select("p").size();

                //List<String> afterImageTextList = new ArrayList<String>(subDescPSize);
                StringBuffer afterImageString = new StringBuffer(subDescPSize);
                for(int s=0;s<subDescPSize;s++){
                    afterImageString.append(subSectionDescription.select("p").get(s).text());
                    System.out.println("subSectionDescription " + subSectionDescription.select("p").get(s).text());
                }

                Element imageGallery = caseSection.get(c).getElementById("case-images");

               //The below code is for jcarousel
                Elements jcarousel = imageGallery.select("ul.carousel.jcarousel-skin-radiopaedia");

                System.out.println("jcarousel images target  "+jcarousel);

                //The below works for cases without jcarousels
                if(jcarousel.isEmpty()){

                    //The below code is for the text below images
                    Elements textBelowImg = caseSection.get(c).select("div.main-desc");

                    StringBuffer stringBuffer = new StringBuffer();
                    for (Element text:textBelowImg){

                       String titleTextImg = text.select("div.title").select("h3").text();
                        System.out.println("txtBeneathImg ***** "+titleTextImg);
                        stringBuffer.append(titleTextImg+" ");

                        String descTextImg = text.select("div.description").text();
                        System.out.println("descTextImg$$$ "+descTextImg);
                        stringBuffer.append(descTextImg+" ");
                    }

                    String imgSrc = "";
                    String imgName = "";

                    Elements ulStack = imageGallery.select("ul[class=stack]");
                    int liSize = ulStack.select("li").size();
                    Elements liOfStack = ulStack.select("li");

                    ArrayList srcURLList = new ArrayList();
                    for(int licount=0;licount<liSize;licount++){

                        //Elements altImg = ulStack.select("img");

                        Element altImg = liOfStack.get(licount);

                        Elements imgElemen = altImg.select("img");

                        if(imgElemen.isEmpty()){

                            Elements hrefElements = altImg.select("a[href]");

                            imgSrc = hrefElements.attr("abs:href");

                            System.out.println("alt Img link is src " + imgSrc);
                            srcURLList.add(imgSrc);

                        }else {

                            for (Element ele:imgElemen){
                                imgSrc = ele.absUrl("src");
                                System.out.println("alt Img link is src " + imgSrc);
                                srcURLList.add(imgSrc);
                            }
                        }
                    }

                    String folder = "/Users/karthik/git/RadiopediaImages/";
                    File dir = new File(folder, caseId);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File secdir = new File(dir,dataRef);
                    if (!secdir.exists()) {
                        secdir.mkdirs();
                    }

                    for(int k=0;k<srcURLList.size();k++){

                        imgName = srcURLList.get(k).toString().substring(srcURLList.get(k).toString().indexOf("images/") + 7, srcURLList.get(k).toString().lastIndexOf("/"));
                        URL url = new URL(srcURLList.get(k).toString());
                        InputStream in = url.openStream();

                        OutputStream out = new BufferedOutputStream(new FileOutputStream(secdir+"/"+imgName+".jpg"));
                        for (int b; (b = in.read()) != -1; ) {
                            out.write(b);
                        }
                        out.close();
                        in.close();
                    }

                    //Insert Row here for non jcarousel
                    //titleText,caseid,patientDataList,imgName,presentationDetails,description,afterImageList
                    detailsDataModel.setCaseId(caseId);
                    detailsDataModel.setTitleText(titleText);
                    detailsDataModel.setPatientMap(patientMap);
                    detailsDataModel.setModality(subSectionModality);
                    detailsDataModel.setDataRef(dataRef);
                    detailsDataModel.setAfterImageStr(afterImageString.toString());
                    detailsDataModel.setCurLink(curLink);
                    detailsDataModel.setImageTitleDesc(stringBuffer.toString());
                    detailsDataModel.setPresentationDetails(presentationDetail);
                    detailsDataModel.setHeadImgTitle(headText);
                    detailsDataModel.setJcarouselUrl("");
                    detailsDataModel.setLiNumber("");

                    //Insert data for Jcarousel empty
                  // RadiopediaDetailsData.insertRadiopediaDetailsData(detailsDataModel);
                }

                //If Jcarousel is nonEmpty pass values inside this
                String dataUrl = "";

                for(Element element:jcarousel){
                    dataUrl = element.absUrl("data-resource-url");
                    System.out.println("dataurl is "+dataUrl);

                    //Getting PageID from URL
                    pageId = dataUrl.substring(dataUrl.indexOf("studies/")+8,dataUrl.indexOf("/carousel"));
                    detailsDataModel = new DetailsDataModel();
                    //listMap =  getCaseImagesForStoring(dataUrl,pageId,afterImageString.toString(),caseId,patientDataList,titleText);

                    detailsDataModel.setCaseId(caseId);
                    detailsDataModel.setTitleText(titleText);
                    detailsDataModel.setPatientMap(patientMap);

                    //detailsDataModel.setModality(subSectionModality);

                    detailsDataModel.setDataRef(dataRef);
                    detailsDataModel.setAfterImageStr(afterImageString.toString());
                    detailsDataModel.setCurLink(curLink);

                    //detailsDataModel.setImageTitleDesc(stringBuffer.toString());

                    detailsDataModel.setPresentationDetails(presentationDetail);
                    detailsDataModel.setJcarouselUrl(dataUrl);

                    listMap =  getCaseImagesForStoring(detailsDataModel);
                    mapMap.put(pageId, listMap);
                }
            }
           saveImagesToDirectory(caseId,mapMap);
        }catch (Exception e){
            e.printStackTrace();
            Logger.getLogger(ParseCaseReports.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private static Map getCaseImagesForStoring(DetailsDataModel detailsDataModel) {

        Map<String,ArrayList> listMap = new HashMap<String,ArrayList>();
        String srcUrl = "";
        String positionId = "";

        try {
            Document doc = Jsoup.connect(detailsDataModel.getJcarouselUrl()).timeout(60 * 1000).get();

            Elements liElements = doc.select("li[position]");

            int liPositionSize = liElements.size();

            System.out.println("ulStack is "+liPositionSize);

            for(int i=0;i<liPositionSize;i++) {

                Element liNumberElement = liElements.get(i).select("a[href]").first();
                String liNumbStr = liNumberElement.attr("abs:href");

                liNumbStr = liNumbStr.substring(liNumbStr.indexOf("images/")+7);

                detailsDataModel.setLiNumber(liNumbStr);

                Elements modalityElements = liElements.get(i).select("div.sub-section.study-modality");

                //Modality of one section of the case
                System.out.println("val is "+modalityElements.text());

                detailsDataModel.setModality(modalityElements.text());
                //Description of one section of the case
                StringBuffer imgTitleDesc = new StringBuffer();
                String description = liElements.get(i).select("description").text();
                String titleTextImg = liElements.get(i).select("div.hidden-desc").select("div.title").select("h3").text();

                imgTitleDesc.append(description+" ");
                imgTitleDesc.append(titleTextImg);
                detailsDataModel.setImageTitleDesc(imgTitleDesc.toString());

                Elements subSection = liElements.get(i).select("div.sub-section.study-desc");

                String headText = "";
                if(subSection.size()!=0){
                    headText = subSection.select("h2").text();
                    System.out.println("headText is "+headText);
                }
                detailsDataModel.setHeadImgTitle(headText);

                Elements ulStack = liElements.get(i).select("ul[class=stack]");

                    Elements lipositionhref = liElements.get(i).select("a[href]");

                    positionId = lipositionhref.first().attr("abs:href");

                    positionId = positionId.substring(positionId.indexOf("images/")+7);

                    System.out.println("positionId is "+positionId);

                int liSize = ulStack.select("li").size();
                ArrayList srcURLList = new ArrayList();
                Elements srcElements = ulStack.select("li > a[href]");
                for(int licount=0;licount<liSize;licount++){

                    srcUrl = srcElements.get(licount).attr("abs:href");

                    System.out.println("alt Img link is src " + srcUrl);

                    srcURLList.add(srcUrl);
                }
                listMap.put(positionId,srcURLList);

                //Jcarousel Persist the image case with the page number here for
              //  RadiopediaDetailsData.insertRadiopediaDetailsData(detailsDataModel);

            }
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

                        String imgurl = "";
                        String imgname = "";
                        for(int k=0;k<mapList.size();k++){

                            imgurl = mapList.get(k).toString();

                            System.out.println("Img url is "+imgurl);
                            imgname = imgurl.substring(imgurl.indexOf("images/")+7,imgurl.lastIndexOf("/"));
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