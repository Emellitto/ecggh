package com.ecggh.userappspring;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class MainController
{

    // Dependency Injection in action
    @Autowired
    public CredentialRepository credentialRepository;

    @Autowired
    public UserinfoRepository userinfoRepository;

    @GetMapping("/test1")
    public String test1handler()
    {
        return "testview1";
    }

    @GetMapping("/test2")
    public String test2handler()
    {
        return "testview2";
    }

    @PostMapping("/login")
    public String loginHandler(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session)
    {

        Optional<Credential> userCredential =  credentialRepository.findById(username);

        if(userCredential.isPresent())
        {
            Credential tempCredential = userCredential.get();
            if(tempCredential.getPassword().equals(password))
            {
                // user authenticated
                model.addAttribute("username",username);
                //model.addAttribute("password",password);
                session.setAttribute("username", username);
                return "dashboard";
            }
            else
            {
                model.addAttribute("errMsg","Incorrect password");
                return "landingpage";
            }
        }
        else
        {
            model.addAttribute("errMsg","Invalid username");
            return "landingpage";
        }


    }

    @PostMapping("/signup")
    public String signupHandler(@RequestParam("username") String username, @RequestParam("password") String password, Model model)
    {
        Credential newCredential = new Credential();
        newCredential.setId(username);
        newCredential.setPassword(password);

        credentialRepository.save(newCredential);

        model.addAttribute("errMsg","New Signup Completed");

        return "landingpage";
    }

    @PostMapping("/updateprofile")
    public String updateProfileHandler(@RequestParam("fullname") String fullname, @RequestParam("phonenumber") String phonenumber, @RequestParam("type") String type, HttpSession session )
    {
        String username = (String) session.getAttribute("username");

        Userinfo newUserInfo = new Userinfo();
        newUserInfo.setId(username);
        newUserInfo.setFullname(fullname);
        newUserInfo.setPhonenumber(phonenumber);
        newUserInfo.setType(type);

        userinfoRepository.save(newUserInfo);

        return "dashboard";
    }




}