package me.s4h.service;

import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.rome.io.XmlReader;
import me.s4h.entity.RssChannel;
import me.s4h.entity.RssItem;
import me.s4h.entity.User;
import me.s4h.repository.RssChannelRepository;
import me.s4h.repository.RssItemRepository;
import me.s4h.repository.UserRepository;
import me.s4h.util.FeedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

/**
 * Created by LENOVO on 2014/11/22.
 */
@Service
public class ReaderService {
    @Autowired
    RssChannelRepository channelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RssItemRepository itemRepository;

    // @Transactional(propagation = Propagation.REQUIRED, timeout = 7)
    public User addChannel(Long userId, String channelUrl) throws Exception {
        System.out.println("adding " + channelUrl + " to " + userId);
        RssChannel channel = addChannel(channelUrl);
        User user = userRepository.findOne(userId);
        user.addRssChannel(channel);
        return userRepository.save(user);
    }


    private RssChannel addChannel(String channelUrl) throws Exception {
        System.out.println("adding " + channelUrl);
        RssChannel channel = channelRepository.findByUrl(channelUrl);
        if (channel == null) {
            SyndFeed feed = FeedUtil.downloadAndParse(channelUrl);
            channel = new RssChannel(feed, channelUrl);
            channel = channelRepository.save(channel);
            Date lastUpdate = null;
            for (SyndEntry e : feed.getEntries()) {
                itemRepository.save(new RssItem(e, channel));
                if (lastUpdate == null ||
                        (e.getPublishedDate() != null && lastUpdate.before(e.getPublishedDate()))) {
                    lastUpdate = e.getPublishedDate();
                }
            }
            channel.setLastUpdate(lastUpdate);
            channel = channelRepository.save(channel);
        }
        System.gc();
        return channel;
    }

    @Async
    public void handleOpml(InputStream in, Long userId) {
        System.out.println("start opml");
        try {
            User user = userRepository.findOne(userId);

            WireFeedInput input = new WireFeedInput();
            Opml feed = (Opml) input.build(new InputSource(in));
            List<Outline> outlines = feed.getOutlines();
            for (Outline o : outlines) {
                for (Outline c : o.getChildren()) {
                    try {
                        RssChannel channel = this.addChannel(c.getXmlUrl());
                        user.addRssChannel(channel);
                    } catch (Exception e) {
                        System.err.println("fail to add " + c.getXmlUrl());
                        System.err.println(e.getMessage());
                    }
                }
            }
            user = userRepository.save(user);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        }
        System.out.println("end opml");
    }

}
