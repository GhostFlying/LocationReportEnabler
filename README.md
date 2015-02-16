LocationReportEnabler
=====================

A simple android app to enable Google Location Report, Google Now and related functions in China.

*ROOT REQUIRED*

*I have found an issue about Google Maps and its satellite view, no matter which one is correct and the other must be wrong. If someone finds the way to solve it, please contact me or reply in issue #2*

There are two version of this app, difference is showed below.  

The full version changed 3 variables about sim card once the device boot completed.

+ set gsm.sim.operator.numeric to 310004 or 310030
+ set gsm.sim.operator.iso-country to us
+ set gsm.sim.operator.alpha to Verizon

The tiny version only changed the gms.sim.operator.numeric to 310030 once  the sim card is ready.

Some other apps have the same function set the other 3 variables gsm.operator.numeric, gsm.operator.iso-country and gsm.operator.alpha. 
This will disable the loation correction of Google Maps so that it wil have a so-called Chinese error. So I write this.

*How to choose*

If you do not need to unlock Google Play as your account is unlocked, just using the tiny version, it is more reliable and more clean.

If you need to unlock Google Play, you may try the full version, it set more variables and it may unlock the Google Play.

In my opinion, you should always try the tiny version first, it seems more reliable.

####Download

Tiny version: https://drive.google.com/file/d/0BxNnvIFWQpP2SjhBRWRRSzFkQms/view?usp=sharing

Full version: https://drive.google.com/file/d/0BxNnvIFWQpP2aGszc3lYRWRCSzg/view?usp=sharing

NOTICE：  
The size of the app is alomost the support lib, so you can remove it and complie a smaller one.

####Xposed Experiment 

As the alpha version of xposed for lollipop is released, we have a better way to unlock location report in China.

If you have already installed the new xposed framwork, you can easily install the below module to enable location report and do not effect any other apps. You can review the code in xposed branch.

Xposed version: https://drive.google.com/file/d/0BxNnvIFWQpP2U2NSM3ptdjBnQkk/view?usp=sharing
