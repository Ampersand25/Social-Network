package validation;

import exception.ValidationException;

public interface IValidator<T> {
    /**
     * Metoda publica de tip void (nu returneaza/intoarce nicio valoare) care valideaza o entitate entity de tipul T
     * Metoda este de tip template (este parametrizata) avand parametrul T care reprezinta tipul (type-ul) entitatii entity (obiect de clasa T)
     * @param entity - obiect de tipul T, entitatea pe care dorim sa o validam (T poate sa fie User sau Friendship)
     * @throws ValidationException - daca entitatea entity nu este valida (are cel putin un atribut/camp invalid)
     */
    public void validate(T entity) throws ValidationException;
}
