package com.data;

import com.Model.DetailsDataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by karthik on 4/9/15.
 */
public class RadiopediaDetailsData {

    public static void insertRadiopediaDetailsData(DetailsDataModel detailsDataModel){


        try {

            DatabaseHelper databaseHelper = new DatabaseHelper();

            Connection connection = databaseHelper.connectionPool.reserveConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO RadiopediaCrawlData.RadiopediaDetailsData (caseid,curlink,jcarouselurl,dataref,titletext,headimgtitle,afterimagetext,modality,age,gender,race,imagetitledesc,presentationdetails,linumber) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

            if(detailsDataModel.getCaseId()!=null){

                preparedStatement.setString(1, detailsDataModel.getCaseId());
            }else{
                preparedStatement.setString(1, "");

            }

            if(detailsDataModel.getCurLink()==null){

                preparedStatement.setString(2, "");
            }else {
                preparedStatement.setString(2, detailsDataModel.getCurLink());

            }

            if(detailsDataModel.getJcarouselUrl()==null){

                preparedStatement.setString(3, "");
            }else{
                preparedStatement.setString(3, detailsDataModel.getJcarouselUrl());

            }

            if(detailsDataModel.getDataRef()==null){

                preparedStatement.setString(4, "");
            }else {
                preparedStatement.setString(4, detailsDataModel.getDataRef());

            }

            if(detailsDataModel.getTitleText()==null){
                preparedStatement.setString(5, "");

            }else {
                preparedStatement.setString(5, detailsDataModel.getTitleText());

            }

            if(detailsDataModel.getHeadImgTitle()==null){

                preparedStatement.setString(6,"");
            }else {
                preparedStatement.setString(6,detailsDataModel.getHeadImgTitle());

            }

            if(detailsDataModel.getAfterImageStr()==null){

                preparedStatement.setString(7, "");
            }else {
                preparedStatement.setString(7, detailsDataModel.getAfterImageStr());

            }

            if(detailsDataModel.getModality()==null){

                preparedStatement.setString(8, "");
            }else {
                preparedStatement.setString(8, detailsDataModel.getModality());

            }


if(detailsDataModel.getPatientMap().isEmpty()){

    preparedStatement.setString(9, "");
    preparedStatement.setString(10, "");
    preparedStatement.setString(11, "");

}else {


    if(detailsDataModel.getPatientMap().get("Age")==null) {
        preparedStatement.setString(9, "");
    }else{
        preparedStatement.setString(9, detailsDataModel.getPatientMap().get("Age").toString());

    }

        if(detailsDataModel.getPatientMap().get("Gender")==null) {

            preparedStatement.setString(10, "");
        }else{
            preparedStatement.setString(10, detailsDataModel.getPatientMap().get("Gender").toString());

        }

        if(detailsDataModel.getPatientMap().get("Race")==null) {
            preparedStatement.setString(11, "");
        }else{
            preparedStatement.setString(11, detailsDataModel.getPatientMap().get("Race").toString());

        }
}

            if(detailsDataModel.getImageTitleDesc()==null){

                preparedStatement.setString(12, "");
            }else{
                preparedStatement.setString(12, detailsDataModel.getImageTitleDesc());

            }
            if(detailsDataModel.getPresentationDetails()==null){

                preparedStatement.setString(13, "");
            }else{
                preparedStatement.setString(13, detailsDataModel.getPresentationDetails());

            }

            if(detailsDataModel.getLiNumber()==null){

                preparedStatement.setString(14, "");
            }else{
                preparedStatement.setString(14, detailsDataModel.getLiNumber());

            }

            preparedStatement.addBatch();

            preparedStatement.executeBatch();
            connection.commit();

            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
