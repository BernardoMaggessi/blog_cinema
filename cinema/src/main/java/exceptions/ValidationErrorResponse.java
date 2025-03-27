package exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse extends StandardError {
    private List<String> details = new ArrayList<>();

    public ValidationErrorResponse() {
        super(400, "Erro de validação");
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public void addError(String detail) {
        this.details.add(detail);
    }
}