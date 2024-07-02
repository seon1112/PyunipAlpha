package com.example.demo.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDto;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@Setter
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
	@Autowired
	private UserRepository usersRept;
	private final HttpServletRequest request;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user=null;
		UserDto m = usersRept.findUsersByEmail(username);
		
		if(m == null) {
			request.getSession().setAttribute("id", username);
			throw new UsernameNotFoundException(username);		
		}else {
			user = User.builder().username(username).roles(m.getROLE()).build();
			
			request.getSession().setAttribute("m", m);
		}
		
		return user;
	}
	


}
