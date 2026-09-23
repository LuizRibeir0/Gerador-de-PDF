package com.ribeirodev.luizPDF.service;

import org.springframework.http.MediaType;

public record ArquivoConvertido(
        byte[] conteudo,
        String nomeArquivo,
        MediaType mediaType
) {
}
