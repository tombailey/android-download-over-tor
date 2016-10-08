# android-download-over-tor
Please be aware that some network operators don't like you using TOR on their network. If you are using the internet at a school, college, university, library, place of work, etc then you should make sure you aren't about to get into a lot of trouble by using TOR, no matter how innocent your activities are!

## What is this?
This is a Proof of Concept (PoC) for sending HTTP(S) traffic over the TOR network.

## What dependencies are there?
This project mainly leans on the thaliproject/Tor_Onion_Proxy_Library which leans on various other projects. You should go over there to figure out more about what they have done, their dependencies, etc.

## How do I get this to run on my machine?
Follow the instructions that thaliproject/Tor_Onion_Proxy_Library provide for Android in their README.md to create a local maven repo with  com.msopentech.thali:ThaliOnionProxyAndroid:0.0.2

Everything else should work straight out of the box. Just import into Android Studio and run it.

## What is the license?
This project leans on thaliproject/Tor_Onion_Proxy_Library which leans on various other projects like guardianproject/Orbot which also leans on various other projects. The licensing gets messy so please consult ./LINCENSE for the full details.
