package org.jeecg.modules.im;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import java.util.Iterator;

public class CheckWebPSupport {
    public static void main(String[] args) {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        Iterator<ImageReaderSpi> imageReaderSpis = registry.getServiceProviders(ImageReaderSpi.class, false);

        boolean webpSupported = false;
        while (imageReaderSpis.hasNext()) {
            ImageReaderSpi readerSpi = imageReaderSpis.next();
            if (readerSpi.getFormatNames()[0].equalsIgnoreCase("webp")) {
                webpSupported = true;
                break;
            }
        }

        if (webpSupported) {
            System.out.println("WebP support is available.");
        } else {
            System.out.println("WebP support is not available.");
        }
    }
}