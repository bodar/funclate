package com.googlecode.funclate.stringtemplate;

import com.googlecode.funclate.Renderer;
import org.junit.Test;

import static com.googlecode.funclate.Funclate.methods.defaultFunclates;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RendererAdapterTest {
    @Test
    public void supportsDifferentFormatsForAnyWrappedRenderer() throws Exception{
        RendererAdapter adapter = new RendererAdapter(defaultFunclates());
        assertThat(adapter.toString("<root/>", "xml"), is("&lt;root/&gt;"));
        assertThat(adapter.toString("<root/>", "html"), is("&lt;root/&gt;"));
        assertThat(adapter.toString("Some values", "url"), is("Some+values"));
        assertThat(new RendererAdapter(defaultFunclates()).toString("Some values", "raw"), is("Some values"));
    }

    @Test
    public void supportsChainingFormats() throws Exception{
        assertThat(new RendererAdapter(defaultFunclates()).toString("1 < 2", "raw,html"), is("1 &lt; 2"));
    }

    private Renderer<Object> callingThrows() {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                throw new RuntimeException("Should never get here");
            }
        };
    }


    private Renderer<Object> returns() {
        return new Renderer<Object>() {
            public String render(Object instance) throws Exception {
                return instance.toString();
            }
        };
    }
}
