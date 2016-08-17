# `<cilla:map>`

Renders a Map with the map implementation being used (e.g. _Google Maps_ or _OpenStreetMaps_). The map is centered around the given `Geolocation`.

## Parameters

* `location` (required): `Geolocation` to be shown on the map. The map is centered at this location.
* `satellite`: If `true`, a satellite view is used (if the map implementation offers it). Otherwise a plain roadmap is shown.
* `divId`: id attribute of the `<div>` the map is rendered into. Defaults to `"map_canvas"`.
* `styleClass`: `class` attribute of the `<div>` the map is rendered into.
* `style`: `style` attribute of the `<div>` the map is rendered into.
* `var`: the name of the variable to store the result into. The generated HTML is not written to the response, but returned in this variable.
* `scope`: variable scope ("`page`", "`request`", "`session`", "`application`"), defaults to `page` scope if not given.

## Nested Tags

* [`<cilla:mapmarker>`](./mapmarker.html): Marker to be shown on the map.
* [`<cilla:param>`](./param.html): Parameters for customizing the map (see below).

## Customizing Parameters

The map can be customized by using the [`<cilla:param>`](./param.html) tag. These param names are available:

* `"backgroundColor"`: Changes the background color of empty tiles of the map (if supported by the implementation).

Other (unknown) parameter names will be ignored.

## Note

If this tag is used, the [`<cilla:mapinit>`](./mapinit.html) tag must be present in the same page.
