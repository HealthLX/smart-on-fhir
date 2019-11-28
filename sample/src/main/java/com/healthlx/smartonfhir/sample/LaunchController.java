package com.healthlx.smartonfhir.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LaunchController {

    @GetMapping("/launch")
    public String main(Model model) {
        return "index";
    }

}
