package us.mude.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import javax.persistence.AttributeConverter;

public class MailConverter implements AttributeConverter<InternetAddress, String> {

    private static final Logger log = LoggerFactory.getLogger(MailConverter.class);

    @Override
    public String convertToDatabaseColumn(InternetAddress addr) {
        String strAddr = addr.getAddress();
        return strAddr;
    }

    @Override
    public InternetAddress convertToEntityAttribute(String strAddr) {
        InternetAddress a = null;
        try {
            a = new InternetAddress(strAddr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }
}
