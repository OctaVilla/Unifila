package com.unifila.backend.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.unifila.backend.model.ItemPresupuesto;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

@Service
public class PdfGeneratorService {

    public byte[] generarPresupuesto(String nombreCliente, String fechaTexto, List<ItemPresupuesto> items) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font bold = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normal = new Font(Font.HELVETICA, 12);

        Paragraph empresa = new Paragraph("MIP - METALÚRGICA INDUSTRIAL PARAGUAYA\n", bold);
        empresa.setAlignment(Element.ALIGN_LEFT);
        empresa.add(new Paragraph(
                "RUC: 80025896-5\nDirección: Calle última casi Defensores del Chaco\n" +
                        "Tel: (0981) 396 114 / (0986) 123 417\nEmail: mip.paraguay@gmail.com\nInstagram: @mip_py", normal));
        document.add(empresa);

        Paragraph fecha = new Paragraph("\nFernando de la Mora, " + fechaTexto + "\n\n", normal);
        fecha.setAlignment(Element.ALIGN_LEFT);
        document.add(fecha);

        Paragraph saludo = new Paragraph(
                "Señor: " + nombreCliente + "\n\n" +
                        "Nos dirigimos a Uds. con el objeto de comunicarles sobre el sgte presupuesto:\n\n",
                normal
        );
        document.add(saludo);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.5f, 5, 2.5f, 3});

        table.addCell(getCell("Cant.", bold));
        table.addCell(getCell("Concepto", bold));
        table.addCell(getCell("P/U", bold));
        table.addCell(getCell("Subtotal", bold));

        BigDecimal total = BigDecimal.ZERO;
        DecimalFormat df = new DecimalFormat("###,###");

        for (ItemPresupuesto item : items) {
            BigDecimal subtotal = item.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(item.getCantidad())); // cantidad * P/U

            total = total.add(subtotal);

            table.addCell(getCell(String.valueOf(item.getCantidad()), normal));
            table.addCell(getCell(item.getDescripcion(), normal));
            table.addCell(getCell("Gs. " + df.format(item.getPrecioUnitario()), normal));
            table.addCell(getCell("Gs. " + df.format(subtotal), normal));
        }

        document.add(table);

        Paragraph totalP = new Paragraph("\nTotal: Gs. " + df.format(total) + "\n", bold);
        document.add(totalP);

        Paragraph letras = new Paragraph("Son guaraníes " + convertirNumeroALetras(total.longValue()) + "\n\n", normal);
        document.add(letras);

        Paragraph nota1 = new Paragraph("Este presupuesto tiene una validez de 15 días a partir de la fecha.\n", normal);
        Paragraph nota2 = new Paragraph("El producto cuenta con dos años de garantía.\n\n", normal);
        document.add(nota1);
        document.add(nota2);

        Paragraph firma = new Paragraph("UNIFILA PY", bold);
        firma.setAlignment(Element.ALIGN_CENTER);
        document.add(firma);

        document.close();
        return out.toByteArray();
    }

    private PdfPCell getCell(String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setPadding(5);
        return cell;
    }

    // Conversión simple a letras (enteros)
    private String convertirNumeroALetras(long numero) {
        if (numero == 0) return "cero";

        String[] unidades = {
                "", "uno", "dos", "tres", "cuatro", "cinco",
                "seis", "siete", "ocho", "nueve", "diez",
                "once", "doce", "trece", "catorce", "quince",
                "dieciséis", "diecisiete", "dieciocho", "diecinueve", "veinte"
        };

        String[] decenas = {
                "", "", "veinte", "treinta", "cuarenta", "cincuenta",
                "sesenta", "setenta", "ochenta", "noventa"
        };

        String[] centenas = {
                "", "ciento", "doscientos", "trescientos", "cuatrocientos",
                "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"
        };

        StringBuilder resultado = new StringBuilder();

        if (numero >= 1_000_000) {
            long millones = numero / 1_000_000;
            resultado.append(millones == 1 ? "un millón" : convertirNumeroALetras(millones) + " millones");
            numero %= 1_000_000;
            if (numero > 0) resultado.append(" ");
        }

        if (numero >= 1000) {
            long miles = numero / 1000;
            resultado.append(miles == 1 ? "mil" : convertirNumeroALetras(miles) + " mil");
            numero %= 1000;
            if (numero > 0) resultado.append(" ");
        }

        if (numero >= 100) {
            if (numero == 100) {
                resultado.append("cien");
            } else {
                resultado.append(centenas[(int) (numero / 100)]);
            }
            numero %= 100;
            if (numero > 0) resultado.append(" ");
        }

        if (numero > 20) {
            int d = (int) (numero / 10);
            int u = (int) (numero % 10);
            resultado.append(decenas[d]);
            if (u > 0) resultado.append(" y ").append(unidades[u]);
        } else if (numero > 0) {
            resultado.append(unidades[(int) numero]);
        }

        // apócope “un” delante de sustantivo (guaraníes)
        String out = resultado.toString().trim();
        if (out.endsWith(" uno")) out = out.substring(0, out.length() - 4) + " un";
        if (out.equals("uno")) out = "un";
        return out;
    }
}
