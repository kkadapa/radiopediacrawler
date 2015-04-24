package com.radiopedia;

import java.io.*;
import java.net.URL;

/**
 * Created by karthik on 4/3/15.
 */
public class Test {

    public static void main(String[] args)  {


        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("1");
        stringBuffer.append("2");

        System.out.println("Stringbuffer content is ^^^^"+stringBuffer.toString());


    }
}

//class TestFolders throws IOException {
//
//    public static void main(String[] args) {
//
//
//        String folder = "/Users/karthik/git/RadiopediaImages/";
//
//        File dir = new File(folder, "case-26930");
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        File secdir = new File(folder + "case-26930", "27106");
//        if (!secdir.exists()) {
//            secdir.mkdirs();
//        }
//        System.out.println("Files Created");
//
//        File thirddir = new File(secdir, "5604225");
//        if (!thirddir.exists()) {
//            thirddir.mkdirs();
//        }
//
//        String imgurl = "http://images.radiopaedia.org/images/5604225/0e7bc0530019f4e101c133a6d19f21_big_gallery.png";
//        String imgname = imgurl.substring(imgurl.indexOf("images/") + 7, imgurl.lastIndexOf("/"));
//
//        URL url = new URL(imgurl);
//        InputStream in = url.openStream();
//
//        OutputStream out = new BufferedOutputStream(new FileOutputStream(thirddir + "/" + imgname + ".jpg"));
//        for (int b; (b = in.read()) != -1; ) {
//            out.write(b);
//        }
//        out.close();
//        in.close();
//    }
//}
