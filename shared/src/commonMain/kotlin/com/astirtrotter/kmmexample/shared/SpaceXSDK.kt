package com.astirtrotter.kmmexample.shared

import com.astirtrotter.kmmexample.shared.cache.Database
import com.astirtrotter.kmmexample.shared.cache.DatabaseDriverFactory
import com.astirtrotter.kmmexample.shared.entity.RocketLaunch
import com.astirtrotter.kmmexample.shared.network.SpaceXApi

class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = SpaceXApi()

    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}