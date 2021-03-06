package com.github.simonpercic.aircycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bind an Activity lifecycle callback method to a listener.
 * <p>
 * Supported lifecycle callbacks are:
 * - onCreate() (with optional Bundle savedInstanceState argument)
 * - onStart()
 * - onResume()
 * - onPause()
 * - onStop()
 * - onSaveInstanceState() (with optional Bundle outState argument)
 * - onDestroy()
 * <p>
 * The annotation processor will generate a binding class for the Activity, named `YourActivity`AirCycle with a
 * static `bind` method.
 * You MUST call `YourActivityAirCycle.bind(this)` BEFORE calling `super.onCreate(savedInstanceState)`,
 * otherwise the bound listener will NOT receive the first onCreate call for the Activity.
 * <p>
 * Valid lifecycle method names are:
 * onCreate() / onActivityCreated(),
 * onStart() / onActivityStarted(),
 * onResume() / onActivityResumed(),
 * onPause() / onActivityPaused(),
 * onStop() / onActivityStopped(),
 * onSaveInstanceState() / onActivitySaveInstanceState(),
 * onDestroy() / onActivityDestroyed()
 * <p>
 * See https://github.com/simonpercic/AirCycle for more info.
 *
 * @author Simon Percic <a href="https://github.com/simonpercic">https://github.com/simonpercic</a>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = ElementType.FIELD)
public @interface AirCycle {

    /**
     * Ignore Activity lifecycle callbacks when generating binding code.
     * <p>
     * Valid values are:
     * ActivityLifecycle.CREATE (1),
     * ActivityLifecycle.START (2),
     * ActivityLifecycle.RESUME (3),
     * ActivityLifecycle.PAUSE (4),
     * ActivityLifecycle.STOP (5),
     * ActivityLifecycle.SAVE_INSTANCE_STATE (6) or
     * ActivityLifecycle.DESTROY (7).
     *
     * @return array of ints representing lifecycle callback values to ignore when generating binding code.
     */
    @ActivityLifecycle.ActivityLifecycleEvent int[] ignore() default {};
}
