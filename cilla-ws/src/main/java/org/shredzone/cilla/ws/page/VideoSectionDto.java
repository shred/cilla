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
package org.shredzone.cilla.ws.page;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Video Section.
 *
 * @author Richard "Shred" Körber
 */
@XmlRootElement
public class VideoSectionDto extends SectionDto {
    private static final long serialVersionUID = 3989530686600710878L;

    private String videoId;
    private boolean autoplay;
    private boolean loop;
    private boolean fullscreen;

    @Override
    public String getType()                     { return "video"; }

    @NotNull
    public String getVideoId()                  { return videoId; }
    public void setVideoId(String videoId)      { this.videoId = videoId; }

    public boolean isAutoplay()                 { return autoplay; }
    public void setAutoplay(boolean autoplay)   { this.autoplay = autoplay; }

    public boolean isLoop()                     { return loop; }
    public void setLoop(boolean loop)           { this.loop = loop; }

    public boolean isFullscreen()               { return fullscreen; }
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof VideoSectionDto && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
