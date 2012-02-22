/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2012 Richard "Shred" Körber
 *   http://cilla.shredzone.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.shredzone.cilla.core.repository;

import org.shredzone.cilla.core.model.Token;
import org.shredzone.cilla.core.model.User;

/**
 * DAO for {@link Token} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface TokenDao extends BaseDao<Token> {

    /**
     * Fetches a {@link Token} for the given {@link User} and site name.
     *
     * @param user
     *            {@link User}
     * @param site
     *            Site name
     * @return {@link Token} that was found, or {@code null} if there is no such token
     */
    Token fetch(User user, String site);

    /**
     * Stores a token. If there is already such a token, it will be replaced.
     *
     * @param user
     *            {@link User}
     * @param site
     *            Site name
     * @param token
     *            Token to be stored
     * @param secret
     *            Secret to the token
     */
    void store(User user, String site, String token, String secret);

    /**
     * Removes a token. If there is no such token, nothing happens.
     *
     * @param user
     *            {@link User}
     * @param site
     *            Site name
     */
    void remove(User user, String site);

}
