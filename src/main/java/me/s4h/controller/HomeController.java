package me.s4h.controller;

import me.s4h.entity.User;
import me.s4h.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LENOVO on 2014/11/21.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    String home(@AuthenticationPrincipal User loginedUser) {
        //if already login in
        if (loginedUser != null) {
            return "redirect:/reader";
        }
        return "home";
    }


    @RequestMapping(value = "/login",method = RequestMethod.GET)
    String loginPage(@AuthenticationPrincipal User loginedUser) {
        //if already login in
        if (loginedUser != null) {
            return "redirect:/reader";
        }
        return "login";
    }


    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    String signup(@AuthenticationPrincipal User loginedUser,@ModelAttribute User user) {
        //if already login in
        if (loginedUser != null) {
            return "redirect:/reader";
        }
        return "signup";
    }

    @RequestMapping(value = "/signup",method = RequestMethod.POST)
    public String signup(@Valid User user, BindingResult result,
                         RedirectAttributes redirect) {
        if (result.hasErrors()) {
            System.out.println(result);
            return "signup";
        }
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.addError(new ObjectError("user","error creating user"));
            return "signup";
        }
        redirect.addFlashAttribute("globalMessage", "Successfully signed up");

        List<GrantedAuthority> authorities =
                AuthorityUtils.createAuthorityList("ROLE_USER");
        Authentication auth =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return "redirect:/reader";
    }
}