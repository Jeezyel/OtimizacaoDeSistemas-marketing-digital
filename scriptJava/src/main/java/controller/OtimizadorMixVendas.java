package controller;

import java.util.HashMap;
import java.util.Map;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;
import DTO.PacoteMarketing;
import DTO.ResultadoOtimizacaoDTO;

public class OtimizadorMixVendas {

    public static ResultadoOtimizacaoDTO otimizar(
            Map<PacoteMarketing, Integer> dadosReais, 
            double limiteHorasAnalista, 
            double limiteHorasDesign) {
        
        ExpressionsBasedModel modelo = new ExpressionsBasedModel();
        Map<PacoteMarketing, Variable> variaveisMap = new HashMap<>();
        
        // 1. Criar as Variáveis de Decisão usando um loop tradicional seguro
        for (Map.Entry<PacoteMarketing, Integer> entry : dadosReais.entrySet()) {
            PacoteMarketing pacote = entry.getKey();
            Integer qtdReal = entry.getValue();
            
            String nomeVariavel = "Vendas_" + pacote.nome().replaceAll("\\s+", "_");
            double tetoDemandaMercado = qtdReal * 1.5;

            Variable var = modelo.newVariable(nomeVariavel)
                    .lower(0)
                    .upper(tetoDemandaMercado)
                    .integer(true); 
            
            variaveisMap.put(pacote, var);
        }

        // 2. FUNÇÃO OBJETIVO: Maximizar Lucro
        Expression funcaoObjetivo = modelo.newExpression("Lucro_Total").weight(1.0);
        for (Map.Entry<PacoteMarketing, Variable> entry : variaveisMap.entrySet()) {
            funcaoObjetivo.set(entry.getValue(), entry.getKey().margemUnitaria());
        }

        // 3. RESTRIÇÃO 1: Horas de Analista
        Expression restricaoAnalista = modelo.newExpression("Limite_Horas_Analista").upper(limiteHorasAnalista);
        for (Map.Entry<PacoteMarketing, Variable> entry : variaveisMap.entrySet()) {
            restricaoAnalista.set(entry.getValue(), entry.getKey().horasAnalista());
        }

        // 4. RESTRIÇÃO 2: Horas de Design
        Expression restricaoDesign = modelo.newExpression("Limite_Horas_Design").upper(limiteHorasDesign);
        for (Map.Entry<PacoteMarketing, Variable> entry : variaveisMap.entrySet()) {
            restricaoDesign.set(entry.getValue(), entry.getKey().horasDesign());
        }

        // 5. Executar o Solver
        Optimisation.Result resultado = modelo.maximise();

        Map<PacoteMarketing, Integer> mixOtimizado = new HashMap<>();
        
        // 6. Processar o resultado de forma totalmente compatível com qualquer versão
        if (resultado.getState().isFeasible()) {
            for (Map.Entry<PacoteMarketing, Variable> entry : variaveisMap.entrySet()) {
                PacoteMarketing pacote = entry.getKey();
                Variable var = entry.getValue();
                
                // SOLUÇÃO DEFINITIVA: Pegamos o valor diretamente de dentro da própria variável do modelo
                double valorCalculado = var.getValue().doubleValue();
                
                int qtdSugerida = (int) Math.round(valorCalculado);
                mixOtimizado.put(pacote, qtdSugerida);
            }
            
            return new ResultadoOtimizacaoDTO(mixOtimizado, resultado.getValue(), true);
        } else {
            System.err.println("O solver falhou com o estado: " + resultado.getState());
            return new ResultadoOtimizacaoDTO(dadosReais, 0.0, false);
        }
    }
}