Code examples for the Squeryl Record chapter of the Lift Cookbook
================================================

This project contains examples from the [Lift Cookbook](http://shop.oreilly.com/product/0636920029151.do), online at [http://cookbook.liftweb.net/](http://cookbook.liftweb.net/).

To run this application:

* Launch SBT with: `./sbt` or `sbt.bat`

* In SBT, `container:start`

* Visit http://127.0.0.1:8080/ in your browser.


Console included
----------------

By default, this Lift application boots with the H2 database.  In development mode it enables a web-based SQL console for H2.  Access it at:  http://127.0.0.1:8080/console/

The parameters for the connection are:

* Driver class: org.h2Driver
* JDBC URL: jdbc:h2:mem:dbname;DB_CLOSE_DELAY=-1
* User Name: sa
* Password: (leave blank)



