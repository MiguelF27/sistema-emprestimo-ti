package com.miguel.AulaSpring.repository;

import com.miguel.AulaSpring.entity.Equipamento;
import com.miguel.AulaSpring.entity.enums.StatusEquipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    @Query("""
    SELECT e FROM Equipamento e
    WHERE (:nome IS NULL OR LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
    AND (:status IS NULL OR e.status = :status)
""")
    List<Equipamento> filtrar(
            @Param("nome") String nome,
            @Param("status") StatusEquipamento status
    );
}
