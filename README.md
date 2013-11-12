windup-eclipse-plugin
=====================

Windup Eclipse Plugin

## Summary

The Windup Eclipse Project provides Eclipse plugins to integrate the Windup project with Eclipse.

## Installing

Right now the only way to install the plugin is to get the code, compile it, and then drop the built plugin into the dropins folder of your Eclipse install.

## Get the code

The easiest way to get started with the code is to [create your own fork](http://help.github.com/forking/), 
and then clone your fork:

    $ git clone git@github.com:<you>/windup-eclipse-plugin.git
    $ cd windup-eclipse-plugin
    $ git remote add upstream git://github.com/windup/windup-eclipse-plugin.git
	
At any time, you can pull changes from the upstream and merge them onto your master:

    $ git checkout master               # switches to the 'master' branch
    $ git pull upstream master          # fetches all 'upstream' changes and merges 'upstream/master' onto your 'master' branch
    $ git push origin                   # pushes all the updates to your fork, which should be in-sync with 'upstream'

The general idea is to keep your 'master' branch in-sync with the
'upstream/master'.

## Building

This project has a dependency on the windup project which does not have an official release in any maven repository yet.
Therefor the only way to build this project is to first download and build the [windup project](https://github.com/windup/windup).

The tests of this project require the org.jboss.tools.tests project. The recommended way to deal with this is
to clone https://github.com/jbosstools/jbosstools-base locally and then import the org.jboss.tools.tests project into your Eclipse workspace.

This command will then run the build:

    $ mvn clean verify

If you just want to check if things compiles/builds you can run:

    $ mvn clean verify -DskipTest=true

But *do not* push changes without having the new and existing unit tests pass!

## Debuging
The Windup engine is resource intensive.  I found I have had to increase the memory settings when
launching Eclipse from the debugger to keep from running out of memory.

When debugging am currently using:
	
    -Xms1024m -Xmx1536m -XX:MaxPermSize=512M

These settings will need to be optimized at some point.

## Releaseing

These are the steps to release a new version of the plugin

1. Update the version of the plugin to the release version
```
    $ mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=2.0.0
```
2. Build the plugin
```
    $ mvn clean verify
```
3. Perform an installation test of the plugin
4. Test the plugin UI contributions
5. Commit the release version change
6. Create a new Github release https://github.com/windup/windup-eclipse-plugin/releases/new
7. Update the version of the plugin to the next SNAPSHOT
```
    $ mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=2.1.0-SNAPSHOT
```
8. Commit the snapshot version change
9. Continue development on next release
