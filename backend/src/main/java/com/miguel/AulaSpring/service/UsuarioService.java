package com.miguel.AulaSpring.service;

import com.miguel.AulaSpring.dto.UsuarioDTO;
import com.miguel.AulaSpring.entity.Usuario;
import com.miguel.AulaSpring.exception.BusinessException;
import com.miguel.AulaSpring.exception.ResourceNotFoundException;
import com.miguel.AulaSpring.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }
    public Usuario salvar(UsuarioDTO dto){

        String emailPadronizado = dto.getEmail().toLowerCase();

        if (repository.existsByEmail(emailPadronizado)) {
            throw new BusinessException("Já existe um funcionário cadastrado com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(emailPadronizado);
        usuario.setDepartamento(dto.getDepartamento());

        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {

        String emailPadronizado = dto.getEmail().toLowerCase();

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuario.getEmail().equals(emailPadronizado) && repository.existsByEmail(emailPadronizado)) {
            throw new BusinessException("Já existe um funcionário cadastrado com este e-mail.");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(emailPadronizado);
        usuario.setDepartamento(dto.getDepartamento());

        return repository.save(usuario);
    }
    public List<Usuario> Listar(){
        return repository.findAll();
    }
    public Usuario buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario não encontrado"));
    }
    public void deletar(Long id){
        repository.deleteById(id);
    }

    public List<Usuario> filtrar(String nome, String email) {
        return repository.filtrar(nome, email);
    }
}
