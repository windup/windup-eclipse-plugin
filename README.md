windup-eclipse-plugin
=====================

Windup Eclipse Plugin

## Summary

The Windup Eclipse Project provides Eclipse plugins to integrate the Windup project with Eclipse.

## Installing

Before you can install the Windup plugin, you'll need to have [JBoss Tools](http://tools.jboss.org/downloads/) installed in Eclipse.

Building the project generates a zip archive containing an Eclipse update site under site/target/. You can install the features into Eclipse from the archive via "Help > Install New Software > Add... > Archive...".

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

This project depends on [jbosstools-base](https://github.com/jbosstools/jbosstools-base) and [jbosstools-forge](https://github.com/jbosstools/jbosstools-forge). 

The build will also pull in a snapshot build of [Windup](https://github.com/windup/windup).

This command will then run the build:

    $ mvn clean verify

Since we depend on a snapshot build of windup-distribution, you occasionally may need to run:

    $ mvn -U clean verify

If you just want to check if things compile/build you can run:

    $ mvn clean verify -DskipTests=true

But *do not* push changes without having the new and existing unit tests pass!

## Debugging

The Windup engine is resource intensive.  I found I have had to increase the memory settings when
launching Eclipse from the debugger to keep from running out of memory.

When debugging am currently using:
	
    -Xms1024m -Xmx1536m -XX:MaxPermSize=512M

These settings will need to be optimized at some point.

## Releasing

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
