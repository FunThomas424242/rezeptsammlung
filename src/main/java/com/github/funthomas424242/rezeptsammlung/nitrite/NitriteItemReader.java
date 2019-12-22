package com.github.funthomas424242.rezeptsammlung.nitrite;

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
import org.dizitart.no2.objects.Cursor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.AbstractCursorItemReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NitriteItemReader<T> extends AbstractCursorItemReader<T> {

    protected final NitriteRepository<T> repo;

    public NitriteItemReader(NitriteRepository<T> repo) {
        this.repo = repo;
    }

    @Override
    public String getSql() {
        return null;
    }

    @Override
    protected void cleanupOnClose() throws Exception {

    }

    @Override
    protected void openCursor(Connection connection) {

    }

    @Override
    protected T readCursor(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
