package com.demo.auth.web.controller;

import com.demo.auth.web.resolver.GetRedirectUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserController {
	@GetMapping("/login")
	public String login(@AuthenticationPrincipal UserDetails user, @GetRedirectUrl String redirectUrl) {
		if (user != null) {
			log.info("redirectUrl: {}", redirectUrl);
			return "redirect:" + redirectUrl;
		}
		return "login";
	}
}
