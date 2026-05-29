package DTO;

import java.util.Map;

public record ResultadoOtimizacaoDTO(
    Map<PacoteMarketing, Integer> mixOtimizado,
    double lucroMaximoTotal,
    boolean sucesso
) {}