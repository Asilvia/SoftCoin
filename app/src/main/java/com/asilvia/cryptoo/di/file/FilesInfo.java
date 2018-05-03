package com.asilvia.cryptoo.di.file;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by asilvia on 27/04/2018.
 */

public class FilesInfo {
    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FileInfo {

    }
}
