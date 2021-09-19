package org.itstep.exam.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.itstep.exam.config.StaticConfig;
import org.itstep.exam.entity.Role;
import org.itstep.exam.entity.User;
import org.itstep.exam.model.UserModel;
import org.itstep.exam.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
public class HomeController  {

    @Value("${file.ava.dir}")
    private String avatarPath;

    @Value("${file.defaultava.dir}")
    private String defaultAvaDir;

    @Value("${file.ava.classpath}")
    private String avaClasspath;

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/",name = "index")
    public String index() {
        return "index";
    }

    @GetMapping(value = "login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model) {
        model.addAttribute("userModel", new UserModel());
        return "register";
    }

    @ModelAttribute
    public void getUser(Model model) {
        model.addAttribute("user", getUser());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping(value = "/upload-ava")
    public String uploudAva(@RequestParam(name = "ava_pic") MultipartFile file) {
        User user = getUser();

        if (Objects.nonNull(user)) {
            if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")) {
                String uniqueName = DigestUtils.sha1Hex("user_avatar_" + user.getId());
                String ext = "png";
                if (file.getContentType().equals("image/jpeg")) {
                    ext = "jpg";
                }
                try {
                    String fullPath = avatarPath + uniqueName + "." + ext;
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(fullPath);
                    Files.write(path, bytes);
                    user.setAva(fullPath);
                    user.setAvaHash(uniqueName + "." + ext);
                    userService.updateUser(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        return "profile";
    }

    @SneakyThrows
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/view-photo/{avaHash}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE} )
    public @ResponseBody byte[] viewPhoto(@PathVariable("avaHash") String avaHash){
        String image = defaultAvaDir + "default_ava.jpg";
        if (!avaHash.equals("null")) {
            image = avaClasspath + avaHash;
        }
        InputStream in;
        try {
            ClassPathResource resource = new ClassPathResource(image);
            in = resource.getInputStream();
        } catch (IOException e) {
            image = defaultAvaDir + "default_ava.jpg";
            ClassPathResource resource = new ClassPathResource(image);
            in = resource.getInputStream();
        }
        return IOUtils.toByteArray(in);
    }

    @PostMapping(value = "/register")
    public String registerUser(@ModelAttribute UserModel userModel) {
        if (userModel.getPassword().equals(userModel.getConfirmPassword())) {
            List<Role> roles = new ArrayList<>();
            roles.add(StaticConfig.ROLE_USER);
            User User = new User(userModel.getEmail(), userModel.getPassword(), userModel.getFullName(), null, null, roles);
            userService.registerUser(User);
            return "redirect:/login";
        } else {
            return "redirect:/register?error=1";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping(value = "/profile")
    public String Profile() {
        return "profile";
    }


    @ModelAttribute
    public void addUser(Model model) {
        model.addAttribute("user", getUser());
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User securityUser = (User) authentication.getPrincipal();
            User user = userService.getUser(securityUser.getFullName());
            return user;
        }
        return null;

    }
}
