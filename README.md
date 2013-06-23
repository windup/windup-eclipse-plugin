windup-eclipse-plugin
=====================

Windup Eclipse Plugin

## Summary

The WindUp Eclipse Project provides Eclipse plugins to integrate the WindUp project with Eclipse.

## Notes
The Windup engine is resource intensive.  I found I had to increase the memory settings when
launching Eclipse to keep from running out of memory.

I when debugging am currently using:
-Xms1024m -Xmx2048m -XX:MaxPermSize=1024m

These settings will need to be optimized at some point.