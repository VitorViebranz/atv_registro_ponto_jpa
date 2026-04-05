package br.univille.poo2.jpa.repository;

import br.univille.poo2.jpa.entity.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public interface RegistroPontoRepository extends JpaRepository<RegistroPonto,Long> {

    List<RegistroPonto> findByFuncionarioMatriculaAndDataHoraBetweenOrderByDataHoraAsc(
            String funcionario_matricula, LocalDateTime dataHora, LocalDateTime dataHora2
    );
}
