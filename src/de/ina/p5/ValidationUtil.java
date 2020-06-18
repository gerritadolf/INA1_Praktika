package de.ina.p5;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;


/**
 * Diese Klasse dient zur Vereinfachung der Nutzung von Hibernate Validator.
 * 
 * Diese Klasse ist neu und nur ein einem Projekt getestet. Zudem ist sie nicht
 * auf Laufzeit oder Threadsicherheit optimiert.
 * 
 * Sie dient zu Lehrzwecken und kann nach Belieben verändert werden.
 * Verbesserungsvorschläge und Erweiterungen sind willkommen!
 * 
 * Kompatibel mit Hibernate Validator 6, Java 8
 *
 * @author Martin Schulten
 * @version 0.1
 */
public class ValidationUtil<BeanType> {

	private static Validator validator;
	private Set<ConstraintViolation<BeanType>> violations;

	/**
	 * Konstruktur erzeugt Validator
	 *
	 */
	public ValidationUtil() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	/**
	 * Prüft bean auf Validität.
	 *
	 * @return true/false
	 */
	public Boolean isValid(BeanType bean) {
		violations = validator.validate(bean);
		return violations.isEmpty();
	}

	/**
	 * Accessor für Validierungsverletzungen.
	 *
	 * @return Set von Validierungsverletzungen
	 */
	public Set<ConstraintViolation<BeanType>> getViolations() {
		return violations;
	}
}