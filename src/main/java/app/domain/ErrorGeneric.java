package app.domain;

import lombok.Data;

@Data
public class ErrorGeneric {
    private static final long serialVersionUID = 1L;
    
    private Integer status;
    
    private String title;
    
    private String message;
    
    private String image;
}