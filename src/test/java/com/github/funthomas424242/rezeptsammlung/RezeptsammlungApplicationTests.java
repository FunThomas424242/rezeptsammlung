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

import com.github.funthomas424242.rezeptsammlung.rezept.Rezept;
import com.github.funthomas424242.rezeptsammlung.rezept.RezeptRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RezeptsammlungApplicationTests {

    @Autowired
    RezeptRepository rezeptRepository;

    @Test
    @DisplayName("Prüfe ob geschriebene Werte wieder ausgelesen werden können.")
    void pruefeSchreibenLesen() {

        Rezept rezept = new Rezept("backen#apfelkuchen#1", "Apfelkuchen mit Hefeteig");
        rezeptRepository.save(rezept);

        final Rezept retrievedProduct = rezeptRepository.findById("backen#apfelkuchen#1").get();
        retrievedProduct.setTitel("Apfelkuchen mit Rührteig");
        rezeptRepository.save(retrievedProduct);
    }

}
