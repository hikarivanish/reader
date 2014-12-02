package me.s4h.controller;

import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.io.WireFeedInput;
import me.s4h.entity.RssChannel;
import me.s4h.entity.User;
import me.s4h.repository.RssChannelRepository;
import me.s4h.repository.RssItemRepository;
import me.s4h.repository.UserRepository;
import me.s4h.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;

import java.util.List;
import java.util.Set;


/**
 * Created by LENOVO on 2014/11/21.
 */

@Controller
@RequestMapping("/")
public class ReaderController {
    @Autowired
    RssChannelRepository channelRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RssItemRepository itemRepository;
    @Autowired
    ReaderService readerService;




    @RequestMapping("/user")
    @ResponseBody
    Set<RssChannel> user(@AuthenticationPrincipal User user){
        return userRepository.findOne(user.getId()).getChannels();
    }


    @RequestMapping(value = "/addChannel", method = RequestMethod.POST)
    String addChannel(@AuthenticationPrincipal User user, String channelUrl) {
        try {
            readerService.addChannel(user.getId(), "http://" + channelUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/reader";
    }


    @RequestMapping(value = "/uploadOpml", method = RequestMethod.POST)
    @ResponseBody
    String uploadOpml(MultipartFile opmlFile, @AuthenticationPrincipal User user) {
        try {
            WireFeedInput input = new WireFeedInput();
            Opml feed = (Opml) input.build(new InputSource(opmlFile.getInputStream()));
            List<Outline> outlines =  feed.getOutlines();

            outlines.forEach(o -> {
                o.getChildren().forEach(c -> {
                    try {
                        readerService.addChannel(user.getId(), c.getXmlUrl());
                    } catch (Exception e) {
                        System.out.println("fail to add " + c.getXmlUrl());
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/reader";
    }





}
