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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.funthomas424242.rades.annotations.builder.RadesAddBuilder;
import com.github.funthomas424242.rades.annotations.builder.RadesNoBuilder;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.Id;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RadesAddBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rezept implements Serializable {

    @Id
    @RadesNoBuilder
    protected NitriteId id;

    @NotNull
    protected String titel;

    protected List<String> tags;

    public String getTitel() {
        return titel;
    }

    public List<String> getTags() {
        return tags;
    }


    public static Rezept of(final URL url) throws IOException, JSONException {
        final String jsonText = IOUtils.toString(url, StandardCharsets.UTF_8);
        final JSONObject json = new JSONObject(jsonText);
        return Rezept.of(json);
    }

    public static Rezept of(final JSONObject jsonObject) throws JSONException {
        final RezeptBuilder builder = new RezeptBuilder()
            .withTitel(jsonObject.getString("titel"));
        final JSONArray rawtags = jsonObject.getJSONArray("tags");
        final List<String> tags = new ArrayList<>(rawtags.length());
        for (int i = 0; i < rawtags.length(); i++) {
            tags.add(rawtags.getString(i));
        }
        return builder.build();
    }

    /**
     * Zwei Rezepte sollten fachlich gleich sein, wenn:
     * a) entweder die Id beider Rezepte gleich ist.
     * (Dann war es mal das gleiche Rezept welches inzwischen ggf. verändert wurde.)
     * Oder
     * b) alle Attribute außer der Id gleich sind.
     * (Dann ist es fachlich das gleiche Rezept, existiert aber evtl. bereits.)
     * <p>
     * Entsprechend muss dann auch der hashCode anders berechnet werden.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rezept)) return false;
        Rezept rezept = (Rezept) o;
        return id.equals(rezept.id) &&
            Objects.equals(titel, rezept.titel) &&
            Objects.equals(tags, rezept.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titel, tags);
    }

    @Override
    public String toString() {
        return "Rezept{" +
            "id=" + id +
            ", titel='" + titel + '\'' +
            ", tags=" + tags +
            '}';
    }
}
