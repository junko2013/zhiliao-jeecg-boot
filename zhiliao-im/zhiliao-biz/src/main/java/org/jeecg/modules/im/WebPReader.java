package org.jeecg.modules.im;

import com.luciad.imageio.webp.WebPReadParam;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class WebPReader {

    static {
        // 注册 WebP ImageReader 和 ImageWriter
        ImageIO.scanForPlugins();
    }

    public static void main(String[] args) {
        try {
            // Obtain a WebP ImageReader instance
            ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();

// Configure decoding parameters
            WebPReadParam readParam = new WebPReadParam();
            readParam.setBypassFiltering(true);

// Configure the input on the ImageReader
            reader.setInput(new FileImageInputStream(new File("C:\\Users\\trayc\\Downloads\\Telegram Desktop\\xiongmao_fafeng_batch_webp\\743710627\\0.webp")));

// Decode the image
            BufferedImage image = reader.read(0, readParam);
            System.out.println(image.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}