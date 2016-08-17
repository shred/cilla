# `<cilla:format>`

Formats a plain text into HTML.

## Parameters

* `text`: The text to be formatted. Can be a `FormattedText` or any other `Object` (`toString()` is used then). If not given, the tag body is used.
* `format`: The format of the `text`. If `text` is a `FormattedText` instance, `format` must be omitted. These formats are available (case sensitive):
    - `PLAIN`: Plain text. HTML characters are escaped and line breaks are converted.
    - `PREFORMATTED`: Preformatted text. HTML characters are escaped and line breaks are converted. The result is displayed in a monospaced font.
    - `SIMPLIFIED`: Plain text with HTML markup. Only a few safe HTML tags are kept, all other HTML is removed. Line breaks are converted.
    - `PARAGRAPHED`: Plain text with HTML markup. Line breaks are converted. All HTML markup is kept. *Potentially harmful*, do not use for guest input.
    - `HTML`: `text` contains HTML and is not modified at all. *Potentially harmful*, do not use for guest input.
    - `TEXTILE`: Plain text with Textile markup. *Could be dangerous* due to its complex nature, only use it for trusted sources.
* `page`: `Page` context for resolving relative links in Textile mode.
* `stripHtml`: if `true`, all HTML markup is stripped from the result. Whitespaces are smartly inserted (e.g. `foo<br>bar` is converted to `foo bar`).
* `truncate`: if set, the text will not exceed this length. It will be truncated appropriately, and an ellipsis ('…') is attached. Truncation is tried to be made at word boundaries.
* `var`: the name of the variable to store the result into. If not set, the result is written to the response stream.
* `scope`: variable scope ("`page`", "`request`", "`session`", "`application`"), defaults to `page` scope if not given.

## Examples

* Strip all HTML from the body:

```html
<cilla:format stripHtml="true">
  This is some <b>Text</b> with HTML markup.
</cilla:format>
```
