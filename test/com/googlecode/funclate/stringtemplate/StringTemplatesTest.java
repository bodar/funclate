package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Templates;
import com.googlecode.funclate.TemplatesTest;

public class StringTemplatesTest extends TemplatesTest{
    @Override
    protected Templates templates() {
        return new StringTemplates(StringTemplatesTest.class.getResource(""));
    }
}
