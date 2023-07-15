windup-eclipse-plugin
=====================

Migration Toolkit for Applications (MTA) Eclipse Plugin

## Summary

[Windup](https://github.com/windup/windup) is a command-line Application Migration and Modernization assessment tool.

Provides Eclipse integration with the Migration Toolkit for Applications (MTA) project.

## Installing

Before you can install the Windup plugin, you'll need to have Eclipse installed.

Building the project generates a zip archive containing an Eclipse update site under site/target/. You can install the features into Eclipse from the archive via "Help > Install New Software > Add... > Archive...".

Also, a [nightly build](https://download.jboss.org/jbosstools/photon/snapshots/builds/windup-eclipse-plugin-NIGHTLY/latest/all/repo/) is available.

### Offline

#### Bring your own Eclipse

* Download a distribution of the [Eclipse IDE 2019-09 (4.12.0)](https://www.eclipse.org/downloads/).
* Download the latest release of the Red Hat CodeReady Studio [update-site 12.13.0.GA](https://tools.jboss.org/downloads/devstudio/2019-09/12.13.0.GA.html#zips).

#### Red Hat CodeReady Studio

* Download and install Red Hat CodeReady Studio [installer 12.13.0.GA](https://tools.jboss.org/downloads/devstudio/2019-09/12.13.0.GA.html#direct_download)


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

## Setup your environment

Setup your local environment with Maven 3.2.x+. See the [Maven local settings.xml](https://developer.jboss.org/wiki/MavenGettingStarted-Developers) for set up of jboss.org repository.

## Building

This project depends on [jbosstools-base](https://github.com/jbosstools/jbosstools-base) and [jbosstools-forge](https://github.com/jbosstools/jbosstools-forge). The build will also pull in a snapshot build of [Windup](https://github.com/windup/windup). To avoid needing to build the jbosstools projects before building windup-eclipse-plugin,
be sure you have added the JBoss repositories to your Maven settings as shown [here](https://raw.githubusercontent.com/windup/windup/master/build/settings.xml).

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
