package com.ribeirodev.luizPDF.rest;

import com.ribeirodev.luizPDF.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    private static final String PDF_FILENAME = "convertido.pdf";
    private static final String ZIP_FILENAME = "convertidos.zip";

    private final PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostMapping("/converter")
    public ResponseEntity<byte[]> converter(@RequestParam("imagem") MultipartFile imagem) throws IOException {
        byte[] pdf = pdfService.converterImagemParaPdf(imagem);
        return arquivoParaDownload(pdf, PDF_FILENAME, MediaType.APPLICATION_PDF);
    }

    @PostMapping("/converter-lote")
    public ResponseEntity<byte[]> converterLote(@RequestParam("imagens") MultipartFile[] imagens) throws IOException {
        byte[] zip = pdfService.converterImagensParaZip(imagens);
        return arquivoParaDownload(zip, ZIP_FILENAME, MediaType.APPLICATION_OCTET_STREAM);
    }

    private ResponseEntity<byte[]> arquivoParaDownload(byte[] arquivo, String nomeArquivo, MediaType mediaType) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition(nomeArquivo))
                .contentType(mediaType)
                .body(arquivo);
    }

    private String contentDisposition(String nomeArquivo) {
        return "attachment; filename=\"" + nomeArquivo + "\"";
    }
}
