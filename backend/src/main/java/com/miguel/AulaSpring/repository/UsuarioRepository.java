package com.miguel.AulaSpring.repository;

import com.miguel.AulaSpring.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    @Query(""" 
    SELECT e FROM Usuario e
    WHERE (:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
    AND (:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%')))
    """)
    List<Usuario> filtrar(
            @Param("nome") String nome,
            @Param("email") String email
    );
}