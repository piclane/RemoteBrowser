package hina.remotebrowser.server.servlet;

import hina.remotebrowser.server.utils.ServletRequestParams;
import hina.remotebrowser.ui.Browser;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet {
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletContext context = getServletContext();
		Browser browser = (Browser) context.getAttribute("browser");
		ServletRequestParams params = new ServletRequestParams(req);
		
		String imageType = params.getString("imageType", "jpeg");
		int quality = params.getInt("quality", 80);
		int scale = params.getInt("scale", 100);
		
		ServletUtil.sendCacheBuster(resp, "image/" + imageType);
		
		byte[] pngBytes;
		try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			browser.capture(out);
			out.flush();
			pngBytes = out.toByteArray();
		}
		
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(pngBytes));
		
		if(scale != 100) {
			double _scale = (double)scale / 100.0;
			int width = (int)(image.getWidth() * _scale);
			int height = (int)(image.getHeight() * _scale);
			
			BufferedImage _image = new BufferedImage(width, height, image.getType());
			Graphics g = null;
			try {
				g = _image.getGraphics();
				
				g.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
			} finally {
				g.dispose();
			}
			image = _image;
		}
		
		try(OutputStream out = resp.getOutputStream()) {
			switch(imageType) {
				case "jpeg":
					writeJpegImage(out, image, quality / 100.0f);
					break;
				case "png":
					writePngImage(out, image);
					break;
			}
		}
	}
	
	private static void writeJpegImage(OutputStream out, RenderedImage image,  float quality) throws IOException {
		ImageWriter jpegWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
		ImageWriteParam param = jpegWriter.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(quality);
		jpegWriter.setOutput(ImageIO.createImageOutputStream(out));
		jpegWriter.write(null, new IIOImage(image, null, null), param);
		jpegWriter.dispose();
	}
	
	private static void writePngImage(OutputStream out, RenderedImage image) throws IOException {
		ImageIO.write(image, "png", out);
	}
}
