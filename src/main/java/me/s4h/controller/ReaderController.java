package me.s4h.controller;

import com.rometools.opml.feed.opml.Opml;
import com.rometools.opml.feed.opml.Outline;
import com.rometools.rome.io.WireFeedInput;
import me.s4h.entity.RssChannel;
import me.s4h.entity.RssItem;
import me.s4h.entity.User;
import me.s4h.repository.RssChannelRepository;
import me.s4h.repository.RssItemRepository;
import me.s4h.repository.UserRepository;
import me.s4h.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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


    @RequestMapping("/user/channels")
    @ResponseBody
    Set<RssChannel> userChannels(@AuthenticationPrincipal User user) {
        return userRepository.findOne(user.getId()).getChannels();
    }

    @RequestMapping("/rssChannel/{channelId}/items")
    @ResponseBody
    Page<RssItem> channelItems(@PathVariable Long channelId, Pageable pageable){
        return itemRepository.findByChannelId(channelId,pageable);
    }



    @RequestMapping(value = "/addChannel", method = RequestMethod.POST)
    String addChannel(@AuthenticationPrincipal User user, String channelUrl) {
        try {
            if (!channelUrl.startsWith("http://")) {
                channelUrl = "http://" + channelUrl;
            }
            readerService.addChannel(user.getId(), channelUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/reader";
    }


    @RequestMapping(value = "/uploadOpml", method = RequestMethod.POST)
    String uploadOpml(MultipartFile opmlFile, @AuthenticationPrincipal User user) {
        try {
            readerService.handleOpml(opmlFile.getInputStream(), user.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/reader";
    }


}
