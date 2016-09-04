package com.github.simonpercic.aircycle.manager;

import com.github.simonpercic.aircycle.model.LifecycleMethod;
import com.github.simonpercic.aircycle.model.type.ActivityLifecycle;
import com.github.simonpercic.aircycle.utils.ElementValidator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * @author Simon Percic <a href="https://github.com/simonpercic">https://github.com/simonpercic</a>
 */
@Singleton
public class MethodParser {

    private final ProcessorLogger logger;
    private final Elements elementUtils;

    @Inject
    public MethodParser(ProcessorLogger logger, Elements elementUtils) {
        this.logger = logger;
        this.elementUtils = elementUtils;
    }

    public List<LifecycleMethod> parseLifecycleMethods(TypeElement element) {
        List<LifecycleMethod> lifecycleMethods = new ArrayList<>();

        List<? extends Element> allMembers = elementUtils.getAllMembers(element);

        for (Element member : allMembers) {
            if (member.getKind() != ElementKind.METHOD) {
                continue;
            }

            ExecutableElement method = (ExecutableElement) member;

            if (!isLifecycleMethod(method)) {
                continue;
            }

            LifecycleMethod lifecycleMethod = parseLifecycleMethod(method, element);
            if (lifecycleMethod == null) {
                return null;
            }

            lifecycleMethods.add(lifecycleMethod);
        }

        if (lifecycleMethods.size() == 0) {
            // TODO: 04/09/16 detailed error message
            String message = String.format("`%s` does not contain any Activity lifecycle methods.",
                    element.getQualifiedName());

            logger.w(message, element);
        }

        return lifecycleMethods;
    }

    private LifecycleMethod parseLifecycleMethod(ExecutableElement method, TypeElement enclosingElement) {
        if (method == null) {
            throw new IllegalArgumentException("method is null");
        }

        String methodName = method.getSimpleName().toString();
        ActivityLifecycle activityLifecycle = parseLifecycle(methodName);

        if (activityLifecycle == null) {
            return null;
        }

        if (!ElementValidator.isPublic(method)) {
            String message = String.format("Method `%s` in `%s` is not public.",
                    methodName,
                    enclosingElement.getQualifiedName());

            logger.e(message, enclosingElement);
            return null;
        }

        if (method.isVarArgs()) {
            String message = String.format("Method `%s` in `%s` accepts a variable number of arguments.",
                    methodName,
                    enclosingElement.getQualifiedName());

            logger.e(message, enclosingElement);
            return null;
        }

        List<? extends VariableElement> parameters = method.getParameters();

        // TODO: 04/09/16 parse params

        LifecycleMethod.Builder builder = LifecycleMethod.builder(activityLifecycle, methodName);

        return builder.build();
    }

    private static boolean isLifecycleMethod(ExecutableElement method) {
        if (method == null) {
            throw new IllegalArgumentException("method is null");
        }

        ActivityLifecycle activityLifecycle = parseLifecycle(method.getSimpleName().toString());
        return activityLifecycle != null;
    }

    private static ActivityLifecycle parseLifecycle(String methodName) {
        switch (methodName) {
            case "onCreate":
            case "onActivityCreated":
                return ActivityLifecycle.CREATE;
            default:
                return null;
        }
    }
}