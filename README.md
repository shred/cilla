# cilla  ![build status](https://shredzone.org/badge/cilla.svg)

_Cilla_ is a weblog written in Java.

## Features

* Articles, with nested categories and tagging.
* Suited for photo blogs: gallery with thumbnail generation, EXIF analysis, GPS support, tagging.
* Random title pictures.
* Comment system available.
* ATOM and RSS feeds.
* Full content search, using [Apache Lucene](http://lucene.apache.org/).
* Comes with a separate administration web frontend and a SOAP WS interface.
* Easy to customize. Uses JSP templates and a set of powerful JSP tags.
* Easy to extend. A collection of [plugins](https://codeberg.org/shred/cilla-plugin) is available. Plug in your own add-ons via Spring beans.
* Uses Java 8, Spring and Hibernate. Tested on PostgreSQL, but _should_ run on other DBMS as well.

## Documentation

See the [online documentation](https://shredzone.org/maven/cilla/). There is also a [set up guide](https://shredzone.org/maven/cilla/setup.html).

## Current Status

The idea behind _Cilla_ was just a "quick Java blog hack" that should replace my PHP based blog. However, this quick hack soon became a huge project that took me several years to make it what you see today.

The blog itself is feature complete and stable. I use it for [my personal weblog](https://shred.zone) since 2010.

The administration GUI is usable, but it misses some non-essential functions and also contains a few annoying bugs. Cilla also needs a nice installer (or maybe a Docker image), a choice of themes, and some in-depth documentation.

Cilla is sufficient for me in its current state, so I am not going to put much more time into it in the near future. However, I hope that this project might be useful to other people. If you want to improve Cilla, I really appreciate your help!

## Contribute

* Fork the [Source code at Codeberg](https://codeberg.org/shred/cilla). Feel free to send pull requests.
* Found a bug? [File a bug report!](https://codeberg.org/shred/cilla/issues)

## License

_cilla_ is open source software. The source code is distributed under the terms of [GNU Affero General Public License Version 3](http://www.gnu.org/licenses/agpl-3.0.html).
