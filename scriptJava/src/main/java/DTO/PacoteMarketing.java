package DTO;

public record PacoteMarketing(
    String nome, 
    double margemUnitaria, 
    int horasAnalista, 
    int horasDesign, 
    double orcamentoMidia, 
    int demandaMaxima
) {}
