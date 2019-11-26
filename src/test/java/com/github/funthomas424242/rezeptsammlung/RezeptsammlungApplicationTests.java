package com.github.funthomas424242.rezeptsammlung;

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

import com.github.funthomas424242.rezeptsammlung.nitrite.NitriteRepository;
import com.github.funthomas424242.rezeptsammlung.nitrite.NitriteTemplate;
import com.github.funthomas424242.rezeptsammlung.rezept.Rezept;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RezeptsammlungApplicationTests {

    @Autowired
    NitriteTemplate nitriteTemplate;

    @Test
    @DisplayName("Prüfe ob geschriebene Werte wieder ausgelesen werden können.")
    void pruefeSchreibenLesen() {

        final Rezept rezept = new Rezept(1L, "Apfelkuchen mit Hefeteig");
        final NitriteRepository<Rezept,Long> repository = nitriteTemplate.getRepository(Rezept.class,Long.class);
        repository.insert(rezept);

        final Cursor<Rezept> retrievedProduct = repository.find(ObjectFilters.eq("titel", "Apfelkuchen mit Hefeteig"));
        assertEquals(1,retrievedProduct.size());
        final Cursor<Rezept> retrievedProduct1 = repository.find(ObjectFilters.eq("id", 1L));
        assertEquals(1,retrievedProduct1.size());



//        retrievedProduct.setTitel("Apfelkuchen mit Rührteig");
//        nitriteTemplate.save(retrievedProduct);
    }

}
