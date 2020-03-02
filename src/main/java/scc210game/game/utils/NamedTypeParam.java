package scc210game.game.utils;

import java.lang.annotation.*;

/**
 * An annotation used to give names to type parameters that may be confusing
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface NamedTypeParam {
    String name();
}
