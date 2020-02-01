package com.github.funthomas424242.rezeptsammlung.rezept;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiteController {

    public static final String ROUTE_INDEX = "index";
    public static final String ROUTE_REZEPTLISTE = "rezeptliste";

    @Autowired
    protected PersistenzService rezeptService;


    @GetMapping(value = "/index")
    public String index() {
        return ROUTE_INDEX;
    }

    @GetMapping(value = {"/rezeptliste"})
    public String rezepte(Model model) {
        model.addAttribute("rezepte", rezeptService.allRezepte());
        return ROUTE_REZEPTLISTE;
    }

    @GetMapping(value = {"/clearRezeptsammlung"})
    public String clearRezeptsammlung(Model model) {
        rezeptService.clearRezeptsammlung();
        return ROUTE_REZEPTLISTE;
    }

}
