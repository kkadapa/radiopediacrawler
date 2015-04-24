package com.radiopedia;

import java.io.File;

/**
 * Created by karthik on 3/30/15.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("This is main class");

        //For All Pages Home Page Data
//        for(int i=1;i<=197;i++) {
//            String caseUrl = "http://radiopaedia.org/encyclopaedia/cases/all?page="+i;
//            ParseCaseReports.parseAllCases(caseUrl);
//            System.out.println("Page no "+i);
//        }

        //For All Pages Details Page Data
        for(int i=41;i<=100;i++) {
            String detailUrl = "http://radiopaedia.org/encyclopaedia/cases/all?page="+i;
            ParseCaseDetailPage.parseCaseDetails(detailUrl);
            System.out.println("Page no "+i);
        }
    }
}
