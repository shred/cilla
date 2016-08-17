# `<cilla:feed>`

Creates a link to a RSS or ATOM feed of the given category.

## Parameters

* `type` (required): Feed type (`"RSS"`, `"RSS2"`, `"ATOM"`), case sensitive!
* `title`: title of the link tag.
* `ref`: a `Linkable` reference to generate the feed for. If not given, a feed is generated for all pages.
* `category`: limit the feed to the given `Category`.
* `tag`: limit the feed to the given `Tag`.
* `author`: limit the feed to the given `Author`.
* `var`: the name of the variable to store the feed URL into. If not given, a `<link>` tag is generated and written to the result.
* `scope`: variable scope ("`page`", "`request`", "`session`", "`application`"), defaults to `page` scope if not given.
