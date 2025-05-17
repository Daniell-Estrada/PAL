// package com.example.pal.validation;

// import jakarta.validation.Constraint;
// import jakarta.validation.Payload;
// import java.lang.annotation.ElementType;
// import java.lang.annotation.Retention;
// import java.lang.annotation.RetentionPolicy;
// import java.lang.annotation.Target;

// @Constraint(validatedBy = UniqueCategoryNameValidator.class)
// @Target({ElementType.FIELD})
// @Retention(RetentionPolicy.RUNTIME)
// public @interface UniqueCategoryName {
//     String message() default "El nombre de la categoría ya existe";
//     Class<?>[] groups() default {};
//     Class<? extends Payload>[] payload() default {};
// }