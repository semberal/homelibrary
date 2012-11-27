=======================================
Welcome to the HomeLibrary project page
=======================================

.. contents:: Table of Contents
   :backlinks: none

.. sectnum::

About
-----

Homelibrary is a simple application for categorizing books. I developed the application for my wife to help her with categorizing books.

Technologies and frameworks used
--------------------------------

Homelibrary is written in the Scala programming language and Play 2.0 web framework. The client side is written in HTML5 and JavaScript.

Metadata
--------

For each book the following attributes are stored:

  * ID
  * ISBN10, ISBN13
  * Title (required field)
  * Author(s) (required field)
  * Description
  * Publisher
  * Date published
  * Language
  * Page count
  * Cover picture
  * Tag(s)
  * Notes


Features
--------

* Automatically obtaining information about a book by specifying its identifier (either `ISBN10` or `ISBN13`) and downloading from `Google Books`_.
* Anonymous mode for browsing the books, administration mode for editing
* Easy categorization of books, attaching tags
* Search.

Technical description
---------------------

The application also serves as a showcase of interesting Scala and Play2.0 features, most notably:

* Consuming RESTful web services with Play2 Scala API
* Working with JSON using the Play2 API
* Internationalization in Play 2.0
* Utilizing Anorm - a simple yet powerful db layer
* Power of the Scala collections API when dealing with nontrivial Anorm result sets
* Elegant programming style with no mutable state
* Simplifying AJAX calls thanks to Play2.0 typesafe templates
* 


Limitations and possible future development
-------------------------------------------

* Currently, there is no advanced user management. Since the application was written for purely personal use, there is a single administrator account, whost password hard-coded in the application configuration.
* Since I'm not really good at design, the application uses Twitter bootstrap as it is, without any signification modifications


Packaging and running
---------------------

You can run the application using `play run` and then open `http://localhost:9000`. In the default configuration, it uses an in-memory H2 database, which is discarded on application restart. 
In order keep you modifications between application restart, you need to edit the `db.conf` configuration file, comment out the in-memory database and uncomment the line with flat-file
database configuration. Part of the database query string is also the path, where db files will be stored (which defaults to the user's home directory `~`).

Package the application for a production use can be dome using `play dist`.


.. _Google Books: http://books.google.com/
