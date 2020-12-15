/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.app.di.module

import android.app.ProgressDialog
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.rbddevs.splashy.Splashy
import com.romellfudi.fudinfc.app.BuildConfig
import com.romellfudi.fudinfc.app.MainActivity
import com.romellfudi.fudinfc.app.R
import com.romellfudi.fudinfc.app.di.NFCScope
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback
import com.romellfudi.fudinfc.util.interfaces.NfcReadUtility
import com.romellfudi.fudinfc.util.sync.NfcReadUtilityImpl
import dagger.Module
import dagger.Provides

val TAG: String? = MainActivity::class.java.simpleName

@Module
class UIModule {

    @Provides
    @NFCScope
    fun provideSplashy(mainActivity: MainActivity) =
            Splashy(mainActivity)
                    .setLogo(R.drawable.combine)
                    .setLogoScaleType(ImageView.ScaleType.FIT_CENTER)
                    .setAnimation(Splashy.Animation.GROW_LOGO_FROM_CENTER, 1000)
                    .setTitle(R.string.app_name)
                    .setSubTitle("Version  " + BuildConfig.VERSION_NAME)
                    .setTitleColor(R.color.colorAccent)
                    .setProgressColor(R.color.colorPrimary)
                    .setBackgroundResource(R.color.colorPrimaryDark)
                    .setTime(2000)


    @Provides
    @NFCScope
    fun provideProgressDialog(mainActivity: MainActivity) =
            ProgressDialog(mainActivity)

    @Provides
    @NFCScope
    fun provideNfcReadUtility(): NfcReadUtility = NfcReadUtilityImpl()

    @Provides
    @NFCScope
    fun provideTaskCallback(mainActivity: MainActivity, mProgressDialog: ProgressDialog) =
            object : TaskCallback {
                override fun onReturn(result: Boolean) {
                    if (mProgressDialog.isShowing) mProgressDialog.dismiss()
                    if (result)
                        Toast.makeText(mainActivity, "Write has been done!", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Received our result : $result")
                }

                override fun onProgressUpdate(vararg values: Boolean?) {
                    if (values.isNotEmpty() && values[0]!!) {
                        mProgressDialog.setMessage("Writing")
                        Log.d(TAG, "Writing !")
                    }
                }

                override fun onError(e: Exception) {
                    if (mProgressDialog.isShowing) mProgressDialog.dismiss()
                    Log.i(TAG, "Encountered an error !", e)
                    Toast.makeText(mainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
}