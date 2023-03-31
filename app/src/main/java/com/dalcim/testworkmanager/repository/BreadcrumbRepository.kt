package com.dalcim.testworkmanager.repository

import android.content.Context
import com.dalcim.testworkmanager.database.TestWorkManagerDatabase
import com.dalcim.testworkmanager.database.domain.BreadcrumbEntity
import com.dalcim.testworkmanager.domain.Breadcrumb

class BreadcrumbRepository (private val context: Context) {

    private val testWorkManagerDatabase by lazy { TestWorkManagerDatabase.buildDatabase(context) }
    private val breadcrumbDao by lazy { testWorkManagerDatabase.breadcrumbDao() }

    fun addBreadcrumb(breadcrumb: Breadcrumb) : Boolean {
        return breadcrumbDao.insert(
            BreadcrumbEntity.parse(breadcrumb)
        ) > 0
    }

    fun getBreadcrumbs() : List<Breadcrumb> {
        return breadcrumbDao.all().map {
            BreadcrumbEntity.parse(it)
        }
    }
}