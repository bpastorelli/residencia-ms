package br.com.residencia.security.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.com.residencia.entities.Morador;
import br.com.residencia.errorheadling.ErroRegistro;
import br.com.residencia.errorheadling.RegistroException;
import br.com.residencia.repositories.MoradorRepository;
import br.com.residencia.security.constants.SecurityConstants;
import br.com.residencia.security.dto.AlterarSenhaDto;
import br.com.residencia.security.dto.JwtAuthenticationDto;
import br.com.residencia.security.dto.TokenDto;
import br.com.residencia.security.util.JwtTokenUtil;
import br.com.residencia.security.util.PasswordUtils;
import br.com.residencia.validators.Validators;

@Service
public class AuthenticationService {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private MoradorRepository moradorRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private Validators<JwtAuthenticationDto, AlterarSenhaDto> validar;
	
	private static final String TITULO = "Autenticação"; 

	
	public TokenDto atenticar(JwtAuthenticationDto authenticationDto) throws RegistroException, IOException {
		
		String token = null;
		
		this.validar.validarPost(authenticationDto);
    	
		try {
	        Authentication auth = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                   	authenticationDto.getEmail(),
	                      authenticationDto.getSenha(),
	                      new ArrayList<>()));
	          SecurityContextHolder.getContext().setAuthentication(auth);
	              
	          UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
	          token = jwtTokenUtil.obterToken(userDetails, auth);
		}catch(AuthenticationException ex) {
			RegistroException errors = new RegistroException();
			errors.getErros().add(new ErroRegistro("",TITULO, " Senha inválida!"));
			throw errors;
		}
             
     	Optional<Morador> user = this.moradorRepository.findByEmail(authenticationDto.getEmail());
             
        TokenDto dto = TokenDto.builder()
            		.id(user.get().getId())
            		.nome(user.get().getNome())
            		.primeiroAcesso(PasswordUtils.IsPasswordDefault(authenticationDto.getSenha(), user.get()))
            		.token(token)
            		.build();
                    
        return dto;
		
	}
	
	public TokenDto refreshToken(String email, String token) throws RegistroException {
		
		RegistroException errors = new RegistroException();
		
		//Optional<String> token = Optional.ofNullable(request.getHeader(SecurityConstants.HEADER_STRING));
		Optional<String> tokenOpt = Optional.ofNullable(token);
		//UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		
		if (tokenOpt.isPresent() && tokenOpt.get().startsWith(SecurityConstants.TOKEN_PREFIX)) {
			tokenOpt = Optional.of(tokenOpt.get().substring(7));
        }
		
		if (!tokenOpt.isPresent()) {
			errors.getErros().add(new ErroRegistro("", TITULO, " Token não informado."));
		} else if (!jwtTokenUtil.tokenValido(tokenOpt.get())) {
			errors.getErros().add(new ErroRegistro("", TITULO, " Token inválido ou expirado"));
		}
		
		if(!errors.getErros().isEmpty())
			throw errors;
		
		TokenDto tokenDto = TokenDto.builder()
				.token(tokenOpt.get())
				.build();		
		
		return tokenDto;
		
	}
	
}
