package com.inhatc.attendance;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class NetworkThread extends Thread{

    public static ArrayList<String> list_routeNo = new ArrayList<String>();

    @Override
    public void run() {
        boolean isRouteNo = false;
        String routeNo = "";
        String busStopId = SelectBusActivity.busStopId;

        Log.e("please...", busStopId);

        try {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/6280000/busStationService/getBusStationViaRouteList");
            Log.e("MY_TEST","urlBuilder");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=o2ZQsjx6tHMpSiMv4%2F%2Bs19g4MSzBBranfpGsX1GM8D5dIKCHug1AsFBLSi7W%2FygXlomkyXKv8WD4W7uFGSDNAA%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("bstopId", "UTF-8") + "=" + URLEncoder.encode(busStopId, "UTF-8"));
            URL url = new URL(urlBuilder.toString());

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("Start Parsing...");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("ROUTENO")){ //title 만나면 내용을 받을수 있게 하자
                            isRouteNo = true;
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(isRouteNo){ //isTitle이 true일 때 태그의 내용을 저장.
                            routeNo = parser.getText();
                            list_routeNo.add(routeNo);
                            isRouteNo = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                parserEvent = parser.next();
            }

            for(int i=0; i<list_routeNo.size(); i++) {
                Log.e("Bus : ", list_routeNo.get(i));
            }

//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Content-type", "application/json");
//            BufferedReader rd;
//            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//            }
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = rd.readLine()) != null) {
//                sb.append(line);
//            }
//            Log.e("BUS_API_TEST",sb.toString());
//            rd.close();
//            conn.disconnect();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
