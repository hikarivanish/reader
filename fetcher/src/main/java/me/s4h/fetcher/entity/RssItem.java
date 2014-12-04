package me.s4h.fetcher.entity;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

/**
 * Created by LENOVO on 2014/11/22.
 */
@Entity
public class RssItem {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    RssChannel channel;

    String author;

    @Type(type = "text")
    String description;

    @Type(type = "text")
    String content;

    String link;

    Date publishedDate;

    String title;

    Date updatedDate;

    String uri;

    public RssItem(SyndEntry entry, RssChannel channel) {
        this.author = entry.getAuthor();
        if (entry.getDescription() != null) {
            this.description = entry.getDescription().getValue();
        }
        this.link = entry.getLink();
        this.publishedDate = entry.getPublishedDate();
        this.title = entry.getTitle();
        this.updatedDate = entry.getUpdatedDate();
        this.uri = entry.getUri();
        List<SyndContent> contents = entry.getContents();
        if (contents != null && contents.size() > 0) {
            this.content = contents.get(0).getValue();
        }
        this.channel = channel;
    }

    public RssItem() {
    }

    public RssChannel getChannel() {
        return channel;
    }

    public void setChannel(RssChannel channel) {
        this.channel = channel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
