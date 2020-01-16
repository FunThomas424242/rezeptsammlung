package com.github.funthomas424242.rezeptsammlung;

/*-
 * #%L
 * rezeptsammlung
 * %%
 * Copyright (C) 2019 - 2020 PIUG
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AddResponseHeaderFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        //response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        //response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        //response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        //response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Service-Worker-Allowed", "/");
        filterChain.doFilter(request, response);
    }
}
