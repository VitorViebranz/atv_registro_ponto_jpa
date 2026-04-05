package br.univille.poo2.jpa;

import br.univille.poo2.jpa.entity.Funcionario;
import br.univille.poo2.jpa.entity.RegistroPonto;
import br.univille.poo2.jpa.repository.FuncionarioRepository;
import br.univille.poo2.jpa.repository.RegistroPontoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class JpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(RegistroPontoRepository registro){
        return args -> {
            String matriculaBusca = "12345";
            LocalDate dataRelatorio = LocalDate.of(2026, 3, 10);

            LocalDateTime inicioDoDia = dataRelatorio.atStartOfDay();
            LocalDateTime fimDoDia = dataRelatorio.atTime(LocalTime.MAX);

            List<RegistroPonto> registros = registro.findByFuncionarioMatriculaAndDataHoraBetweenOrderByDataHoraAsc(
                    matriculaBusca,
                    inicioDoDia,
                    fimDoDia
            );

            if (!registros.isEmpty()) {
                Funcionario f = registros.get(0).getFuncionario();

                System.out.println("RELATÓRIO DE PONTO");
                System.out.println("Funcionário: " + f.getNome());
                System.out.println("Departamento: " + f.getDepartamento().getNome());
                System.out.println("Data: " + dataRelatorio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                System.out.println("\nEntrada\tSaída\tHoras");

                Duration totalTrabalhado = Duration.ZERO;
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                for (int i = 0; i < registros.size(); i += 2) {
                    LocalDateTime entrada = registros.get(i).getDataHora();
                    LocalDateTime saida = null;

                    if (i + 1 < registros.size()) {
                        saida = registros.get(i + 1).getDataHora();
                    }

                    String strEntrada = entrada.format(timeFormatter);
                    String strSaida = (saida != null) ? saida.format(timeFormatter) : "---";
                    String strHoras = "---";

                    if (saida != null) {
                        Duration duracao = Duration.between(entrada, saida);
                        totalTrabalhado = totalTrabalhado.plus(duracao);
                        strHoras = String.format("%02d:%02d", duracao.toHours(), duracao.toMinutesPart());
                    }

                    System.out.println(strEntrada + "\t" + strSaida + "\t" + strHoras);
                }

                System.out.println("\nTotal trabalhado:\t" + String.format("%02d:%02d", totalTrabalhado.toHours(), totalTrabalhado.toMinutesPart()));

            } else {
                System.out.println("Nenhum registro de ponto encontrado para a matrícula " + matriculaBusca + " na data informada.");
            }
        };
    }
}