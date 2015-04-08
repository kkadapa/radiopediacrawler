package com.data;

import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karthik on 3/31/15.
 */
public class ArchiveHomePageData {

    public static void archiveHomePageData(ArrayList<ArrayList> homePageDataList) {

        ArrayList caseIdList = new ArrayList();
        ArrayList hyperLinkList = new ArrayList();
        ArrayList titleTextList = new ArrayList();
        ArrayList thumbImgList = new ArrayList();

        caseIdList.addAll(homePageDataList.get(0));
        hyperLinkList.addAll(homePageDataList.get(1));
        thumbImgList.addAll(homePageDataList.get(2));
        titleTextList.addAll(homePageDataList.get(3));

        try {

            DatabaseHelper databaseHelper = new DatabaseHelper();

            Connection connection = databaseHelper.connectionPool.reserveConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO RadiopediaCrawlData.ArchiveHomePageData (caseid,link,thumbimg,titletext) values (?,?,?,?) ");

            for (int i=0;i<hyperLinkList.size();i++){
                preparedStatement.setString(1, String.valueOf(caseIdList.get(i)));
                preparedStatement.setString(2, String.valueOf(hyperLinkList.get(i)));
                preparedStatement.setString(3, String.valueOf(thumbImgList.get(i)));
                preparedStatement.setString(4, String.valueOf(titleTextList.get(i)));
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit();

            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
