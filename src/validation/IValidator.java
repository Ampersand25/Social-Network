package validation;

import exception.ValidationException;

public interface IValidator<T> {
    public void validate(T entity) throws ValidationException;
}
