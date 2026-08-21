package com.ribeirodev.luizPDF.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class PdfService {

    public byte[] converterImagemParaPdf(MultipartFile imagem) throws IOException {
        validarImagem(imagem);

        try (PDDocument documento = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);

            PDImageXObject imagemPdf = PDImageXObject.createFromByteArray(
                    documento,
                    imagem.getBytes(),
                    imagem.getOriginalFilename()
            );

            try (PDPageContentStream contentStream =
                         new PDPageContentStream(documento, pagina)) {
                contentStream.drawImage(
                        imagemPdf,
                        0,
                        0,
                        PDRectangle.A4.getWidth(),
                        PDRectangle.A4.getHeight()
                );
            }
            documento.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] converterImagensParaZip(MultipartFile[] imagens) throws IOException {
        validarImagens(imagens);

        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();

        try (ZipOutputStream zip = new ZipOutputStream(zipOutputStream)) {
            Set<String> nomesUsados = new HashSet<>();

            for (MultipartFile imagem : imagens) {

                byte[] pdf = converterImagemParaPdf(imagem);

                String nomePdf = gerarNomePdf(imagem, nomesUsados);

                ZipEntry zipEntry = new ZipEntry(nomePdf);
                zip.putNextEntry(zipEntry);

                zip.write(pdf);

                zip.closeEntry();
            }
        }
        return zipOutputStream.toByteArray();
    }

    private void validarImagens(MultipartFile[] imagens) {
        if (imagens == null || imagens.length == 0) {
            throw new IllegalArgumentException("Envie pelo menos uma imagem.");
        }

        for (MultipartFile imagem : imagens) {
            validarImagem(imagem);
        }
    }

    private void validarImagem(MultipartFile imagem) {
        if (imagem == null || imagem.isEmpty()) {
            throw new IllegalArgumentException("Envie uma imagem valida.");
        }
    }

    private String gerarNomePdf(MultipartFile imagem, Set<String> nomesUsados) {
        String nomeOriginal = imagem.getOriginalFilename();
        String nomeBase = nomeOriginal == null || nomeOriginal.isBlank()
                ? "imagem"
                : nomeOriginal.replaceFirst("[.][^.]+$", "");

        String nomePdf = nomeBase + ".pdf";
        int sufixo = 2;

        while (!nomesUsados.add(nomePdf)) {
            nomePdf = nomeBase + "-" + sufixo + ".pdf";
            sufixo++;
        }

        return nomePdf;
    }
}
