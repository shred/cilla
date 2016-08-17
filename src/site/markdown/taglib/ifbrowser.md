# `<cilla:ifbrowser>`

Body is only rendered if the User-Agent matches the given pattern.

## Parameters

* `agent` (required): A regular expression. If it matches the User-Agent header of the current HTTP request, the body is rendered.

## Note

The User-Agent header may be faked or missing. Never rely on an accurate information!

If used, a `Vary: User-Agent` header should be set in the response.
