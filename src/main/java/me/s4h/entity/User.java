package me.s4h.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by LENOVO on 2014/11/21.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<RssChannel> channels = new HashSet<RssChannel>();


    @Column(nullable = false, unique = true)
    @NotEmpty(message = "hey, can't be empty0")
    String email;
    @NotEmpty(message = "hey, can't be empty1")
    String password;
    @NotEmpty(message = "hey, can't be empty2")
    String nickname;

    public void addRssChannel(RssChannel channel) {
        this.channels.add(channel);
    }

    public User() {
    }

    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User(User user) {
        this.id = user.id;
        this.nickname = user.nickname;
        this.email = user.email;
        this.password = user.password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Set<RssChannel> getChannels() {
        return channels;
    }

    public void setChannels(Set<RssChannel> channels) {
        this.channels = channels;
    }
}
