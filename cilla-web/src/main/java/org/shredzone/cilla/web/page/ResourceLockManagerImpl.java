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
package org.shredzone.cilla.web.page;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.shredzone.cilla.core.model.BaseModel;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link ResourceLockManager}.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ResourceLockManagerImpl implements ResourceLockManager {

    private static final String STORE_UNLOCK_ATTRIBUTE = "storeUnlockSet";

    @Override
    @SuppressWarnings("unchecked")
    public void unlockStore(HttpSession session, BaseModel entity) {
        String entityName = getEntityName(entity);
        HashMap<String, Set<Long>> unlockMap = (HashMap<String, Set<Long>>) session.getAttribute(STORE_UNLOCK_ATTRIBUTE);

        if (unlockMap == null) {
            unlockMap = new HashMap<String, Set<Long>>();
            session.setAttribute(STORE_UNLOCK_ATTRIBUTE, unlockMap);
        }

        Set<Long> unlockSet = unlockMap.get(entityName);
        if (unlockSet == null) {
            unlockSet = new HashSet<Long>();
            unlockMap.put(entityName, unlockSet);
        }

        unlockSet.add(entity.getId());
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isUnlocked(HttpSession session, BaseModel entity) {
        HashMap<String, Set<Long>> unlockMap = (HashMap<String, Set<Long>>) session.getAttribute(STORE_UNLOCK_ATTRIBUTE);

        if (unlockMap != null) {
            Set<Long> unlockSet = unlockMap.get(getEntityName(entity));
            if (unlockSet != null) {
                return unlockSet.contains(entity.getId());
            }
        }

        return false;
    }

    /**
     * Gets the entity name.
     *
     * @param entity
     *            the entity
     * @return the entity name
     */
    private String getEntityName(BaseModel entity) {
        return entity.getClass().getSimpleName();
    }

}
