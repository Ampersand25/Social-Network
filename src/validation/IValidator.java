package validation;

import exception.ValidationException;

public interface IValidator<T> {
    /**
     * Metoda publica de tip void (nu returneaza/intoarce nicio valoare) care valideaza o entitate entity de tipul T (ii valideaza toate datele membru)<br>
     * Metoda este de tip template (este parametrizata) avand parametrul T care reprezinta tipul (type-ul) entitatii entity (obiect de clasa T)
     * @param entity obiect de tipul T (tip generic de date) reprezentand entitatea pe care dorim sa o validam (momentan T poate sa fie una din clasele User sau Friendship)
     * @throws ValidationException daca entitatea entity nu este valida (are cel putin un atribut/camp invalid)
     */
    public void validate(T entity) throws ValidationException;
}
