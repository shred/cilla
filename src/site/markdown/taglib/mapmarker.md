# `<cilla:mapmarker>`

Sets additional markers in a map. There are pins shown at the position, but it is not guaranteed that the map detail that is initially presented to the visitor actually contains all map markers.

This tag must be nested inside the body of [`<cilla:map>`](./map.html).

## Parameters

* `location` (required): `Geolocation` of the marker.
* `text`: A text to be presented when the mouse moves over the marker. If not set, the tag body is used instead.
* `link`: A link to go to when the marker is clicked.

## Note

This tag is not final and may change in future Cilla releases.
