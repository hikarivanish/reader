package me.s4h.fetcher;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import me.s4h.fetcher.entity.RssChannel;
import me.s4h.fetcher.entity.RssItem;
import me.s4h.fetcher.repository.RssChannelRepository;
import me.s4h.fetcher.repository.RssItemRepository;
import me.s4h.fetcher.util.FeedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    RssChannelRepository channelRepository;
    @Autowired
    RssItemRepository itemRepository;

    @Scheduled(initialDelay = 15000, fixedRate = 90 * 60 * 1000)
    void fetch() {
        System.out.println("begin " + Thread.currentThread().getName());
        List<RssChannel> channels = channelRepository.findAll();
        for (RssChannel channel : channels) {
            try {
                System.out.println("starting update " + channel.getUrl());
                SyndFeed feed = FeedUtil.downloadAndParse(channel.getUrl());
                Date publishedDate = channel.getPublishedDate();
                if (publishedDate != null && feed.getPublishedDate() != null
                        && publishedDate.getTime() == feed.getPublishedDate().getTime()) {
                    System.out.println("same publish:" + publishedDate);
                    continue;
                }

                Date lastUpdate = channel.getLastUpdate();
                Date newLastUpdate = lastUpdate;
                System.out.println("lastUpdate " + lastUpdate);
                for (SyndEntry e : feed.getEntries()) {
                    if (lastUpdate == null ||
                            (e.getPublishedDate() != null && lastUpdate.before(e.getPublishedDate()))) {
                        itemRepository.save(new RssItem(e, channel));
                        if (newLastUpdate == null || newLastUpdate.before(e.getPublishedDate())) {
                            newLastUpdate = e.getPublishedDate();
                        }
                        System.out.println("added:" + e.getTitle());
                    }
                }
                channel.setPublishedDate(feed.getPublishedDate());
                channel.setLastUpdate(newLastUpdate);
                channel = channelRepository.save(channel);
                System.gc();
            } catch (Exception e) {
                System.err.println("faild to update "+channel.getUrl());
                System.err.println(e.getMessage());
            }
        }
        System.gc();
        System.out.println("end " + Thread.currentThread().getName());
    }
}


//@Component
class Runner implements CommandLineRunner {
    @Autowired
    RssChannelRepository channelRepository;
    @Autowired
    RssItemRepository itemRepository;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("begin------------");
        List<RssChannel> channels = channelRepository.findAll();
        for (RssChannel channel : channels) {
            try {
                System.out.println("starting update " + channel.getUrl());
                Date beforeDate = channel.getPublishedDate();
                Date lastUpdate = channel.getLastUpdate();
                System.out.println("lastUpdate " + lastUpdate);
                SyndFeed feed = FeedUtil.downloadAndParse(channel.getUrl());


                Date newLastUpdate = lastUpdate;
                for (SyndEntry e : feed.getEntries()) {
                    System.out.println("e.getPublishedDate():" + e.getPublishedDate());
                    if (lastUpdate == null || lastUpdate.before(e.getPublishedDate())) {
                        itemRepository.save(new RssItem(e, channel));
                        if (newLastUpdate == null || newLastUpdate.before(e.getPublishedDate())) {
                            newLastUpdate = e.getPublishedDate();
                        }
                        System.out.println("added:" + e.getTitle());
                    }
                }
                channel.setPublishedDate(feed.getPublishedDate());
                channel.setLastUpdate(newLastUpdate);
                channel = channelRepository.save(channel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("finish-------------");
    }
}