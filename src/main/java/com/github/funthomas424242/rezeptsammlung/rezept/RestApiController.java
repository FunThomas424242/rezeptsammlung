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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

@RestController
@RequestMapping(path = "/api/rezepte")
public class RestApiController {

    @Autowired
    protected NitriteTemplate nitriteTemplate;

    @Autowired
    protected PersistenzService rezeptService;


    @GetMapping(path = "/tags", produces = "application/json")
    public ResponseEntity<Set<String>> getAllTags() {
        final Set<String> tags = rezeptService.alleTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping(path = "/all", produces = "application/json")
    public ResponseEntity<List<Rezept>> getAllRezepte() {
        final List<Rezept> rezepte = rezeptService.alleRezepte();
        return new ResponseEntity<>(rezepte, HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Rezept> getRezept(@PathVariable final long id) {
        final NitriteRepository<Rezept> repository = rezeptService.getRezeptRepository();
        final Rezept rezept = repository.find(eq("id", id)).firstOrDefault();
        repository.close();
        return new ResponseEntity<Rezept>(rezept, HttpStatus.OK);
    }

    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Rezept> addRezept(@RequestBody final Rezept rezept) {
        final NitriteRepository<Rezept> repository = rezeptService.getRezeptRepository();
        repository.insert(rezept);
        repository.close();
        return new ResponseEntity<Rezept>(rezept, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Rezept> deleteRezept(@PathVariable final long id) {
        final NitriteRepository<Rezept> repository = rezeptService.getRezeptRepository();
        final int geloeschteAnzahl = repository.remove(eq("id", id)).getAffectedCount();
        repository.close();
        if (geloeschteAnzahl == 0) {
            return new ResponseEntity<Rezept>(HttpStatus.NOT_FOUND);
        } else if (geloeschteAnzahl == 1) {
            return new ResponseEntity<Rezept>(HttpStatus.OK);
        } else if (geloeschteAnzahl > 1) {
            return new ResponseEntity<Rezept>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<Rezept>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
