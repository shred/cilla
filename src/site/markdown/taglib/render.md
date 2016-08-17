# `<cilla:render>`

Renders a fragment by invoking the `@Fragment` annotated fragment handler. Actually, this is a very powerful tag, as parts of the content generation can be delegated to a Spring bean with full access to the backend services.

## Parameters

* `fragment` (required): Name of the fragment to be rendered. Throws an exception if no fragment with that name exists.
* `item`: Root object to be passed to the fragment handler.
* `rendered`: If `false`, the fragment handler will not be invoked and the fragment is not rendered. Can be used for conditional rendering of fragments.
* `optional`: If `true`, ignore if no fragment with the name was found.

## Sub Tags

* [`cilla:param`](./param.html): Additional parameters that are passed in to the fragment handler.
