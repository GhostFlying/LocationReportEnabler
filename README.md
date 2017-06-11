LocationReportEnabler
=====================

A simple android app which enables Google Location Report, Google Now and related functions in China.

*ROOT REQUIRED*

There are two variants of this app, the general one is for the users only rooted and the xposed one is for the users have xposed framwork.

In general, I suggest to use the xposed one, which is more clear and stable.

#### General variant

This version sets two variables once the device boot completes or the sim card is ready.

+ set gsm.sim.operator.numeric to 310030
+ set gsm.sim.operator.iso-country to us  (to enable the Google Maps' timeline)
+ after version 1.4.1, you can set it to any value you want (be cautious to use it)

Download: https://github.com/GhostFlying/LocationReportEnabler/releases

#### Xposed variant

To make the project structure clean, all code and releases of the xposed version are moved to https://github.com/GhostFlying/LocationReportEnablerXposed

You can find apks at https://github.com/GhostFlying/LocationReportEnablerXposed/releases or xposed repo.
