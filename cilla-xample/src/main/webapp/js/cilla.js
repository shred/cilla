/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2013 Richard "Shred" Körber
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

gallery = {
    mapd: null,

    setupMap: function (url, title) {
        if (this.mapd) this.mapd.remove();
        this.mapd = $('<div>').dialog({
            open: function () {$(this).load(url);},
            autoOpen: false,
            width: 700,
            height: 500,
            draggable: true,
            title: title
        });
        var that = this;
        $('#mapinfo').show().click(function () {that.mapd.dialog('open');});
    }
};

commentform = {
    setReplyTo: function (id) {
        document.forms['postcomment']['cmtReplyTo'].value = id;
    }
};
