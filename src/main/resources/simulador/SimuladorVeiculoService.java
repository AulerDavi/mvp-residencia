package com.residencia.mvp_residencia.simulator;

import com.residencia.mvp_residencia.model.StatusVeiculo;
import com.residencia.mvp_residencia.model.Veiculo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class SimuladorVeiculoService {

    private static final Logger log = LoggerFactory.getLogger(SimuladorVeiculoService.class);
    private final List<Veiculo> frota = new ArrayList<>();

    public SimuladorVeiculoService() {
        frota.add(new Veiculo("1", "Onibus 101", StatusVeiculo.SAIU_DO_PONTO));
        frota.add(new Veiculo("2", "VLT Azul", StatusVeiculo.A_CAMINHO));
    }

    @Scheduled(fixedRate = 5000) // Roda a cada 5 segundos
    public void simular() {
        for (Veiculo v : frota) {
            StatusVeiculo antigo = v.getStatus();
            v.setStatus(antigo.proximo());
            log.info("Veiculo {}: [{}] -> [{}]", v.getNome(), antigo.getDescricao(), v.getStatus().getDescricao());
        }
    }
}