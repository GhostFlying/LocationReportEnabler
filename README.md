LocationReportEnabler
=====================

A simple android app to enable Google Location Report, Google Now and related functions in China.

*ROOT REQUIRED*

There are two version of this app, the geneeral one is for the users only rooted and the xposed one is for the users have xposed framwork.

In general, I suggest to use the xposed one, it do more clearly and stable.

#### General version

This version set the two variable once the device boot completed or the sim card is ready.

+ set gsm.sim.operator.numeric to 310030
+ set gsm.sim.operator.iso-country to us  (to enable the Google Maps' timeline)
+ after version 1.4.1, you can set it to any value you want (be caution to use it)

Download: https://github.com/GhostFlying/LocationReportEnabler/releases

#### Xposed version

To make project structure clean, all code and release abount xposed version is moved to https://github.com/GhostFlying/LocationReportEnablerXposed

You can find apk at https://github.com/GhostFlying/LocationReportEnablerXposed/releases or xposed repo.
