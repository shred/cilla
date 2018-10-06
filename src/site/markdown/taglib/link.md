# `<cilla:link>`

Creates a link to a Cilla view.

## Parameters

A combination of these parameters, to find a matching view to link to:

* `page`: `Page` instance
* `category`: `Category` instance
* `tag`: `Tag` instance
* `author`: `User` instance
* `section`: `Section` instance
* `picture`: `Picture` instance
* `header`: `Header` instance
* `ref`: A generic `Linkable` instance
* `commentable`: `Commentable` instance

A view can be further selected by its view name and/or qualifier:

* `view`: View name
* `qualifier`: View qualifier

These parameters are optional:

* `anchor`: An anchor to append to the generated URL
* `id`: `id` attribute of the generated `<a>` tag
* `target`: `target` attribute of the generated `<a>` tag
* `rel`: `rel` attribute of the generated `<a>` tag
* `title`: `title` attribute of the generated `<a>` tag
* `styleClass`: `class` attribute of the generated `<a>` tag
* `style`: `style` attribute of the generated `<a>` tag
* `onclick`: `onclick` attribute of the generated `<a>` tag
* `var`: the name of the variable to store the generated URL into. No `<a>` tag is written.
* `scope`: variable scope ("`page`", "`request`", "`session`", "`application`"), defaults to `page` scope if not given.

## Sub Tags

* [`<cilla:param>`](./param.html): Query parameters that are appended to the generated URL. If the parameter name starts with a hash '#', it will replace appropriate placeholders in the URL pattern instead.
