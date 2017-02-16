LocationReportEnabler
=====================

A simple android app to enable Google Location Report, Google Now and related functions in China.

*ROOT REQUIRED*

*I have found an issue about Google Maps and its satellite view, no matter which one is correct and the other must be wrong. If someone finds the way to solve it, please contact me or reply in issue #2*

There are two version of this app, the geneeral one is for the users only rooted and the xposed one is for the users have xposed framwork.

In general, I suggest to use the xposed one, it do more clearly and stable.

#### General version

This version set the two variable once the device boot completed or the sim card is ready.

+ set gsm.sim.operator.numeric to 310030
+ set gsm.sim.operator.iso-country to us  (to enable the Google Maps' timeline)
+ after version 1.4.1, you can set it to any value you want (be caution to use it)

Download: https://github.com/GhostFlying/LocationReportEnabler/releases

#### Xposed version

As the alpha version of xposed for lollipop is released, we have a better way to unlock location report in China.

If you have already installed the new xposed framwork, you can easily install the below module to enable location report and do not effect any other apps. You can review the code in xposed branch.

Download: https://drive.google.com/file/d/0BxNnvIFWQpP2U2NSM3ptdjBnQkk/view?usp=sharing

Xposed Repo: http://repo.xposed.info/module/com.ghostflying.locationreportenabler

