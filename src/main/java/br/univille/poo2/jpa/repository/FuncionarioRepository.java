package br.univille.poo2.jpa.repository;

import br.univille.poo2.jpa.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FuncionarioRepository extends JpaRepository<Funcionario,Long> {

    Optional<Funcionario> findByMatricula(String matricula);
}
