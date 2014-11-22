package me.s4h.entity;

import com.rometools.rome.feed.synd.SyndFeed;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by LENOVO on 2014/11/21.
 */
@Entity
public class RssChannel {
    @Id
    @GeneratedValue
    Long id;

    @OneToMany(mappedBy = "channel")
    Set<RssItem> items = new HashSet<RssItem>();

    @Column(nullable = false, unique = true)
    String url; //url to feed xml


    @Column(nullable = false, unique = true)
    String link; //url of the site/host,consider it the identity of the RssChannel class

    String uri;
    String title;
    String author;
//    String imgLink;
    String description;

    Date publishedDate;


    public void addRssItem(RssItem item){
        this.items.add(item);
    }

    public RssChannel() {
    }

    public RssChannel(SyndFeed feed,String url) {
        this.uri = feed.getUri();
        this.title = feed.getTitle();
        this.publishedDate = feed.getPublishedDate();
        this.author = feed.getAuthor();
        this.description = feed.getDescription();
//        this.imgLink = feed.getImage().getLink();
        this.link = feed.getLink();

//        feed.getEntries().forEach(e -> this.items.add(new RssItem(e)));

        this.url = url;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<RssItem> getItems() {
        return items;
    }

    public void setItems(Set<RssItem> items) {
        this.items = items;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

//    public String getImgLink() {
//        return imgLink;
//    }
//
//    public void setImgLink(String imgLink) {
//        this.imgLink = imgLink;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
