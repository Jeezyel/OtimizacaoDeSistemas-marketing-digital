
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import DTO.PacoteMarketing;
import DTO.ResultadoOtimizacaoDTO;
import controller.GerenciadorDados;
import controller.OtimizadorMixVendas;

public class App {

    public static void main(String[] args) {
        // 1. Carrega os dados reais do CSV
        String caminho = java.nio.file.Paths.get("dados.csv").toAbsolutePath().toString();
        Map<PacoteMarketing, Integer> dadosReais = GerenciadorDados.lerEAgruparCSV(caminho);

        if (dadosReais.isEmpty()) {
            System.err.println("Nenhum dado carregado.");
            return;
        }

        // 2. Executa a Otimização
        ResultadoOtimizacaoDTO resultado = OtimizadorMixVendas.otimizar(dadosReais, 600.0, 500.0);

        // 3. Prepara as listas para os Gráficos Comparativos
        int totalItens = dadosReais.size();
        List<String> nomesPacotes = new ArrayList<>(totalItens);
        
        // CORREÇÃO AQUI: Mudando para Double para normalizar a assinatura do XChart
        List<Double> qtdReais = new ArrayList<>(totalItens);
        List<Double> qtdOtimizadas = new ArrayList<>(totalItens);
        
        List<Double> lucroRealTotal = new ArrayList<>(totalItens);
        List<Double> lucroOtimizadoTotal = new ArrayList<>(totalItens);

        dadosReais.forEach((pacote, qtdReal) -> {
            nomesPacotes.add(pacote.nome());
            qtdReais.add((double) qtdReal); // Cast para double
            lucroRealTotal.add(pacote.margemUnitaria() * qtdReal);

            int qtdOtimizada = resultado.mixOtimizado().getOrDefault(pacote, qtdReal);
            qtdOtimizadas.add((double) qtdOtimizada); // Cast para double
            lucroOtimizadoTotal.add(pacote.margemUnitaria() * qtdOtimizada);
        });

        // ----------------------------------------------------------------
        // 4. MONTAGEM DOS GRÁFICOS (XChart)
        // ----------------------------------------------------------------
        CategoryChart graficoQtd = new CategoryChartBuilder()
                .width(600).height(400).title("Volume de Vendas: Real vs Otimizado").build();
        graficoQtd.addSeries("Atual (Real)", nomesPacotes, qtdReais);
        graficoQtd.addSeries("Sugerido (ojAlgo)", nomesPacotes, qtdOtimizadas);

        CategoryChart graficoLucro = new CategoryChartBuilder()
                .width(600).height(400).title("Lucro Total: Real vs Otimizado").build();
        graficoLucro.addSeries("Lucro Real", nomesPacotes, lucroRealTotal);
        graficoLucro.addSeries("Lucro Otimizado", nomesPacotes, lucroOtimizadoTotal);

        System.out.println("Otimização concluída com sucesso? " + resultado.sucesso());
        System.out.println("Lucro Máximo Estimado: R$ " + resultado.lucroMaximoTotal());

        // Exibe a matriz de gráficos na tela
        List<Chart<?, ?>> matriz = List.of(graficoQtd, graficoLucro);
        new SwingWrapper<>(matriz).displayChartMatrix();

///////////////////////////////////////////////////////////grafico comun ///////////////////////////////////////
        // 1. Carrega os dados agrupados
        Map<PacoteMarketing, Integer> dados = GerenciadorDados.lerEAgruparCSV(caminho);
        
        // Listas para armazenar os dados processados
        List<String> nomesPacotesCSV = new ArrayList<>();
        List<Double> margensUnitarias = new ArrayList<>();
        List<Double> totalHoras = new ArrayList<>();
        List<Double> lucroMaximoPotencial = new ArrayList<>();
        List<Integer> quantidades = new ArrayList<>();

        // Processa o mapa de dados
        dados.forEach((pacote, qtd) -> {
            nomesPacotesCSV.add(pacote.nome());
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
        graficoOriginal.addSeries("Demanda Real", nomesPacotesCSV, quantidades);

        // ----------------------------------------------------------------
        // SUGERIDO 1: Margem de Lucro por Pacote (CategoryChart)
        // ----------------------------------------------------------------
        CategoryChart graficoMargem = new CategoryChartBuilder()
                .width(600).height(400)
                .title("Gráfico 1: Margem de Lucro Unitária por Pacote")
                .xAxisTitle("Pacote").yAxisTitle("Margem (R$)").build();
        graficoMargem.addSeries("Margem Unitária", nomesPacotesCSV, margensUnitarias);

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
        for (int i = 0; i < nomesPacotesCSV.size(); i++) {
            List<Double> xData = List.of(totalHoras.get(i));
            List<Double> yData = List.of(margensUnitarias.get(i));
            graficoDispersao.addSeries(nomesPacotesCSV.get(i), xData, yData);
        }

        // ----------------------------------------------------------------
        // SUGERIDO 5: Potencial de Faturamento Máximo (PieChart - Pizza/Rosca)
        // ----------------------------------------------------------------
        PieChart graficoPizza = new PieChartBuilder()
                .width(600).height(400)
                .title("Gráfico 5: Participação no Lucro Máximo Potencial").build();

        // Transforma o estilo em gráfico de Rosca (Donut) para ficar mais moderno
        graficoPizza.getStyler().setCircular(true);

        for (int i = 0; i < nomesPacotesCSV.size(); i++) {
            graficoPizza.addSeries(nomesPacotesCSV.get(i), lucroMaximoPotencial.get(i));
        }

        // ----------------------------------------------------------------
        // 5. Abre as janelas na tela mostrando todos os gráficos!
        // ----------------------------------------------------------------
        List<Chart<?, ?>> listaGraficos = List.of(graficoOriginal, graficoMargem, graficoDispersao, graficoPizza);
        new SwingWrapper<>(listaGraficos).displayChartMatrix();
    }
}