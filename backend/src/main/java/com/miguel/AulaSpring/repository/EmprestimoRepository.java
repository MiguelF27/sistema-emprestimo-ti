package com.miguel.AulaSpring.repository;

import com.miguel.AulaSpring.entity.Emprestimo;
import com.miguel.AulaSpring.entity.enums.StatusEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    @Query("""
    SELECT e FROM Emprestimo e
    WHERE (:nome IS NULL OR LOWER(e.usuario.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
    AND (:status IS NULL OR e.status = :status)
""")
    List<Emprestimo> filtrar(
            @Param("nome") String nome,
            @Param("status") StatusEmprestimo status
    );
}

