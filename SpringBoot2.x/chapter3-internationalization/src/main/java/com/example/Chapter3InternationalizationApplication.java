package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@SpringBootApplication
@Controller
public class Chapter3InternationalizationApplication {
	@Autowired
	private MessageSource messageSource;

	@GetMapping("/")
	public String hello(Model model) {
		Locale locale = LocaleContextHolder.getLocale();
		model.addAttribute("message", messageSource.getMessage("message", null, locale));
		return "index";
	}

	public static void main(String[] args) {
		SpringApplication.run(Chapter3InternationalizationApplication.class, args);
	}

}
