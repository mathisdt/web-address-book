# Web Address Book

## Features
- manage families and their members
- provide family data (which is the same for all family members) _and_ individual data for any family member if applicable
- export a formatted PDF

## Technology
This is an [Echo3](http://echo.nextapp.com/site/echo3/)-based web application. It uses Spring Boot to provide a Tomcat instance.

Startable as Spring Boot application using org.zephyrsoft.wab.SpringBootStarter, optionally with the VM argument
`-Dspring.profiles.active=dev` if schema should be created and populated with example data.