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
package org.shredzone.cilla.web.comment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A comment thread.
 *
 * @author Richard "Shred" Körber
 */
public class CommentThreadModel implements Serializable {
    private static final long serialVersionUID = 1962797484608958144L;

    private List<CommentModel> thread;
    private int count;
    private Date lastCommented;

    /**
     * Thread entries, in a threaded sequence.
     */
    public List<CommentModel> getThread()       { return thread; }
    public void setThread(List<CommentModel> thread) { this.thread = thread; }

    /**
     * Number of comments.
     */
    public int getCount()                       { return count; }
    public void setCount(int count)             { this.count = count; }

    /**
     * Date of the most recent comment, or {@code null} if there are no comments yet.
     */
    public Date getLastCommented()              { return lastCommented; }
    public void setLastCommented(Date lastCommented) { this.lastCommented = lastCommented; }

}
