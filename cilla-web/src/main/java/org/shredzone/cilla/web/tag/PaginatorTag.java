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
package org.shredzone.cilla.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.service.search.PaginatorModel;
import org.shredzone.cilla.web.util.TagUtils;
import org.shredzone.jshred.spring.taglib.annotation.Tag;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.shredzone.jshred.spring.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Renders a paginator.
 *
 * @author Richard "Shred" Körber
 */
@Tag(type = BodyTag.class)
@TagInfo("Renders a paginator")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaginatorTag extends BodyTagSupport {
    private static final long serialVersionUID = 8472585544548644029L;

    private enum State {
        PREVIOUS, SETUP, PAGINATOR, NEXT, END;
    }

    // use even numbers for best results!
    private static final int maxPageGroupSize = 8;

    private @TagParameter(required = true) PaginatorModel model;
    private @TagParameter(required = true) String var;
    private @TagParameter String scope;

    private PaginatorModel iterator;
    private State state;
    private boolean separated;

    @Override
    public int doStartTag() throws JspException {
        if (model == null || model.isEmpty()) {
            return SKIP_BODY;
        }

        iterator = new PaginatorModel(model);
        state = State.PREVIOUS;

        TagUtils.setScopedAttribute(pageContext, var, iterator, scope);

        return nextIteration() ? EVAL_BODY_BUFFERED : SKIP_BODY;
    }

    @Override
    public int doAfterBody() throws JspException {
        if (bodyContent != null) {
            try {
                JspWriter out = getPreviousOut();
                out.print(bodyContent.getString().trim());
                bodyContent.clearBody();
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }

        return nextIteration() ? EVAL_BODY_AGAIN : SKIP_BODY;
    }

    /**
     * Performs the next iteration.
     */
    private boolean nextIteration() {
        if (state == State.PREVIOUS) {
            state = State.SETUP;

            if (!model.isFirstPage()) {
                iterator.setSelectedPage(model.getSelectedPage() - 1);
                setFragment("previous");
                return true;
            }
        }

        if (state == State.SETUP) {
            state = State.PAGINATOR;
            iterator.setSelectedPage(0);
            separated = false;

            setFragment("page");
            return true;
        }

        if (state == State.PAGINATOR) {
            int current = iterator.getSelectedPage();
            int next = computeNextPage(current);

            if (next != current) {
                if (!separated && (current + 1) != next) {
                    separated = true;
                    setFragment("separator");
                    return true;
                }

                iterator.setSelectedPage(next);
                separated = false;
                setFragment("page");
                return true;
            } else {
                state = State.NEXT;
            }
        }

        if (state == State.NEXT) {
            state = State.END;

            if (!model.isLastPage()) {
                iterator.setSelectedPage(model.getSelectedPage() + 1);
                setFragment("next");
                return true;
            }
        }

        return false;
    }

    /**
     * Sets the iterator's selected page to the next page number.
     *
     * @param current
     *            Current page
     * @return next page, or -1 if the last page was reached
     */
    private int computeNextPage(int current) {
        int lastPage = model.getPageCount() - 1;
        int start = Math.max(model.getSelectedPage() - (maxPageGroupSize / 2), 0);
        int end = Math.min(start + maxPageGroupSize, lastPage);
        start = Math.max(end - maxPageGroupSize, 0);

        if (current < start) {
            return start;
        } else if (current >= end) {
            return lastPage;
        } else {
            return current + 1;
        }
    }

    /**
     * Sets the fragment to be rendered.
     *
     * @param type
     *            Fragment type
     */
    private void setFragment(String type) {
        pageContext.setAttribute(FragmentTag.FRAGMENT_ATTRIBUTE, type);
    }

}
