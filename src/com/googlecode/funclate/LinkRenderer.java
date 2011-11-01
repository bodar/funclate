package com.googlecode.funclate;

import java.net.URI;

public class LinkRenderer implements Renderer<URI> {
    public static Renderer<URI> toLink() {
        return new LinkRenderer();
    }

    public String render(URI uri) throws Exception {
        return String.format("<a href=\"%1$s\">%1$s</a>", uri);
    }
}