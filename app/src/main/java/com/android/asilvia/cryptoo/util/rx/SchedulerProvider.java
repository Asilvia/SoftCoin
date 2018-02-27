package com.android.asilvia.softcoin.util.rx;

import rx.Scheduler;

/**
 * Created by asilvia on 26-10-2017.
 */

public interface SchedulerProvider {
    Scheduler ui();

    Scheduler computation();

    Scheduler io();
}
