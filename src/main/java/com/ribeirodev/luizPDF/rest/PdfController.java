package com.ribeirodev.luizPDF.rest;

import com.ribeirodev.luizPDF.service.ArquivoConvertido;
import com.ribeirodev.luizPDF.service.PdfService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PdfService pdfService;
    private final ArquivoDownloadResponse arquivoDownloadResponse;

    public PdfController(PdfService pdfService, ArquivoDownloadResponse arquivoDownloadResponse) {
        this.pdfService = pdfService;
        this.arquivoDownloadResponse = arquivoDownloadResponse;
    }

    @PostMapping("/converter")
    public ResponseEntity<byte[]> converter(@RequestParam("imagem") MultipartFile imagem) throws IOException {
        ArquivoConvertido arquivo = pdfService.converterImagemParaPdf(imagem);
        return arquivoDownloadResponse.criar(arquivo);
    }

    @PostMapping("/converter-lote")
    public ResponseEntity<byte[]> converterLote(@RequestParam("imagens") MultipartFile[] imagens) throws IOException {
        ArquivoConvertido arquivo = pdfService.converterImagensParaZip(imagens);
        return arquivoDownloadResponse.criar(arquivo);
    }
}
