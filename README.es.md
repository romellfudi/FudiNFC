# Fudi NFC

[![Platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/romellfudi/FudiNFC/blob/master/LICENSE)
[![Bintray](https://img.shields.io/bintray/v/romllz489/maven/fudi-nfc.svg)](https://bintray.com/romllz489/maven/fudi-nfc) 
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Fudi%20NFC-green.svg?style=flat )]( https://android-arsenal.com/details/1/? )
[![Jitpack](https://jitpack.io/v/romellfudi/FudiNFC.svg)](https://jitpack.io/#romellfudi/FudiNFC)
[![](https://img.shields.io/badge/language-ES-blue.svg)](./)

### by Romell Domínguez
[![](snapshot/icono.png)](https://www.romellfudi.com/)

`latestVersion` is ![](https://img.shields.io/bintray/v/romllz489/maven/fudi-nfc.svg)

Add the following in your app's `build.gradle` file:

```groovy
repositories {
    jcenter()
}
dependencies {
    implementation 'com.romellfudi.permission:fudi-nfc:${latestVersion}'
    implementation 'com.romellfudi.permission:fudi-nfc-kotlin:${latestVersion}'

}
```

Add the following to your AndroidManifest.xml file :
```xml
	 <uses-permission android:name="android.permission.NFC" />
```

Now go to the created activity, and either

* Implement [FGD] yourself

```java
... inner activity
	    private PendingIntent pendingIntent;
    	private IntentFilter[] mIntentFilters;
   		private String[][] mTechLists;
    	private NfcAdapter mNfcAdapter;

  	 	protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.activity_main);
        	mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        	pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        	mIntentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)};
        	mTechLists = new String[][]{new String[]{Ndef.class.getName()},
                    new String[]{NdefFormatable.class.getName()}};
                        	}
		public void onResume(){
			super.onResume();
    		if (mNfcAdapter != null) {
       			mNfcAdapter.enableForegroundDispatch(this, pendingIntent, mIntentFilters, mTechLists);
    		}
    	}
    	public void onPause(){
    		super.onPause();
    		if (mNfcAdapter != null)
        	{
            	mNfcAdapter.disableForegroundDispatch(this);
        	}
        }
		...

```

## Reading

Paste this in the activity if you're **extending our class** :


```java
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
    	for (String message : getNfcMessages()){
       		Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    	}
	}
```

* Otherwise :

```java
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SparseArray<String> res = new NfcReadUtilityImpl().readFromTagWithSparseArray(intent);
        for (int i =0; i < res.size() ; i++ ) {
            Toast.makeText(this, res.valueAt(i), Toast.LENGTH_SHORT).show();
        }
    }
```
* If you like the Map implementation more you might as well use :

```java
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        for (String message : new NfcReadUtilityImpl().readFromTagWithMap(intent).values()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
```

* Now you're able to read the NFC Tags as long as the library supports the data in it when held to your phone!

## Write to a tag
* Let your activity implement `TaskCallback`:


```java
    @Override
    public void  onReturn(Boolean result) {
        String message = result ? "Success" : "Failed!";
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(Boolean... booleans) {
        Toast.makeText(this, booleans[0] ? "We started writing" : "We could not write!",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }
```

Create a field with an `TaskCallback` in the following way :

```java
	TaskCallback mTaskCallback = new AsyncOperationCallback() {

        @Override
        public boolean performWrite(NfcWriteUtility writeUtility) throws ReadOnlyTagException, InsufficientCapacityException, TagNotPresentException, FormatException {
            return writeUtility.writeEmailToTagFromIntent("some@email.tld","Subject","Message",getIntent());
        }
    };
```

* Override the `onNewIntent(Intent)` method in the following way :

```java
	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    new WriteEmailNfcAsync(this,mAsyncOperationCallback).executeWriteOperation();
	}
```
* If you hold a tag against the phone and it is NFC Enabled, your implementation of the methods will be executed.

### License
```
Copyright 2019 Romell D.Z.

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