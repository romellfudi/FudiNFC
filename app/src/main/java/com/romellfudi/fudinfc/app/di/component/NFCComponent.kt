/*
 * Copyright (c) 2020. BoostTag E.I.R.L. Romell D.Z.
 * All rights reserved
 * porfile.romellfudi.com
 */

package com.romellfudi.fudinfc.app.di.component

import com.romellfudi.fudinfc.app.MainActivity
import com.romellfudi.fudinfc.app.di.NFCScope
import com.romellfudi.fudinfc.app.di.module.UIModule
import dagger.BindsInstance
import dagger.Component

/**
 * @autor Romell Domínguez
 * @date 2020-04-24
 * @version 1.0
 */
@NFCScope
@Component(modules = [UIModule::class])
interface NFCComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance mainActivity: MainActivity): NFCComponent
    }

    fun inject(mainActivity: MainActivity)

}