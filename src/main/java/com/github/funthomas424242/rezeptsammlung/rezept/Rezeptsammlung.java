package com.github.funthomas424242.rezeptsammlung.rezept;

/*-
 * #%L
 * rezeptsammlung
 * %%
 * Copyright (C) 2019 PIUG
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

import com.github.funthomas424242.sbstarter.nitrite.NitriteRepository;
import com.github.funthomas424242.sbstarter.nitrite.NitriteTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/rezepte")
public class Rezeptsammlung {

    @Autowired
    protected NitriteTemplate nitriteTemplate;


    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Rezept> addRezept(@RequestBody final Rezept rezept) {
        final NitriteRepository<Rezept> repository = nitriteTemplate.getRepository(Rezept.class);
        repository.insert(rezept);
        repository.close();
        return new ResponseEntity<Rezept>(rezept, HttpStatus.CREATED);
    }

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<List<Rezept>> getAll(){
        final NitriteRepository<Rezept> repository = nitriteTemplate.getRepository(Rezept.class);
        final List<Rezept> rezepte = repository.find().toList();
        return new ResponseEntity<List<Rezept>>(rezepte,HttpStatus.OK);
    }

}
