package com.ribeirodev.luizPDF.rest;

import com.ribeirodev.luizPDF.service.ArquivoConvertido;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ArquivoDownloadResponse {

    public ResponseEntity<byte[]> criar(ArquivoConvertido arquivo) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition(arquivo.nomeArquivo()))
                .contentType(arquivo.mediaType())
                .body(arquivo.conteudo());
    }

    private String contentDisposition(String nomeArquivo) {
        return ContentDisposition.attachment()
                .filename(nomeArquivo)
                .build()
                .toString();
    }
}
