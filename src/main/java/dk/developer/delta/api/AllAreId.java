package dk.developer.delta.api;

import cz.jirutka.validator.collection.CommonEachValidator;
import cz.jirutka.validator.collection.constraints.EachConstraint;
import dk.developer.database.DatabaseObject;
import dk.developer.validation.single.Id;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CommonEachValidator.class)
@EachConstraint(validateAs = Id.class)
public @interface AllAreId {
    Class<? extends DatabaseObject> of();

    String message() default "Not all were the id of a database object";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}