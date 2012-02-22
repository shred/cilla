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
package org.shredzone.cilla.web.social.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.web.social.SocialLink;

/**
 * A method of a {@link SocialHandler} annotated Spring bean which creates a reference to
 * a social bookmark service.
 * <p>
 * As parameters, the method may ask for {@link Page}, or for {@link String} annotated
 * with {@link PageLink} or {@link PageTitle}.
 * <p>
 * The method must return either a {@link SocialLink} object, a {@link String} containing
 * the URL of the social bookmark, or {@code null} if no social bookmark service link is
 * to be rendered.
 *
 * @author Richard "Shred" Körber
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SocialBookmark {

    /**
     * If the handler method returns a {@link String}, this image file name is used as
     * icon for the social bookmark service.
     */
    String icon() default "";

    /**
     * If the handler method returns a {@link String}, this name is used for the social
     * bookmark service.
     */
    String name() default "";

}
