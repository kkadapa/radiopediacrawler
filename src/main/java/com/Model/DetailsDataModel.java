package com.Model;

import java.util.List;
import java.util.Map;

/**
 * Created by karthik on 4/12/15.
 */
public class DetailsDataModel {

    String caseId;
    String titleText;
    List patientDataList;

    Map<String,String> patientMap;

    String modality;
    String dataRef;
    String afterImageStr;
    String curLink;
    String presentationDetails;
    String jcarouselUrl;
    String imageTitleDesc;
    String headImgTitle;
    String liNumber;


    public Map<String, String> getPatientMap() {
        return patientMap;
    }

    public void setPatientMap(Map<String, String> patientMap) {
        this.patientMap = patientMap;
    }

    public String getLiNumber() {
        return liNumber;
    }

    public void setLiNumber(String liNumber) {
        this.liNumber = liNumber;
    }

    public String getHeadImgTitle() {
        return headImgTitle;
    }

    public void setHeadImgTitle(String headImgTitle) {
        this.headImgTitle = headImgTitle;
    }

    public String getJcarouselUrl() {
        return jcarouselUrl;
    }

    public void setJcarouselUrl(String jcarouselUrl) {
        this.jcarouselUrl = jcarouselUrl;
    }

    public String getPresentationDetails() {
        return presentationDetails;
    }

    public void setPresentationDetails(String presentationDetails) {
        this.presentationDetails = presentationDetails;
    }

    public String getAfterImageStr() {
        return afterImageStr;
    }

    public void setAfterImageStr(String afterImageStr) {
        this.afterImageStr = afterImageStr;
    }

    public String getImageTitleDesc() {
        return imageTitleDesc;
    }

    public void setImageTitleDesc(String imageTitleDesc) {
        this.imageTitleDesc = imageTitleDesc;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getDataRef() {
        return dataRef;
    }

    public void setDataRef(String dataRef) {
        this.dataRef = dataRef;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public List getPatientDataList() {
        return patientDataList;
    }

    public void setPatientDataList(List patientDataList) {
        this.patientDataList = patientDataList;
    }

    public String getCurLink() {
        return curLink;
    }

    public void setCurLink(String curLink) {
        this.curLink = curLink;
    }

}
