package app.service;

import app.config.AppProperties;
import java.text.DecimalFormat;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ValidationService implements IValidationService {
    
    private final AppProperties appProperties;
    
    public ValidationService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }
    
    @Override
    public String getAmountWithFormat(Double amount) {
        amount = Optional.ofNullable(amount).orElse(0D);
        
        return new DecimalFormat(appProperties.getCurrencyFormat()).format(amount);
    }
    
}