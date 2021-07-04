# `<cilla:image>`

Creates an `<img>` tag to an internal picture medium.

## Parameters

Exactly one of the following image sources must be set:

* `picture`: `Picture` instance to render the `<img>` tag of.
* `header`: `Header` instance to render the `<img>` tag of.
* `medium`: `Medium` instance to render the `<img>` tag of.

Additional parameters:

* `type`: Desired image type. The list of valid values depends on your implementation. Cilla tries to find a `ImageProcessingFactory` that accepts the given type. The Xample blog accepts `"thumb"` (100x100 PNG thumbnail) and `"preview"` (250x250 PNG large thumbnail). If missing, the picture is not scaled or processed at all.
* `uncropped`: For `Header` instances, if set to `true`, the uncropped version of the header image is shown. Ignored for all other image types.
* `nosize`: Set to `true` to omit automatically generated `width` and `height` attributes. This may be useful if there is a generic image filter that changes the size of all images.
* `styleClass`: A `class` attribute to be added to the `<img>` tag.
* `style`: A `style` attribute to be added to the `<img>` tag.
* `title`: A `title` attribute to be added to the `<img>` tag.
* `alt`: An `alt` attribute to be added to the `<img>` tag. Should be set for valid HTML, but is not enforced.
* `width`, `height`: `width` and/or `height` attributes to be added to the `<img>` tag. This will override any automatically generated `width` or `height` attributes.
* `var`: The name of the variable to store the image URL into. If set, no `<img>` tag is rendered.
* `scope`: Variable scope ("`page`", "`request`", "`session`", "`application`"), defaults to `page` scope if not given.

## Note

An `ImageProcessingFactory` is not limited to scaling down a picture. You can also implement an image processor for a black and white picture, or for adding a watermark to your picture.
