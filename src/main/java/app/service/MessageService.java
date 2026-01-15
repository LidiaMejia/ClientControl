package app.service;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService implements IMessageService {

    private final MessageSource messageSource;
    
    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    
    @Override
    public String getMessage(String messageKey) {
        Locale locale = LocaleContextHolder.getLocale();
        
        return messageSource.getMessage(messageKey, null, locale);
    }
    
}