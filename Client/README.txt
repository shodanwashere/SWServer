// HTTP Client -- How to Use //

This java program can take up to two arguments, minimum of one, and will change in behavior depending on the number of arguments

=> One argument (java Client.HTTPClient <filename>)
---------------------------------------------------

In the case you use only one argument, the program tries to contact a server on localhost, running on port 6789.
It then requests the file specified in <filename>. Note: the specified name should not have any extensions
(.html, .png, .jpg, etc).

=> Two arguments (java Client.HTTPClient <filename> <date_epoch>)
-----------------------------------------------------------------

The behavior with two arguments is very similar as with one, except for the fact that an If-Modified-Since
header is added to the HTTP request with a date specified as a UNIX epoch, specified in <date_epoch>. It
must be a numeric number and it must fit in a long integer, or 4 bytes of data.
