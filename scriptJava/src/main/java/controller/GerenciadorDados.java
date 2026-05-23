package controller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import DTO.PacoteMarketing;

public class GerenciadorDados {

    public static Map<PacoteMarketing, Integer> lerEAgruparCSV(String caminhoArquivo) {
        Map<PacoteMarketing, Integer> mapaPacotes = new HashMap<>();

        // Como seu CSV usa ponto e vírgula (;), configuramos o formato correspondente
        // Configuramos para usar ponto e vírgula E pular a primeira linha (cabeçalho)
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setDelimiter(';')
                .setHeader() // Reconhece que a primeira linha contém os nomes das colunas
                .setSkipHeaderRecord(true) // Diz para o Java ignorar essa linha física ao iterar
                .build();

        try (Reader reader = new FileReader(caminhoArquivo);
                CSVParser parser = new CSVParser(reader, format)) {

            for (CSVRecord record : parser) {
                // Instancia o objeto com os dados da linha atual do CSV
                PacoteMarketing pacote = new PacoteMarketing(
                        record.get(0), // Nome do Pacote
                        Double.parseDouble(record.get(1)), // Margem Unitária
                        Integer.parseInt(record.get(2)), // Horas Analista
                        Integer.parseInt(record.get(3)), // Horas Design
                        Double.parseDouble(record.get(4)), // Orçamento Mídia
                        Integer.parseInt(record.get(5)) // Demanda Máxima
                );

                // Se o pacote já existir no mapa, soma +1 na quantidade. Se não, começa com 1.
                mapaPacotes.put(pacote, mapaPacotes.getOrDefault(pacote, 0) + 1);
            }

        } catch (Exception e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }

        return mapaPacotes;
    }
}
