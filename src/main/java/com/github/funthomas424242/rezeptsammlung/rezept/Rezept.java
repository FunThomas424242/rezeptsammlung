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

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.io.Serializable;


@Indices({
    @Index(value = "titel", type = IndexType.NonUnique),
    @Index(value = "id", type = IndexType.Unique)
})
public class Rezept implements Serializable {

    @Id
    protected long id;

    protected String titel;

    protected String tag;


    public Rezept(final Long id, final String titel) {
        this.id = id;
        this.titel = titel;
    }

    public void setTitel(final String titel) {
        this.titel = titel;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }


}
