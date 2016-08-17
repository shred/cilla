# Getting Started

_Cilla_ does not provide a fancy installer or a Docker image.

Your first job is to manually set up the _xample_ blog. But hey, isn't it more fun than just copying some files, clicking a few buttons, and _voilà_?

## Build Cilla

Building Cilla is straightforward. Just change to the project's root directory and invoke maven:

```
mvn clean install
```

It will take a while until it downloaded all dependencies, but then it will build all the modules in one go.

## Database

The next thing we are going to need is a database.

Cilla is tested on PostgreSQL. Other DBMS _should_ work, but are untested. This documentation expects that you have a PostgreSQL server already installed. For the first setup, I recommend to create a user `cilla` with the password `cilla`, and create a database called `cilla`:

```
createuser -P -S -R -D cilla
createdb -Ocilla cilla
```

Now we will set up the tables and initialize the content. Luckily, Maven has already generated a schema file for us.

```
psql cilla < cilla-core/target/schema/schema.sql
```

For the initial content, there is a file called `cilla-xample/initial-setup.sql`. It contains an administration user called "admin" (password "admin"), and a basic set of categories. Please review this file. You can keep the default values, or change them to your needs. After that, execute the setup file:

```
psql cilla < cilla-xample/initial-setup.sql
```

Your database is ready for your first run now.

## File System

Images and other media are not stored in the database, but on the file system. We are going to create the necessary structure now. The example was made for a Linux system, you need to adjust the paths on other operating systems (see _Configuration Files_ below).

```
sudo mkdir /var/local/cilla
sudo chown $USER: /var/local/cilla
mkdir /var/local/cilla/{ehcache,gravatar,search,store}
mkdir /var/local/cilla/store/{0..9}
```

## Configuration Files

There are a few configuration files. The default values are fine for a first run, but you may want to review them, and adjust them to your needs.

* `cilla-xample/main/resources/cilla-config.properties` - Some general configurations (like the global blog name).

* `cilla-xample/src/main/resources/ehcache.xml` - Caching parameters. The default values should be fine for most purposes, but you might need to adjust the default cache path.

* `cilla-xample/src/etc/jetty.xml` - The example blog's jetty configuration.

If you have used different file system paths in the step above, you need to adjust all of the configuration files accordingly!

After changing the files, remember to invoke `mvn install` first.

## First Start

The _xample_ blog is ready for its first launch now. Change to the `cilla-xample` module and start jetty:

```
cd cilla-xample
mvn jetty:run
```

Now point your browser to http://localhost:8090. You should see an empty blog now.

## Administration interface

The administration interface is a separate project that runs on its own web server and connects to your blog via SOAP web services. This means that *you have to keep your blog instance running* for administration.

If you keep the default configurations, the administration interface should start up just fine. However, changes can be made at `cilla-admin/src/etc/jetty.xml` if required. If you run your blog on a different IP or port, you need to adjust this file.

Now open another console, change to the admin module and start jetty:

```
cd cilla-admin
mvn jetty:run
```

You can reach the administration interface at http://localhost:8091 now, where you can login as "admin" (default password is "admin") and create your first blog article.

## Done!

Congratulations, you have successfully set up your first Cilla instance!

## Yay! What's next?

* Have a look at all the files in `cilla-xample`, to get an idea of how Cilla works.
* Read the documentation, to get acquainted with the [API](./apidocs/index.html) and Cilla's [JSP tag library](taglib/index.html).
* Start customizing the template files. You will find them in `cilla-xample/src/main/webapp/WEB-INF/jsp/`.
* Take a look at the [plugins](https://github.com/shred/cilla-plugin), and maybe write your own.
* Generally, have fun exploring and extending this project.
