LocationReportEnabler
=====================

A simple android app to enable Google Location Report, Google Now and related functions in China.

*ROOT REQUIRED*

*I have found an issue about Google Maps and its satellite view, no matter which one is correct and the other must be wrong. If someone finds the way to solve it, please contact me or reply in issue #2*

The only thing the app does is set 3 variables about sim card once the device boot completed.

+ set gsm.sim.operator.numeric to 310004
+ set gsm.sim.operator.iso-country to us
+ set gsm.sim.operator.alpha to Verizon

Some other apps have the same function set the other 3 variables gsm.operator.numeric, gsm.operator.iso-country and gsm.operator.alpha. 
This will disable the loation correction of Google Maps so that it wil have a so-called Chinese error. So I write this.

Download: https://drive.google.com/file/d/0BxNnvIFWQpP2aGszc3lYRWRCSzg/view?usp=sharing

####Alternative version

*If you do not have problems using the first one, just skip this.*

For some users who use the locker of sim PIN or others, the origin one may change the setting a little early. And this is an alternative version changing it after the sim card is ready, this may should be tested for some time and need a more sensetive permission. 

Beside above, this version is only set the gms.sim.operator.numeric to 310030 for experiment. It showed the same function but have less side effect.

Download: https://drive.google.com/file/d/0BxNnvIFWQpP2SjhBRWRRSzFkQms/view?usp=sharing


NOTICE：  
The size of the app is alomost the support lib, so you can remove it and complie a smaller one.
