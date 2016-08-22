# `<cilla:mapinit>`

Prepares the rendered page for Map output by inserting all necessary code for the map implemenation that is being used.

Can be used anywhere in the JSP page, but must not be used more than once per page! To keep traffic down, it is suggested to use it only if a map shall be actually rendered.

## Parameters

* `key`: An API key that is passed to the init JavaScript. Optional, may be left out or `null` if not required by the underlying implementation.
