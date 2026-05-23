import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

import DTO.PacoteMarketing;
import controller.GerenciadorDados;

public class App {
    public static void main(String[] args) {
        // 1. Carrega os dados agrupados
        // Usa o java.nio.file.Paths para garantir que o caminho funcione em qualquer
        // sistema operacional
        String caminho = java.nio.file.Paths.get("dados.csv").toAbsolutePath().toString();
        Map<PacoteMarketing, Integer> dados = GerenciadorDados.lerEAgruparCSV(caminho);

        // 2. Prepara as listas para o gráfico
        List<String> nomesPacotes = new ArrayList<>();
        List<Integer> quantidades = new ArrayList<>();

        dados.forEach((pacote, qtd) -> {
            nomesPacotes.add(pacote.nome());
            quantidades.add(qtd);
        });

        // 3. Cria o gráfico de barras com o XChart
        CategoryChart grafico = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Quantidade de Pacotes Vendidos / Demandados")
                .xAxisTitle("Pacote")
                .yAxisTitle("Quantidade")
                .build();

        // 4. Adiciona os dados no gráfico
        grafico.addSeries("Demanda", nomesPacotes, quantidades);

        // 5. Abre uma janela na tela mostrando o gráfico!
        new SwingWrapper<>(grafico).displayChart();
    }
}