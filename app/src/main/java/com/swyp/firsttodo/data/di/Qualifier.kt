package com.swyp.firsttodo.data.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WithToken

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WithoutToken
