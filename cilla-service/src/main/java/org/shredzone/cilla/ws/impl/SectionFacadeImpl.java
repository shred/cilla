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
package org.shredzone.cilla.ws.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.ws.SectionFacade;
import org.shredzone.cilla.ws.assembler.SectionAssembler;
import org.shredzone.cilla.ws.assembler.annotation.SectionAssemblerType;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.SectionDto;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link SectionFacade}.
 *
 * @author Richard "Shred" Körber
 */
@SuppressWarnings({"unchecked", "rawtypes"}) // Proposals for a better design are very welcome!
@Component
public class SectionFacadeImpl implements SectionFacade {

    private @Resource List<SectionAssembler> assemblerList;

    private Map<Class<? extends Section>, SectionAssembler> sourceMap = new HashMap<>();
    private Map<Class<? extends SectionDto>, SectionAssembler> targetMap = new HashMap<>();
    private SortedMap<String, SectionAssembler> typeMap = new TreeMap<>();

    @PostConstruct
    public void setup() {
        for (SectionAssembler assembler : assemblerList) {
            SectionAssemblerType anno = assembler.getClass().getAnnotation(SectionAssemblerType.class);
            Class<? extends Section> sectionType = anno.entity();
            Class<? extends SectionDto> targetType = anno.dto();
            sourceMap.put(sectionType, assembler);
            targetMap.put(targetType, assembler);
            typeMap.put(anno.type(), assembler);
        }
    }

    @Override
    public List<SectionDto> assembleSections(List<Section> sections) throws CillaServiceException {
        List<SectionDto> result = new ArrayList<>(sections.size());
        for (Section sec : sections) {
            SectionAssembler assembler = sourceMap.get(sec.getClass());
            result.add((SectionDto) assembler.assemble(sec));
        }
        return result;
    }

    @Override
    public Section persistSection(SectionDto dto, Page page) throws CillaServiceException {
        SectionAssembler assembler = targetMap.get(dto.getClass());
        return assembler.persistSection(dto, page);
    }

    @Override
    public void deleteSection(Section entity) throws CillaServiceException {
        SectionAssembler assembler = sourceMap.get(entity.getClass());
        assembler.delete(entity);
    }

    @Override
    public SectionDto createSection(String type) throws CillaServiceException {
        SectionAssembler assembler = typeMap.get(type);
        if (assembler == null) {
            throw new IllegalArgumentException("Unknown section type " + type);
        }
        return assembler.createSection();
    }

    @Override
    public Collection<String> getSectionTypes() {
        return Collections.unmodifiableCollection(typeMap.keySet());
    }

}
