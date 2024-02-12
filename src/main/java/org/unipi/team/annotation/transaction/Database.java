package org.unipi.team.annotation.transaction;

import org.unipi.team.annotation.consts.DataSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Database {

    String name();
    DataSource dbtype();
    String username();
    String password();
}
