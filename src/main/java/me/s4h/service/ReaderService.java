package me.s4h.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import me.s4h.entity.RssChannel;
import me.s4h.entity.RssItem;
import me.s4h.entity.User;
import me.s4h.repository.RssChannelRepository;
import me.s4h.repository.RssItemRepository;
import me.s4h.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;

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

    @Transactional(propagation= Propagation.REQUIRED,timeout = 7)
    public User addChannel(Long userId, String channelUrl) throws Exception {
        RssChannel channel = channelRepository.findByUrl(channelUrl);
        if (channel == null) {
            URL feedUrl = new URL(channelUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            channel = new RssChannel(feed, channelUrl);
            channel = channelRepository.save(channel);
            for (SyndEntry e : feed.getEntries()) {
                itemRepository.save(new RssItem(e, channel));
            }
        }
        User user = userRepository.findOne(userId);
        user.addRssChannel(channel);
        return userRepository.save(user);
    }


}
