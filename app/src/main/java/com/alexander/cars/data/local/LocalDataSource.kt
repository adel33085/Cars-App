package com.alexander.cars.data.local

import com.alexander.base.data.local.ISharedPreferences

class LocalDataSource constructor(
    override val sharedPreferences: ISharedPreferences
) : ILocalDataSource {
}
