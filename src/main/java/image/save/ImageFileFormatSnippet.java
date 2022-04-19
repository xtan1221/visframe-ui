package image.save;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

/**
 * discover all currently known ImageWriter implementations with following snippet
 * 
 * reference https://stackoverflow.com/questions/59089118/javax-imageio-imageio-file-format-constants
 * @author tanxu
 *
 */
public class ImageFileFormatSnippet {
    public static void main(String[] args) {
        IIORegistry registry = IIORegistry.getDefaultInstance();
        Iterator<ImageWriterSpi> serviceProviders = registry.getServiceProviders(ImageWriterSpi.class, false);
        while(serviceProviders.hasNext()) {
            ImageWriterSpi next = serviceProviders.next();
            System.out.printf("description: %-27s   format names: %s%n",
                    next.getDescription(Locale.ENGLISH),
                    Arrays.toString(next.getFormatNames())
            );
        }
    }
}
