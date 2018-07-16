/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2018 Richard "Shred" Körber
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
package org.shredzone.cilla.ws.assembler;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.VideoSection;
import org.shredzone.cilla.core.repository.SectionDao;
import org.shredzone.cilla.ws.assembler.annotation.SectionAssemblerType;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.VideoSectionDto;
import org.springframework.stereotype.Component;

/**
 * {@link SectionAssembler} for {@link VideoSectionDto} and {@link VideoSection}.
 * <p>
 * Projections are <em>not</em> supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
@SectionAssemblerType(type = "video", entity = VideoSection.class, dto = VideoSectionDto.class)
public class VideoSectionAssembler extends AbstractSectionAssembler<VideoSection, VideoSectionDto> {

    private @Resource SectionDao sectionDao;

    @Override
    public VideoSectionDto assemble(VideoSection entity) {
        VideoSectionDto dto = new VideoSectionDto();
        dto.setId(entity.getId());
        dto.setVideoId(entity.getVideoId());
        dto.setAutoplay(entity.isAutoplay());
        dto.setFullscreen(entity.isFullscreen());
        dto.setLoop(entity.isLoop());
        return dto;
    }

    @Override
    public void merge(VideoSectionDto dto, VideoSection entity) {
        entity.setVideoId(dto.getVideoId());
        entity.setAutoplay(dto.isAutoplay());
        entity.setFullscreen(dto.isFullscreen());
        entity.setLoop(dto.isLoop());
    }

    @Override
    public VideoSection persistSection(VideoSectionDto dto, Page page) throws CillaServiceException {
        VideoSection sec;

        if (dto.isPersisted()) {
            sec = (VideoSection) sectionDao.fetch(dto.getId());
            merge(dto, sec);
            updateSection(sec);

        } else {
            sec = new VideoSection();
            merge(dto, sec);
            addSection(page, sec);
        }

        return sec;
    }

    @Override
    public VideoSectionDto createSection() throws CillaServiceException {
        VideoSection sec = new VideoSection();
        sec.setAutoplay(true);      // TODO: configurable
        sec.setFullscreen(true);
        return assemble(sec);
    }

    @Override
    public void delete(VideoSection entity) throws CillaServiceException {
        removeSection(entity);
    }

}
