# `<cilla:commentthread>`

Fetches a `CommentThreadModel` for a `Commentable` entity.

The `CommentThreadModel` is optimized for output of comments on web pages. It contains the number of comments, the date of the last comment, and a list of all comments. The comments are ordered in a flattened tree fashion, with `level` giving the nesting level in the comment tree.

## Parameters

* `item` (required): the `Commentable` entity to get the comments from.
* `var` (required): the name of the variable to store the result into.
* `scope`: variable scope ("`page`", "`request`", "`session`", "`application`"), defaults to `page` scope if not given.
