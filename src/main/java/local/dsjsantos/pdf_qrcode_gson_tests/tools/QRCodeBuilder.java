package local.dsjsantos.pdf_qrcode_gson_tests.tools;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Hashtable;

@SuppressWarnings("unused")
public class QRCodeBuilder {
	
	private String qrCodeText;
	private Integer size = 100;
	private Integer margin = 4;
	private String imageType = "png";
	private byte[] qrCode = null;

	public QRCodeBuilder() throws WriterException, IOException {
		this.qrCodeText = null;
		update();
	}
	
	public QRCodeBuilder(String qrCodeText) throws WriterException, IOException {
		this.qrCodeText = qrCodeText;
		update();
	}

	public QRCodeBuilder(String qrCodeText, int size) throws WriterException, IOException {
		this.qrCodeText = qrCodeText;
		this.size = size;
		update();
	}
	
	public QRCodeBuilder(String qrCodeText, String imageType) throws WriterException, IOException {
		this.qrCodeText = qrCodeText;
		this.imageType = imageType;
		update();
	}
	
	public QRCodeBuilder(String qrCodeText, int size, String imageType) throws WriterException, IOException {
		this.qrCodeText = qrCodeText;
		this.size = size;
		this.imageType = imageType;
		update();
	}
	
	public QRCodeBuilder(String qrCodeText, int size, int margin, String imageType) throws WriterException, IOException {
		this.qrCodeText = qrCodeText;
		this.size = size;
		this.margin = margin;
		this.imageType = imageType;
		update();
	}
	
	public void update() throws WriterException, IOException {
		if(this.qrCodeText != null) {
			this.qrCode = createQRCodeImage();
		}
	}

	public boolean save(URL urlFile) throws IOException, SecurityException {
        if(this.qrCode != null) {
        	try {
    	        OutputStream os = new FileOutputStream(Paths.get(urlFile.toURI()).toString());
    	        os.write(this.qrCode);
    	        os.close();
    	        return true;
        	} catch(URISyntaxException e) {
        		return false;
        	}
        }
        return false;
	}

	public String getQrCodeBase64() {
		return (this.qrCode == null ? null: Base64.getEncoder().encodeToString(this.qrCode));
	}
	
	private byte[] createQRCodeImage() throws WriterException, IOException {

		// Create the ByteMatrix for QR-Code that encodes the String
		Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, this.margin);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(this.qrCodeText, BarcodeFormat.QR_CODE, this.size, this.size, hints);
		
		// Make the BufferedImage to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		// Clear Image (Paint all with White color)
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);

		// Paint image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		final ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
		if(ImageIO.write(image, this.imageType, baoStream)) {
			return baoStream.toByteArray();
		}
		
		return null;
	}


	public String getQrCodeText() {
		return qrCodeText;
	}

	public void setQrCodeText(String qrCodeText) {
		this.qrCodeText = qrCodeText;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getMargin() {
		return margin;
	}

	public void setMargin(Integer margin) {
		this.margin = margin;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public byte[] getQrCode() {
		return qrCode;
	}
	
}

