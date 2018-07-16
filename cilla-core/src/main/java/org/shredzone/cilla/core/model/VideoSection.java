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
package org.shredzone.cilla.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * A section that embeds a video.
 *
 * @author Richard "Shred" Körber
 */
@Entity
public class VideoSection extends Section {
    private static final long serialVersionUID = -2527363754936592207L;

    private String videoId;
    private boolean autoplay;
    private boolean loop;
    private boolean fullscreen;

    /**
     * ID of the video.
     */
    @Column(nullable = false, length = 1024)
    public String getVideoId()                  { return videoId; }
    public void setVideoId(String videoId)      { this.videoId = videoId; }

    /**
     * If {@code true}, playback video immediately.
     */
    public boolean isAutoplay()                 { return autoplay; }
    public void setAutoplay(boolean autoplay)   { this.autoplay = autoplay; }

    /**
     * If {@code true}, playback in an endless loop.
     */
    public boolean isLoop()                     { return loop; }
    public void setLoop(boolean loop)           { this.loop = loop; }

    /**
     * If {@code true}, allow fullscreen playback.
     */
    public boolean isFullscreen()               { return fullscreen; }
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }

    @Override
    @Transient
    public String getType()                     { return "video"; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof VideoSection && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
