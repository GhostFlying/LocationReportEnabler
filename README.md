LocationReportEnabler
=====================

A simple android app to enable Google Location Report, Google Now and related functions in china.

*ROOT REQUIRED*

The only thing the app does is set 3 variables about sim card once the device boot completed.

+ set gsm.sim.operator.numeric to 310004
+ set gsm.sim.operator.iso-country to us
+ set gsm.sim.operator.alpha to Verizon

Some other apps have the same function set the other 3 variables gsm.operator.numeric, gsm.operator.iso-country and gsm.operator.alpha. 
This will disable the loation correction of Google Maps so that it wil have a so-called Chinese error. So I write this.

Download: https://drive.google.com/file/d/0BxNnvIFWQpP2aGszc3lYRWRCSzg/view?usp=sharing

NOTICE：  
The size of the app is alomost the support lib, so you can remove it and complie a smaller one.
