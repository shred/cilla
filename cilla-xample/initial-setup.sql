-- cilla - Blog Management System
--
-- Copyright (C) 2016 Richard "Shred" Körber
--   http://cilla.shredzone.org
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published
-- by the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU Affero General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.
--
-- ------------------------------------------------------------------------- --

-- This is an initial database setup for the example blog.
-- First set up the schema: cilla-core/target/schema/schema.sql



-- Set up the available authorities.
-- There are no customizable parts here. Do not change this!
INSERT INTO authority (id, version, name) VALUES
    (1, 0, 'COMMENTER'),
    (2, 0, 'MODERATOR'),
    (3, 0, 'EDITOR'),
    (4, 0, 'ROLEADMIN'),
    (5, 0, 'WEBSERVICE'),
    (6, 0, 'USERADMIN'),
    (7, 0, 'PREVIEW');
SELECT setval('seq_authority', 7);

-- Create the basic set of roles. You can add more roles later if needed.
INSERT INTO role (id, version, name) VALUES
    (1, 0, 'anonymous'),
    (2, 0, 'admin');
SELECT setval('seq_role', 2);

-- Associate the roles to the authorities
--   'anonymous' (not logged in) is only allowed to comment.
--   'admin' is allowed to do anything.
INSERT INTO role_authority (role_id, authorities_id) VALUES
    (1, 1),
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7);



-- Insert all the languages that will be available for blog articles.
--   Add more if needed.
INSERT INTO language (id, version, locale, name) VALUES
    (nextval('seq_language'), 0, 'en_US', 'English');

-- Insert the article categories and sub-categories.
--   Please change as needed.
--   'name' is a short category name
--   'title' is a longer description of the category (e.g. on tooltips)
--   'icon' is an icon name to be rendered by the frontend (e.g. an image name)
--   'parent_id' refers to the parent category of a sub-category
--   'sequence' is the order of the categories within the same parent
INSERT INTO category (id, version, name, parent_id, title, icon, caption, captionformat, sequence) VALUES
    (nextval('seq_category'), 0, 'Articles', NULL, 'My blog articles', 'book', NULL, 0, 1);

-- Finally, create the administator user.
--   Change as needed. The default login is: "admin" - password "admin"
--   The password is currently an unsalted sha256 hash.
--   bcrypt is on the todo list!
INSERT INTO login (id, version, login, mail, name, password, role_id, language_id, timezone) VALUES
    (nextval('seq_login'), 0, 'admin', 'me@example.com', 'Administrator',
        '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
        2, 1, 'UTC');


-- That's it. You should have an empty blog now.

--
