package com.x.program.center.jaxrs;

import javax.servlet.annotation.WebFilter;

import com.x.base.core.project.jaxrs.CipherManagerJaxrsFilter;

@WebFilter(urlPatterns = "/jaxrs/test/*", asyncSupported = true)
public class TestJaxrsFilter extends CipherManagerJaxrsFilter {

}
