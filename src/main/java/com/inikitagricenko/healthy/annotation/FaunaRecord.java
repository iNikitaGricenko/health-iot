package com.inikitagricenko.healthy.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Identifies a domain object to be persisted to Fauna.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface FaunaRecord {
  /**
	 * The index the document representing the entity is supposed to be stored in. If not configured, a default
	 * collection name will be derived from the type's name.
	 * @return the name of the collection to be used.
	 */
	@AliasFor("collection")
	String index() default "";

	/**
	 * The collection the document representing the entity is supposed to be stored in. If not configured, a default
	 * collection name will be derived from the type's name.
	 * @return the name of the collection to be used.
	 */
	@AliasFor("index")
	String collection() default "";
}