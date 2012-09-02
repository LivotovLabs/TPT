Vaadin Toolkit Productivity Tools
=================================

               


                TOOLKIT PRODUCTIVITY TOOLS
      
            version 2.0.0 , released 09.09.2012

   ( for release notes / what's changed please see below )





WHAT IS THIS
============
Toolkit Productivity Tools aka TPT is a small server-ide extension to the Vaadin framework and aims
to provide extended components, utilities and helpers that increases productivity when creating
Vaadin applications. TPT was born after I've managed to create several Vaadin-based production
applications and noticed that a number of small same things are commonly used in all apps
and can be extracted to a common library to boost the productivity and eliminate code duplicates.


DISCLAIMER
=============
TPT provided as is, in hope that somebody else will also find it useful or find some ideas or
examples for its own code.


LICENSE
=======
TPT is absolutely free and uses version 2 of Apache License, please see LICENSE.TXT for more details.


BUILDING
========
You'll need Java 1.6+ and Apache Ant 1.6+ to build the project. Just run "ant" from the project base
folder and it will compile and create tpt.jar in the "dist" folder.


BUG REPORTING AND OTHER CONTACTS
================================
TPT is hosted at GitHub, please check the homepage: https://github.com/livotov/TPT

- If you want to post a new bug / feature request - please create a ticket here: https://github.com/livotov/TPT/issues
- If you want to share something or need more documentation - take a look at wiki: https://github.com/livotov/TPT/wiki
- If you want the latest source - download pre-built binaries or clone the sources from repo.
- You are also welcome to join the project.



CHANGES HISTORY
===============

Legend:

+ - New feature / improvement
* - Bugfix
? - Information


09.09.2012 v.2.0.0

A very long awaited release. Sorry, too many things and activities happened with me past year,
so TPT support was a "bit" slow :) Hopefully we now return to normal mode :))

? - Aligned with the latest Vaadin release 6.8.2
? - Repackaged source tree to match new structure
? - Finally moved project back to GitHub
* - Fixed all GoogleCode reported issues: #14, #16, #17, #19, #20



31.01.2011 v.1.2.0

? - PDF Viewer removed from tpt project and moved to a separate add-on VPDF
? - Aligned with Vaadin 6.5.0


26.04.2010 v.1.1.1

* - Fixed issues: #4, #5, #6
+ - Removed optional jar files from IcePDF OpenSource edition bundled package to reduce distribution size.
+ - DocumentViewer has not the IcePDF deps protection code - if IcePDF files are not present in the classpath, it will render an appropriate message instead of raising NoClassDefFound error.
+ - Dependency jar files are also copied to distribution "deps" folder to be more visible.
+ - i18n translation files should be plain text utf-8 files now - no native2ascii required anymore (according to issue #6)
? - Added dependencies section into the developer handbook to more transparetly describe the external dependencies.


18.04.2010 v.1.1.0

+ - TPTMessagePanel utility widget
+ - PDF Document Viewer widget + framework
+ - Simplified i18n message translation, support for in-place output formatting
* - fixed bug #4
* - overall code cleanups
? - aligned with Vaadin 6.3.0

IMPORTANT: PDF Document Viewer depends on IcePDF OpenSource edition (included)
which is used to render PDF documents at the server side. If you're going to use
DocumentViewer component, do not forget to iclude IcePDF jars to the classpath as well.





17.12.2009 v.1.0.0

* - Numerous small bugfixes
? - Added developer handbook
+ - Added new demo application


09.11.2009 v.1.0.0 RC1

? - Finally, a release candidate of first public version of TPT
? - Added user manual in addition to JavaDoc
? - Removed unnecessary code and refactored packages and clases locations a bit
* - Fixed bug in an application thread helper, which was causing problems for passing currern application instance into ThreadLocal
* - Removed explicit dependency on HttpServlet in a transaction listener implementation of a TPTApplication
+ - Upgraded to Vaadin 6.1.4 release

31.05.2009 v.0.0.3

? - Project is now published at Google Code: http://code.google.com/p/tpt
* - Fixed bug with duplicated views, when NPE error occurs during switching.
+ - Upgraded to Vaadin 6.0.0 release
+ - TPTMultiView now incorporates URIFragmentUtility of Vaadin, in order to automate view switching
    from an URL. By default this function is disabled, you'll have to construct the TPTMultiView
    with a new constructor, that enables fragment utility. See JavaDoc for more info.
+ - TPTMultiView now passes previous view name to the view being activated, which can be useful
    for historical navigation purposes.
+ - TPTMultiView now has a lazy view feature - you may now register view class instead of instance as a view. 
    If class is registered, it will be instantiated later automatically, when view will be 
    activated (switched) for the first time.

15.03.2009 v.0.0.2

+ Upgraded to final 5.3.0 release of ITMill Toolkit.
+ TPTCaptcha component for representing a captcha image in the UI.
+ RandomPasswordGenerator component, mainly used for TPTCaptcha but can be also used as standalone utility class.
+ TPTMultiView component for managing multiple UI views.
* Cosmetic changes in demo application.


19.02.2009 v.0.0.1

? Sources separated into a standalone library and moved to contrib section.

