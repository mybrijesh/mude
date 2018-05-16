package us.mude.util;


import javax.persistence.AttributeConverter;
import java.net.URI;

public class URIConverter implements AttributeConverter<URI, String> {

    @Override
    public String convertToDatabaseColumn(URI uri) {
        if (uri == null) {
            return "";
        }
        return uri.getPath();
    }

    @Override
    public URI convertToEntityAttribute(String strURI) {
        URI uri = null;
        try {
            uri = new URI(strURI);
        } catch (Exception e) {
        }
        return uri;
    }
}
