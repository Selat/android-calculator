#include <string>
#include <cmath>

#include <jni.h>
#include "expression.h"

extern "C" JNIEXPORT jdouble JNICALL
Java_com_example_selat_androidcalculator_MainActivity_evalExpression(JNIEnv *env, jobject instance,
                                                                     jstring s_) {
    jboolean state;
    const char *s = env->GetStringUTFChars(s_, &state);
    double res = nan("");

    try {
        Expression e(s);
        res = e.eval();
    } catch (std::exception& e) {

    }

    env->ReleaseStringUTFChars(s_, s);

    return res;
}