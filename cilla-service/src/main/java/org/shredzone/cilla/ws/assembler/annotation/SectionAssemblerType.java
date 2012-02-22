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
package org.shredzone.cilla.ws.assembler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.ws.assembler.SectionAssembler;
import org.shredzone.cilla.ws.page.SectionDto;

/**
 * Annotates a {@link SectionAssembler}, providing information about the handled types.
 *
 * @author Richard "Shred" Körber
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SectionAssemblerType {

    /**
     * {@link Section} class handled by the annotated assembler.
     */
    Class<? extends Section> entity();

    /**
     * {@link SectionDto} class handled by the annotated assembler.
     */
    Class<? extends SectionDto> dto();

    /**
     * Type name of the assembler.
     */
    String type();

}
