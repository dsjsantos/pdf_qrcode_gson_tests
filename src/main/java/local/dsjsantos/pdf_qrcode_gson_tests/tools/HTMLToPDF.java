package local.dsjsantos.pdf_qrcode_gson_tests.tools;

import com.lowagie.text.DocumentException;
import org.apache.commons.configuration.ConversionException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

@SuppressWarnings("unused")
public class HTMLToPDF {
	
	private URL urlInputHTML = null;
	private String htmlContent = null;
	private byte[] pdfContent = null;

	public HTMLToPDF() throws ConversionException {
		this.update();
	}
	
	public HTMLToPDF(String htmlContent) throws ConversionException {
		setHtmlContent(htmlContent);
		this.update();
	}

	public HTMLToPDF(URL urlInputHTML) throws ConversionException {
		setUrlInputHTML(urlInputHTML);
		this.update();
	}

	public boolean save(URL urlFile) throws IOException, SecurityException {
        if(this.pdfContent != null) {
        	try {
    	        OutputStream os = new FileOutputStream(Paths.get(urlFile.toURI()).toString());
    	        os.write(this.pdfContent);
    	        os.close();
    	        return true;
        	} catch(URISyntaxException e) {
        		return false;
        	}
        }
        return false;
	}
	
	public void update() throws ConversionException {
		if(this.urlInputHTML != null) {
			this.pdfContent = convertFromFile(this.urlInputHTML);
		} else if(this.htmlContent != null) {
			this.pdfContent = convertFromContent(this.htmlContent);
		} else {
			this.pdfContent = null;
		}
	}
	
    private void reset() {
    	this.urlInputHTML = null;
    	this.htmlContent = null;
    	this.pdfContent = null;
    }
    
	private byte[] convertFromContent(String inputContent) throws ConversionException {
	    try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();

	        renderer.setDocumentFromString(inputContent);
	        renderer.layout();
	        renderer.createPDF(baoStream);
 
	        return baoStream.toByteArray();
	    } catch (DocumentException e) {
	        e.printStackTrace();
	        throw new ConversionException();
	    }
	}

	private byte[] convertFromFile(URL urlInputHTML) throws ConversionException {
	    try {
	        ITextRenderer renderer = new ITextRenderer();
	        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
	        String inputContent = urlInputHTML.toString();

	        renderer.setDocument(inputContent);
	        renderer.layout();
	        renderer.createPDF(baoStream);
 
	        return baoStream.toByteArray();
	    } catch (DocumentException e) {
	        e.printStackTrace();
	        throw new ConversionException();
	    }
	}

	public URL getUrlInputHTML() {
		return urlInputHTML;
	}

	public void setUrlInputHTML(URL urlInputHTML) {
		reset();
		this.urlInputHTML = urlInputHTML;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		reset();
		this.htmlContent = htmlContent;
	}

	public byte[] getPdfContent() {
		return pdfContent;
	}

}
