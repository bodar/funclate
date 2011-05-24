package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Templates;
import com.googlecode.funclate.TemplatesTests;

import static com.googlecode.totallylazy.URLs.packageUrl;

public class StringTemplatesTest extends TemplatesTests {
    protected Templates templates() {
        return new StringTemplates(packageUrl(StringTemplatesTest.class));
    }
}
