package com.googlecode.funclate.stringtemplate;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EnhancedStringTemplateGroupTest {
    @Test
    public void correctlyRemovedDoubleSlashes() throws Exception {
        String result = EnhancedStringTemplateGroup.format("jar:file:/home/dev/Projects/baron-greenback/build/artifacts/baron-greenback-dev.build.jar!/com/googlecode/barongreenback//search/list.st");
        assertThat(result, is("jar:file:/home/dev/Projects/baron-greenback/build/artifacts/baron-greenback-dev.build.jar!/com/googlecode/barongreenback/search/list.st"));
    }
}
