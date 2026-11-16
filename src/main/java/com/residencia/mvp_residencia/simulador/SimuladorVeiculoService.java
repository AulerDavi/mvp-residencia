package com.residencia.mvp_residencia.simulador;

import com.residencia.mvp_residencia.model.StatusVeiculo;
import com.residencia.mvp_residencia.model.Veiculos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SimuladorVeiculoService {

    private static final Logger log = LoggerFactory.getLogger(SimuladorVeiculoService.class);
    private final List<Veiculos> frota = new CopyOnWriteArrayList<>();

    public SimuladorVeiculoService() {
        frota.add(new Veiculos("1", "Onibus 101", StatusVeiculo.SAIU_DO_PONTO));
        frota.add(new Veiculos("2", "VLT Azul", StatusVeiculo.A_CAMINHO));
    }

    @Scheduled(fixedRate = 5000) // Roda a cada 5 segundos
    public void simular() {
        for (Veiculos v : frota) {
            StatusVeiculo antigo = v.getStatus();
            v.setStatus(antigo.proximo());
            log.info("Veiculo {}: [{}] -> [{}]", v.getNome(), antigo.getDescricao(), v.getStatus().getDescricao());
        }
    }

    public Optional<Veiculos> buscarPorId(String id) {
        return frota.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }
}
