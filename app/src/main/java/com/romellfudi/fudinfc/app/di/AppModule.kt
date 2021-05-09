package com.romellfudi.fudinfc.app.di

import android.app.Activity
import android.app.ProgressDialog
import android.widget.Toast
import com.romellfudi.fudinfc.gear.interfaces.TaskCallback
import com.romellfudi.fudinfc.util.interfaces.NfcReadUtility
import com.romellfudi.fudinfc.util.sync.NfcReadUtilityImpl
import org.koin.dsl.module
import timber.log.Timber

/**
 * Android koin module
 *
 * @version 1.0.a
 * @autor Romell Domínguez (@romellfudi)
 * @date 5/9/21
 */
val moduleNFC = module {

    factory { (activity: Activity) -> ProgressDialog(activity) }

    single<NfcReadUtility> { NfcReadUtilityImpl() }

    fun provideTaskCallback(mainActivity: Activity, mProgressDialog: ProgressDialog) =
            object : TaskCallback {
                override fun onReturn(result: Boolean) {
                    Timber.d("Received our result : $result")
                    if (mProgressDialog.isShowing) mProgressDialog.dismiss()
                    if (result) Toast.makeText(mainActivity, "Write has been done!", Toast.LENGTH_SHORT).show()
                }

                override fun onProgressUpdate(vararg values: Boolean?) {
                    Timber.i("Trying to write!")
                    if (values.isNotEmpty() && values[0]!!) {
                        mProgressDialog.setMessage("Writing")
                        Timber.i("Writing!!!")
                    }
                }

                override fun onError(e: Exception) {
                    Timber.e("Encountered an error!!")
                    if (mProgressDialog.isShowing)
                        mProgressDialog.dismiss()
                    Toast.makeText(mainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

    single<TaskCallback> { provideTaskCallback(get(), get()) }

}