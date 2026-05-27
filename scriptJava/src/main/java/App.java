import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.internal.chartpart.Chart;

import DTO.PacoteMarketing;
import controller.GerenciadorDados;

import org.knowm.xchart.*;

public class App {

    public static void main(String[] args) {
        // 1. Carrega os dados agrupados
        String caminho = java.nio.file.Paths.get("dados.csv").toAbsolutePath().toString();
        Map<PacoteMarketing, Integer> dados = GerenciadorDados.lerEAgruparCSV(caminho);

        // Listas para armazenar os dados processados
        List<String> nomesPacotes = new ArrayList<>();
        List<Double> margensUnitarias = new ArrayList<>();
        List<Double> totalHoras = new ArrayList<>();
        List<Double> lucroMaximoPotencial = new ArrayList<>();
        List<Integer> quantidades = new ArrayList<>();

        // Processa o mapa de dados
        dados.forEach((pacote, qtd) -> {
            nomesPacotes.add(pacote.nome());
            quantidades.add(qtd);

            // Gráfico 1: Margem unitária direto do pacote
            margensUnitarias.add(pacote.margemUnitaria());

            // Gráfico 2: Soma das horas de analista e design
            double horasTotais = pacote.horasAnalista() + pacote.horasDesign();
            totalHoras.add(horasTotais);

            // Gráfico 5: Margem Unitária * Demanda Máxima (ou pela quantidade vendida
            // 'qtd')
            // Aqui multiplicamos a margem pelo teto de demanda informado no CSV
            double lucroPotencial = pacote.margemUnitaria() * pacote.demandaMaxima();
            lucroMaximoPotencial.add(lucroPotencial);
        });

        // ----------------------------------------------------------------
        // GRÁFICO ORIGINAL: Quantidade Demandada (CategoryChart)
        // ----------------------------------------------------------------
        CategoryChart graficoOriginal = new CategoryChartBuilder()
                .width(600).height(400)
                .title("Quantidade de Pacotes Vendidos / Demandados")
                .xAxisTitle("Pacote").yAxisTitle("Quantidade").build();
        graficoOriginal.addSeries("Demanda Real", nomesPacotes, quantidades);

        // ----------------------------------------------------------------
        // SUGERIDO 1: Margem de Lucro por Pacote (CategoryChart)
        // ----------------------------------------------------------------
        CategoryChart graficoMargem = new CategoryChartBuilder()
                .width(600).height(400)
                .title("Gráfico 1: Margem de Lucro Unitária por Pacote")
                .xAxisTitle("Pacote").yAxisTitle("Margem (R$)").build();
        graficoMargem.addSeries("Margem Unitária", nomesPacotes, margensUnitarias);

        // ----------------------------------------------------------------
        // SUGERIDO 2: Margem Unitária vs. Custo de Tempo (XYChart - Dispersão)
        // ----------------------------------------------------------------
        XYChart graficoDispersao = new XYChartBuilder()
                .width(600).height(400)
                .title("Gráfico 2: Eficiência (Margem vs. Horas Totais)")
                .xAxisTitle("Total de Horas (Analista + Design)").yAxisTitle("Margem Unitária (R$)").build();

        // No XChart, o gráfico de dispersão precisa que o estilo de linha seja "None"
        // para mostrar apenas os pontos
        graficoDispersao.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        // Adiciona cada pacote como uma série individual para plotar os pontos
        // correspondentes
        for (int i = 0; i < nomesPacotes.size(); i++) {
            List<Double> xData = List.of(totalHoras.get(i));
            List<Double> yData = List.of(margensUnitarias.get(i));
            graficoDispersao.addSeries(nomesPacotes.get(i), xData, yData);
        }

        // ----------------------------------------------------------------
        // SUGERIDO 5: Potencial de Faturamento Máximo (PieChart - Pizza/Rosca)
        // ----------------------------------------------------------------
        PieChart graficoPizza = new PieChartBuilder()
                .width(600).height(400)
                .title("Gráfico 5: Participação no Lucro Máximo Potencial").build();

        // Transforma o estilo em gráfico de Rosca (Donut) para ficar mais moderno
        graficoPizza.getStyler().setCircular(true);

        for (int i = 0; i < nomesPacotes.size(); i++) {
            graficoPizza.addSeries(nomesPacotes.get(i), lucroMaximoPotencial.get(i));
        }

        // ----------------------------------------------------------------
        // 5. Abre as janelas na tela mostrando todos os gráficos!
        // ----------------------------------------------------------------
        List<Chart<?, ?>> listaGraficos = List.of(graficoOriginal, graficoMargem, graficoDispersao, graficoPizza);
        new SwingWrapper<>(listaGraficos).displayChartMatrix();
    }
}