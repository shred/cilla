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
package org.shredzone.cilla.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.cilla.ws.TimeDefinition;
import org.shredzone.cilla.ws.page.PageWs;
import org.shredzone.cilla.ws.system.LanguageDto;
import org.shredzone.cilla.ws.system.SystemWs;
import org.shredzone.cilla.ws.user.UserDto;
import org.shredzone.cilla.ws.user.UserWs;
import org.springframework.stereotype.Component;

/**
 * A bean that provides various lists to be used for select boxes.
 * <p>
 * List results may change depending on the logged in user.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ListBean {

    private @Resource PageWs pageWs;
    private @Resource SystemWs systemWs;
    private @Resource UserWs userWs;

    /**
     * Returns a list of available text format types.
     */
    public List<SelectItem> createTextFormatList() {
        ResourceBundle msg = getResourceBundle();

        TextFormat[] values = TextFormat.values();
        SelectItem[] items = new SelectItem[values.length];
        for (int ix = 0; ix < values.length; ix++) {
            String value = values[ix].name();
            String label = msg.getString("select.textformat." + value.toLowerCase());
            String description = msg.getString("select.textformat.tt." + value.toLowerCase());
            items[ix] = new SelectItem(value, label, description);
        }
        return Arrays.asList(items);
    }

    /**
     * Returns a list of available users.
     */
    public List<SelectItem> createUserList() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (UserDto user : userWs.list(null)) {
            result.add(new SelectItem(user.getId(), user.getLogin(), user.getName()));
        }
        return result;
    }

    /**
     * Returns a list of available languages.
     */
    public List<SelectItem> createLanguageList() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (LanguageDto lang : systemWs.listLanguages()) {
            result.add(new SelectItem(lang.getId(), lang.getName()));
        }
        return result;
    }

    /**
     * Returns a list of available time precisions.
     */
    public List<SelectItem> createTimeDefinitionList() {
        ResourceBundle msg = getResourceBundle();

        TimeDefinition[] values = TimeDefinition.values();
        SelectItem[] items = new SelectItem[values.length];
        for (int ix = 0; ix < values.length; ix++) {
            String value = values[ix].name();
            String label = msg.getString("select.timedefinition." + value.toLowerCase());
            String description = msg.getString("select.timedefinition.tt." + value.toLowerCase());
            items[ix] = new SelectItem(value, label, description);
        }
        return Arrays.asList(items);
    }

    /**
     * Returns a list of section types.
     */
    public List<SelectItem> getSectionList() {
        ResourceBundle msg = getResourceBundle();

        List<SelectItem> result = new ArrayList<SelectItem>();
        for (String type : pageWs.getSectionTypes()) {
            String label = msg.getString("select.sectiontype." + type);
            String description = msg.getString("select.sectiontype.tt." + type);
            result.add(new SelectItem(type, label, description));
        }
        return result;
    }

    /**
     * Returns a {@link ResourceBundle} to be used for i18n. The locale used depends on
     * the locale settings of the visiting user.
     */
    private ResourceBundle getResourceBundle() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Locale loc = ctx.getViewRoot().getLocale();
        return ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), loc);
    }

}
