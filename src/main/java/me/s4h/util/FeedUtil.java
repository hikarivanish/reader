package me.s4h.util;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by LENOVO on 2014/12/4.
 */
public class FeedUtil {
    private FeedUtil(){}

    public static SyndFeed downloadAndParse(String url) throws IOException, FeedException {
        URL feedUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) feedUrl.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0(compatible;MSIE 5.0;Windows NT;DigExt)");
        conn.setReadTimeout(5 * 1000);
       return new SyndFeedInput().build(new XmlReader(conn));
    }
}
