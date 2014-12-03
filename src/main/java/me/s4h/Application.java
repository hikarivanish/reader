package me.s4h;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import me.s4h.entity.RssChannel;
import me.s4h.entity.RssItem;
import me.s4h.entity.User;
import me.s4h.repository.RssChannelRepository;
import me.s4h.repository.RssItemRepository;
import me.s4h.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import sun.net.www.protocol.http.HttpURLConnection;

import javax.persistence.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableAsync
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    RssChannelRepository channelRepository;
    @Autowired
    RssItemRepository itemRepository;

    @Scheduled(initialDelay = 15000, fixedDelay = 4000)
    public void doSomething() {
        System.out.println("begin------------");
        List<RssChannel> channels = channelRepository.findAll();
        for (RssChannel channel : channels) {
            try {
                System.out.println("starting update " + channel.getUrl());
                URLConnection conn = new HttpURLConnection(new URL(channel.getUrl()), null);
                conn.setConnectTimeout(7000);
                conn.setReadTimeout(7000);//important
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(conn));
                Date beforeDate = channel.getPublishedDate();
                System.out.println("beforeDate " + beforeDate);
                channel.setPublishedDate(feed.getPublishedDate());
                channel = channelRepository.save(channel);
                for (SyndEntry e : feed.getEntries()) {
                    System.out.println("e.getPublishedDate():" + e.getPublishedDate());
                    if (beforeDate == null || e.getPublishedDate()==null|| !beforeDate.after(e.getPublishedDate())) {
                        itemRepository.save(new RssItem(e, channel));
                        System.out.println("added:" + e.getTitle());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("finish-------------");
    }


}

@Configuration
class MvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/reader").setViewName("reader");
        registry.addViewController("/partials/default.html").setViewName("partials/default");
    }
}


//@Component
class Runner implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        try {
            userRepository.save(new User("aa@aa.com", "pass", "lis64i"));
            userRepository.save(new User("aab@aa.com", "pass", "li23si"));
            userRepository.save(new User("aac@aa.com", "pass", "li6si"));
            userRepository.save(new User("ae@aa.com", "pass", "li43si"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


