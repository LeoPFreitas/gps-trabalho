package com.example.gpstrabalho.dto;

import lombok.Data;

@Data
public class ImovelDto {
    private String titulo;
    private String url;
    private String descricao;
    private String estado;
    private String cidade;
    private String telefone;
    private Integer applicationUserId;
}
