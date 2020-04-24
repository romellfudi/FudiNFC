# Fudi NFC

[![Platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/romellfudi/FudiNFC/blob/master/LICENSE)
[![Bintray](https://img.shields.io/bintray/v/romllz489/maven/fudi-nfc.svg)](https://bintray.com/romllz489/maven/fudi-nfc) 
[![Jitpack](https://jitpack.io/v/romellfudi/FudiNFC.svg)](https://jitpack.io/#romellfudi/FudiNFC)
[![](https://img.shields.io/badge/language-ES-blue.svg)](./)
<!-- [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Fudi%20NFC-green.svg?style=flat )]( https://android-arsenal.com/details/1/? ) -->

### by Romell Domínguez
[![](snapshot/icono.png)](https://www.romellfudi.com/)

`latestVersion` is ![](https://img.shields.io/bintray/v/romllz489/maven/fudi-nfc.svg)

Add the following in your app's `build.gradle` file:

```groovy
repositories {
    jcenter()
}
dependencies {
    implementation 'com.romellfudi.fudinfc:fudi-nfc:${latestVersion}'
}
```

Add the following to your AndroidManifest.xml file :
```xml
<uses-permission android:name="android.permission.NFC" />
```

Now go to the created activity, and either

* Implement [FGD] yourself

```java
// write email
OpCallback { it.writeEmailToTagFromIntent(text, null, null, intent) }

// write sms
OpCallback { it.writeSmsToTagFromIntent(text, null, intent) }

// write geolocation - latitude & longitude
OpCallback { it.writeGeolocationToTagFromIntent(latitude, longitude, intent) } 

// write uri
OpCallback { it.writeUriToTagFromIntent(text, intent) }

// write phone contact
OpCallback { it.writeTelToTagFromIntent(text, intent) }

// write rolling-on bluetooth device
OpCallback { it.writeBluetoothAddressToTagFromIntent(text, intent) }
```

## Reading

Paste this in the activity if you're **extending our class** :

```kotlin
override fun onNewIntent(Intent intent) {
    super.onNewIntent(intent) 
    for (String message in getNfcMessages()) { 
        // message 
    }
}
```

* Otherwise :

```kotlin
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    val items: SparseArray<String> = NfcReadUtilityImpl().readFromTagWithSparseArray(intent)
    for (i in 0 until items.size()) {
        // items.valueAt(i) 
    }
}
```
* If you like the Map implementation more you might as well use :

```kotlin
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    for (message in NfcReadUtilityImpl().readFromTagWithMap(intent).values()) {
        // message
    }
}
```

* Now you're able to read the NFC Tags as long as the library supports the data in it when held to your phone!

## Write to a tag
* Let your activity implement `TaskCallback`:


```kotlin
fun onReturn(result: Boolean) {
    val message = if (result) "Success" else "Failed!"
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun onProgressUpdate(vararg booleans: Boolean) {
    Toast.makeText(this, if (booleans[0]) "We started writing" else "We could not write!", Toast.LENGTH_SHORT).show()
}

fun onError(e: Exception) {
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
}
```

* If you hold a tag against the phone and it is NFC Enabled, your implementation of the methods will be executed.

### License
```
Copyright 2020 Romell D.Z.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```