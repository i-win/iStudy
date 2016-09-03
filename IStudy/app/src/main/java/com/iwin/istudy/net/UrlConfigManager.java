package com.iwin.istudy.net;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.iwin.istudy.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by sxq on 2016/5/1.
 */
public class UrlConfigManager {
    private static final String TAG = "UrlConfigManager";

    public static UrlData findUrlData(final Activity activity,final String key){
        XmlResourceParser xmlParser = activity.getApplicationContext().getResources().getXml(R.xml.xml);
        try {
            while (xmlParser.next() != XmlPullParser.END_DOCUMENT){
                Log.d(TAG,"eventType"+xmlParser.getEventType());
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UrlData();
    }
}
