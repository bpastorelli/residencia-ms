package br.com.residencia.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.residencia.entities.Morador;
import br.com.residencia.enums.PerfilEnum;
import br.com.residencia.repositories.MoradorRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
	private Morador user;
	
	@Autowired
	private MoradorRepository userRepository;
	
	private List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        this.user = userRepository.findByEmail(username).get();

        if (user == null)
            throw new UsernameNotFoundException(username);
        
        setAuthorities(user.getPerfil());
        
        return new User(user.getEmail(), user.getSenha(), roles);
    }

    public Morador getUser() {
    	
    	return this.user;
    }
    
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return roles;
	}
	
	public void setAuthorities(PerfilEnum perfil) {
		
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(perfil.toString());
		this.roles.add(authority);

	}
	
}
