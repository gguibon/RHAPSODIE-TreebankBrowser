RHAPSODIE-TreebankBrowser
===============

Treebank Browser is a graphical tool which allows to browse treebanks graphically. It was initially made for the [RHAPSODIE](http://www.projet-rhapsodie.fr/) projet purposes. Its main assets are :
- create multiple projects with metada, licence information, project descriptions, etc. and save them.
- generate server ready web interface from your project
- no need to change your data ! With [Arborator's engine](http://arborator.ilpga.fr/), it reads most common treebank data and even oral speeches treebanks !
- Fast interface
- etc.


The actual version is 0.8.0. 

The manager UI is an evolution of [this tutorial from code.makery](http://code.makery.ch/library/javafx-8-tutorial/ "code.makery tutorial") with several additionnal features.


# Build

This repo is built using the build.fxbuild file. You can build by importing the project in eclipse and build it, by directly executing the .fxbuild script.
The tutorial (link above) shows you an easy way to build its the "deployment" section.

# Usage

There are two executable files in the root directory:
- TreebankBrowser-{version}.jar --> java executable file
- TreebankBrowser-{version}.dmg --> MAC installation image
- TreebankBrowser-{version}.exe --> Windows executable file
- TreebankBrowser-{version}.deb --> Debian (linux) installation package

Double click on Jar or type :

```
java -Xmx[nb RAM]G -jar TreebankBrowser-{version}.jar
```

# Contacts

gael dot guibon at gmail.com
gael dot guibon at lsis.org

@2016 ANR Rhapsodie 07 CORP 030 01 LPP-CNRS LSIS-CNRS
=======
