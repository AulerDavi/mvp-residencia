package com.residencia.mvp_residencia.importador;

import com.residencia.mvp_residencia.model.Horario;
import com.residencia.mvp_residencia.model.Linha;
import com.residencia.mvp_residencia.model.Parada;
import com.residencia.mvp_residencia.model.TipoDia;
import com.residencia.mvp_residencia.model.Veiculo;
import com.residencia.mvp_residencia.repository.HorarioRepository;
import com.residencia.mvp_residencia.repository.LinhaRepository;
import com.residencia.mvp_residencia.repository.VeiculoRepository;
import com.residencia.mvp_residencia.repository.ParadaRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportadorDados implements CommandLineRunner {

    private final LinhaRepository linhaRepository;
    private final ParadaRepository paradaRepository;
    private final VeiculoRepository veiculoRepository;
    private final HorarioRepository horarioRepository;

    @Value("${import.limite:200}")
    private int limite;

    @Override
    @Transactional
    public void run(String... args) throws IOException {
        if (linhaRepository.count() > 0) {
            log.info("Banco já possui dados. Importação ignorada.");
            return;
        }
        Map<Long, Linha> linhas = importarLinhas();
        Map<Long, Parada> paradas = importarParadas(linhas);
        importarVeiculos(linhas);
        importarHorarios(linhas, paradas);
    }

    private Map<Long, Linha> importarLinhas() throws IOException {
        Map<Long, Linha> mapa = new HashMap<>();
        for (String[] c : lerCsv("linha.csv")) {
            Linha linha = new Linha();
            linha.setNome(campo(c, 1));
            linha.setTipo(campo(c, 2));
            linha.setSentido(campo(c, 3));
            linha.setAtiva(Boolean.parseBoolean(campo(c, 4)));
            mapa.put(Long.parseLong(campo(c, 0)), linhaRepository.save(linha));
        }
        log.info("Linhas importadas: {}", mapa.size());
        return mapa;
    }

    private Map<Long, Parada> importarParadas(Map<Long, Linha> linhas) throws IOException {
        Map<Long, Parada> mapa = new HashMap<>();
        int ignoradas = 0;
        for (String[] c : lerCsv("parada.csv")) {
            Linha linha = linhas.get(idOuNulo(campo(c, 3)));
            if (linha == null) {
                ignoradas++;
                continue;
            }
            Parada parada = new Parada();
            parada.setNome(campo(c, 1));
            parada.setTipo(campo(c, 2));
            parada.setLinha(linha);
            mapa.put(Long.parseLong(campo(c, 0)), paradaRepository.save(parada));
        }
        log.info("Paradas importadas: {} (ignoradas: {})", mapa.size(), ignoradas);
        return mapa;
    }

    private void importarVeiculos(Map<Long, Linha> linhas) throws IOException {
        int importados = 0;
        int ignorados = 0;
        for (String[] c : lerCsv("veiculo.csv")) {
            Linha linha = linhas.get(idOuNulo(campo(c, 2)));
            if (linha == null) {
                ignorados++;
                continue;
            }
            Veiculo veiculo = new Veiculo();
            veiculo.setPlaca(campo(c, 1).isBlank() ? null : campo(c, 1));
            veiculo.setLinha(linha);
            veiculoRepository.save(veiculo);
            importados++;
        }
        log.info("Veículos importados: {} (ignorados: {})", importados, ignorados);
    }

    private void importarHorarios(Map<Long, Linha> linhas, Map<Long, Parada> paradas) throws IOException {
        int importados = 0;
        int ignorados = 0;
        for (String[] c : lerCsv("horario.csv")) {
            Linha linha = linhas.get(idOuNulo(campo(c, 4)));
            Parada parada = paradas.get(idOuNulo(campo(c, 5)));
            if (linha == null || parada == null) {
                ignorados++;
                continue;
            }
            Horario horario = new Horario();
            horario.setHoraPartida(converterHora(campo(c, 1)));
            horario.setTipoDia(TipoDia.valueOf(campo(c, 2).toUpperCase()));
            horario.setAdaptadoDeficiente(Boolean.parseBoolean(campo(c, 3)));
            horario.setLinha(linha);
            horario.setParada(parada);
            horarioRepository.save(horario);
            importados++;
        }
        log.info("Horários importados: {} (ignorados: {})", importados, ignorados);
    }

    /** Lê o CSV de src/main/resources/data, pula o cabeçalho e respeita o limite. */
    private List<String[]> lerCsv(String arquivo) throws IOException {
        List<String[]> registros = new ArrayList<>();
        ClassPathResource recurso = new ClassPathResource("data/" + arquivo);
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(recurso.getInputStream(), StandardCharsets.UTF_8))) {
            leitor.readLine(); // cabeçalho
            String texto;
            while ((texto = leitor.readLine()) != null && registros.size() < limite) {
                if (!texto.isBlank()) {
                    registros.add(dividirCsv(texto));
                }
            }
        }
        return registros;
    }

    static String[] dividirCsv(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean entreAspas = false;
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (c == '"') {
                if (entreAspas && i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                    atual.append('"');
                    i++;
                } else {
                    entreAspas = !entreAspas;
                }
            } else if (c == ',' && !entreAspas) {
                campos.add(atual.toString().trim());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        campos.add(atual.toString().trim());
        return campos.toArray(new String[0]);
    }

    static LocalTime converterHora(String texto) {
        String[] partes = texto.trim().split(":");
        int hora = Integer.parseInt(partes[0]) % 24;
        int minuto = Integer.parseInt(partes[1]);
        return LocalTime.of(hora, minuto);
    }

    private static String campo(String[] campos, int posicao) {
        return posicao < campos.length ? campos[posicao] : "";
    }

    private static Long idOuNulo(String texto) {
        return texto.isBlank() ? null : Long.parseLong(texto);
    }
}
