package com.neueda.minion.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

class ExecutorModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Named("scheduler.keepAlive")
    ScheduledExecutorService provideKeepAliveScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }

}