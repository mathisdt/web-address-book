![license](https://img.shields.io/github/license/mathisdt/web-address-book.svg?style=flat) [![Travis-CI Build](https://img.shields.io/travis/mathisdt/web-address-book.svg?label=Travis-CI%20Build&style=flat)](https://travis-ci.org/mathisdt/web-address-book/) [![last released](https://img.shields.io/github/release-date/mathisdt/web-address-book.svg?label=last%20released&style=flat)](https://github.com/mathisdt/web-address-book/releases)

# Web Address Book

## Features

- manage families and their members
- provide family data (which is the same for all family members) _and_ individual data for any family member if applicable
- export a formatted PDF

## Technology

This is an [Echo3](http://echo.nextapp.com/site/echo3/)-based web application. It uses Spring Boot to provide a Tomcat instance.

## Getting started

- if you don't have it yet: download and install Java 8 (or later if you feel adventurous)
- download the [lastest release](https://github.com/mathisdt/web-address-book/releases/latest) and unpack it
  (use the jar, not the sources.jar)
- start the application using `java -jar YOUR-JAR-FILE.jar`, optionally with the VM argument
  `-Dspring.profiles.active=dev` if a schema should be created and populated with example data
