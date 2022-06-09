package com.dsousa.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dsousa.minhasfinancas.exceptions.ErroAutenticacao;
import com.dsousa.minhasfinancas.exceptions.RegraNegocioException;
import com.dsousa.minhasfinancas.model.entity.Usuario;
import com.dsousa.minhasfinancas.model.repository.UsuarioRepository;
import com.dsousa.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService{

	private UsuarioRepository usuarioRepository;
	private PasswordEncoder encoder;
	
	@Autowired
	public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.encoder = encoder;
	}
	
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		boolean senhasBatem = encoder.matches(senha, usuario.get().getSenha());
		
		if(!senhasBatem) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		return usuario.get();
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		criptografarSenha(usuario);
		return usuarioRepository.save(usuario);
	}

	private void criptografarSenha(Usuario usuario) {
		String senha = usuario.getSenha();
		String senhaCripto = encoder.encode(senha);
		usuario.setSenha(senhaCripto);
		
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = usuarioRepository.existsByEmail(email);
		
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email");
		}
		
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return usuarioRepository.findById(id);
	}
}