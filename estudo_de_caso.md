# Estudo de Caso - Grupo 09

**Tema:** Portfólio de serviços de uma agência de marketing digital

## Contextualização

A agência Clique Norte atende pequenas empresas que contratam pacotes mensais de marketing digital, como gestão de redes sociais, tráfego pago, lançamentos e produção de conteúdo institucional. Nos últimos meses, a procura pelos serviços cresceu, mas a agência não conseguiu expandir a equipe na mesma velocidade.

Como consequência, a direção percebeu que nem todo contrato potencialmente vendável deve ser aceito. Alguns pacotes exigem muitas horas de analistas e designers; outros também consomem parcelas relevantes do orçamento de mídia patrocinada que a agência precisa antecipar para operar. O desafio gerencial é montar uma carteira de clientes rentável sem comprometer a capacidade de entrega do mês.

O estudo busca apoiar essa decisão de composição de portfólio. Em vez de escolher contratos apenas pelo valor bruto de venda, a agência quer avaliar quais combinações de pacotes geram melhor margem total dentro das restrições operacionais.

## Sobre o Negócio

O objetivo é escolher quantos pacotes de cada tipo serão comercializados, respeitando a capacidade da equipe e o orçamento, para maximizar a margem mensal. Cada pacote demanda quantidades diferentes de horas de analistas, horas de design e orçamento de mídia. A demanda máxima representa o limite comercial estimado de pacotes que o mercado absorveria no mês.

O grupo deverá modelar o problema como um planejamento de capacidade em serviços. É importante identificar:
- quais são as variáveis de decisão ligadas ao número de contratos por pacote;
- como representar os recursos compartilhados da agência;
- como a limitação de orçamento influencia a escolha do portfólio;
- se a natureza do problema exige variáveis inteiras.

Uma boa leitura gerencial deve discutir quais pacotes “consomem demais” os recursos críticos e quais apresentam melhor retorno relativo. O relatório pode, por exemplo, interpretar se o gargalo está na equipe criativa, nos analistas ou no orçamento de mídia, e quais implicações isso teria para a estratégia comercial.

Assuma que:
- todo pacote contratado gera a margem unitária informada;
- não haverá contratação extra de equipe no horizonte analisado;
- os limites de demanda são superiores e não precisam ser integralmente atendidos.

Considere:
- horas mensais de analistas: `680`;
- horas mensais de design: `420`;
- orçamento mensal de mídia patrocinada: `98000`;
- todas as decisões devem ser inteiras e não negativas.

## Regras Adicionais de Negócio

Para padronizar o nível de análise entre os grupos, incorpore também as seguintes regras na modelagem:
- a agência deve fechar pelo menos `4` contratos de `Social Basico`, para manter presença entre clientes de entrada;
- a quantidade de `Lancamento Digital` não pode ser superior à quantidade de `Trafego Local`;
- os pacotes com mídia mais intensa, `Trafego Local` e `Lancamento Digital`, devem representar pelo menos `20%` do total de contratos do mês.

## Dados Disponíveis

Arquivo: `dados.csv`

O arquivo CSV foi disponibilizado em granularidade elementar. Linhas repetidas representam unidades, lotes, oportunidades ou parcelas equivalentes da mesma categoria. O grupo pode consolidar registros iguais para obter coeficientes agregados antes de montar o modelo em Java, se considerar essa abordagem mais adequada.

Colunas:
- `pacote`
- `margem_unitaria`
- `horas_analista`
- `horas_design`
- `orcamento_midia`
- `demanda_maxima`

## O que produzir

Produzir um relatório em PDF com a modelagem, a solução em Java com `ojAlgo` e a análise dos resultados.

Entregar também o código Java com leitura do CSV e montagem do modelo.

### Estrutura do Relatório de Trabalho

0. identificação do grupo
1. introdução
2. modelagem do problema
3. aplicação da otimização em Java
4. análise dos resultados
5. dificuldades encontradas
6. conclusão
7. referências

### Entrega

**Grupos da A2:** 2 a 3 alunos

| Critério | Pontuação |
|----------|-----------|
| Grupo de 2 a 3 alunos | 1 ponto |
| Formato ZIP | 1 ponto |
| Todos os arquivos exigidos | 1 ponto |
| Relatório de Trabalho | 2 pontos |
| Scripts Java (ojAlgo) | 3 pontos |

