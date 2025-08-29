package br.com.residencia.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.residencia.entities.Morador;

public class PasswordUtils {

	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);	
	
	public PasswordUtils() {
	}

	/**
	 * Gera um hash utilizando o BCrypt.
	 * 
	 * @param senha
	 * @return String
	 */
	public static String gerarBCrypt(String senha) {
		if (senha == null) {
			return senha;
		}

		log.info("Gerando hash com o BCrypt.");
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		return bCryptEncoder.encode(senha);
	}

	
	/**
	 * Verifica se a senha informada é a padrão
	 * 
	 * @param senha senha atual
	 * @param morador morador atual
	 * @return boolean
	 */
	public static Boolean IsPasswordDefault(String senha, Morador morador) {
		
		String senhaPadrao = morador.getCpf().substring(0, 6);
		
		if(senha.equals(senhaPadrao)) {
			return true;
		}else {
			return false;
		}
	}
	
}
