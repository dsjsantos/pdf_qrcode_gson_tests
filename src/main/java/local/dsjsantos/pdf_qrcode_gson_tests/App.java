package local.dsjsantos.pdf_qrcode_gson_tests;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import local.dsjsantos.pdf_qrcode_gson_tests.tools.HTMLToPDF;
import local.dsjsantos.pdf_qrcode_gson_tests.tools.QRCodeBuilder;
import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SuppressWarnings("unused")
public class App {
    static String jsonString = "{ "+
            "\"client\":\"127.0.0.1\"," +
            "\"servers\":[" +
            "    \"8.8.8.8\"," +
            "    \"8.8.4.4\"," +
            "    \"156.154.70.1\"," +
            "    \"156.154.71.1\" " +
            "    ]" +
            "}";

    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<>(c.size());
        for(Object o: c) {
            r.add(clazz.cast(o));
        }
        return r;
    }

    public static <T> ArrayList<T> castArrayList(Class<? extends T> clazz, Collection<?> c) {
        ArrayList<T> r = new ArrayList<>(c.size());
        for(Object o: c) {
            r.add(clazz.cast(o));
        }
        return r;
    }

    public byte[] getFileContent(URL urlInputFile) throws IOException {
        try {
            return Files.readAllBytes(Paths.get(urlInputFile.toURI()));
        } catch(URISyntaxException e) {
            return null;
        }
    }

    public static void generatePDFfromHTMLFile(String urlInputHTML, String outputPDFPath) {
        try {
            System.out.println("URL: " + urlInputHTML);
            System.out.println("Output: " + outputPDFPath);

            OutputStream out = new FileOutputStream(outputPDFPath);

            // Flying Saucer PDF Render
            final ITextRenderer renderer = new ITextRenderer();

            renderer.setDocument(urlInputHTML);
            renderer.layout();
            renderer.createPDF(out);

            out.close();
        } catch(Exception e) {
            System.out.println("\n[ERROR] Failed to generate PDF");
            e.printStackTrace();
        }
    }


    public static byte[] generatePDFfromHTMLContent(final String _html) {
        try {
            final ITextRenderer renderer = new ITextRenderer();

            renderer.setDocumentFromString(_html);
            renderer.layout();

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            renderer.createPDF(baos);

            return baos.toByteArray();
        } catch(Exception e) {
            System.out.println("\n[ERROR] Failed to generate PDF (2)");
            e.printStackTrace();
            return null;
        }


    }

    public static void main( String[] args ) {

        try {
            // Arguments
            int i = 0;
            for(String arg : args) {
                i++;
                System.out.println("Argumento " + i + ": " + arg);
            }
            System.out.println(">>> Fim argumentos\n");

            // Sample 1
            Gson gson = new Gson();
            System.out.println(gson.toJson("Hello World!"));
            System.out.println(">>> Fim teste 1\n");


            // Sample 2
            JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("servers");

            String[] arrName = new Gson().fromJson(jsonArray, String[].class);
            List<String> lstName;
            lstName = Arrays.asList(arrName);

            for (String str : lstName) {
                System.out.println(str);
            }
            System.out.println(">>> Fim teste 2\n");


            // Sample 3
            JsonParser jsonParser = new JsonParser();
            JsonObject jo = (JsonObject)jsonParser.parse(jsonString);
            JsonArray jsonArr = jo.getAsJsonArray("servers");

            Gson googleJson = new Gson();
            ArrayList<Object> jsonObjList = castArrayList(Object.class, googleJson.fromJson(jsonArr, ArrayList.class));
            System.out.println("List size is : " + jsonObjList.size());
            System.out.println("List Elements are  : " + jsonObjList.toString());
            System.out.println(">>> Fim teste 3\n");

            // HTML to PDF Sample 1.A
            System.out.println(">>> PDF Test 1.A (HTML File -> PDF File");

            URL htmlFile = App.class.getClassLoader().getResource("testeHTML_1.html");
            URL outputFile = new File("/home/djsantos/Temp/testePDF_1.A.pdf").toURI().toURL();
            HTMLToPDF htmlToPDF = new HTMLToPDF(htmlFile);
            if(htmlToPDF.save(outputFile)) {
                System.out.println("Done!\n");
            } else {
                System.out.println("Nada para salvar!\n");
            }

            // HTML to PDF Sample 1.B (Usando InputStream para ler os resources (usado quando empacotado (JAW/EAR/WAR))
            System.out.println(">>> PDF Test 1.B (HTML File -> PDF File");

            InputStream isLogo = App.class.getClassLoader().getResourceAsStream("fruits.jpeg");
            InputStream isHTML = App.class.getClassLoader().getResourceAsStream("testeHTML_1.html");
            InputStream isCSS = App.class.getClassLoader().getResourceAsStream("testeHTML_1.css");

            if(isLogo==null || isHTML==null || isCSS==null)
                throw new Exception("One or more resource files is missing");

            byte[] bytesLogo = IOUtils.toByteArray(isLogo);
            String logoB64 = new String(Base64.getEncoder().encode(bytesLogo));

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(isHTML));
            String htmlContent = reader.lines().collect(Collectors.joining());
            reader = new BufferedReader(new InputStreamReader(isCSS));
            String cssContent = reader.lines().collect(Collectors.joining());

            String imgTag = "<img src='data:image/jpeg;base64," + logoB64 + "' />";
            htmlContent = htmlContent
                    .replaceFirst("<link href=\"testeHTML_1.css\" rel=\"stylesheet\" />", "")
                    .replaceFirst("<style></style>", "<style>\n" + cssContent + "\n</style>")
                    .replaceFirst("\\{EXTRA_CODE}", imgTag);

            htmlToPDF = new HTMLToPDF(htmlContent);

            outputFile = new File("/home/djsantos/Temp/testePDF_1.B.pdf").toURI().toURL();
            if(htmlToPDF.save(outputFile)) {
                System.out.println("Done!\n");
            } else {
                System.out.println("Nada para salvar!\n");
            }


            QRCodeBuilder qrcodeBuilder;
            int size;


            // HTML to PDF Sample 2 - Integrating with generated QRCode Image
            System.out.println(">>> PDF Test 2 (HTML Content -> Memory Buffer -> PDF File");
            size = 150;
            qrcodeBuilder = new QRCodeBuilder("https://www.teste.com.br/processa/xHy96FZx0", size);
            String pngBase64 = qrcodeBuilder.getQrCodeBase64();

            String css = "";
            css += "		div.imgTeste1 {\n";
            css += "			background-image: url('data:image/png;base64," + pngBase64 + "');\n";
            css += "			width: " + size + "px;\n";
            css += "			height: " + size + "px;\n";
            css += "		}\n";

            size = 300;
            qrcodeBuilder.setQrCodeText("http://localhost:9001/processa/xHy96FZx0");
            qrcodeBuilder.setSize(size);
            qrcodeBuilder.setImageType("png");
            qrcodeBuilder.setMargin(0);  // Default margin is 4
            qrcodeBuilder.update();
            pngBase64 = qrcodeBuilder.getQrCodeBase64();

            css += "		div.imgTeste2 {\n";
            css += "			background-image: url('data:image/png;base64," + pngBase64 + "');\n";
            css += "			width: " + size + "px;\n";
            css += "			height: " + size + "px;\n";
            css += "		}\n";

            String html = "";
            html += "<html>\n";
            html += "<head>\n";
            html += "	<title>Teste de HTML</title>\n";
            html += "	<style>\n";
            html += css;
            html += "	</style>\n";
            html += "</head>\n";
            html += "	<body>\n";
            html += "		<br/>\n";
            html += "		Exemplo 1 (150x150 com margem padrão)<br/>\n";
            html += "		<div class='imgTeste1'></div>\n";
            html += "		<br/>\n";
            html += "		<br/>\n";
            html += "		<br/>\n";
            html += "		Exemplo 2 (300x300 com margem zerada)<br/>\n";
            html += "		<div class='imgTeste2'></div>\n";
            html += "	</body>\n";
            html += "</html>\n";

            outputFile = new File("/home/djsantos/Temp/testePDF_2.pdf").toURI().toURL();
            htmlToPDF.setHtmlContent(html);
            htmlToPDF.update();
            if(htmlToPDF.save(outputFile)) {
                System.out.println("Done!\n");
            } else {
                System.out.println("Nada para salvar!\n");
            }


            // QRCode Sample
            System.out.println(">>> QR Code Sample 1 - Create a QRCode PNG File");
            String qrCodeText = "https://www.google.com";
            size = 150;

            qrcodeBuilder = new QRCodeBuilder(qrCodeText);
            qrcodeBuilder.save(new File("/home/djsantos/Temp/QRCode_100x100.png").toURI().toURL());

            qrcodeBuilder = new QRCodeBuilder(qrCodeText, size, 3, "png");
            qrcodeBuilder.save(new File("/home/djsantos/Temp/QRCode_" + size + "x" + size + ".png").toURI().toURL());

            System.out.println("Done!\n");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
