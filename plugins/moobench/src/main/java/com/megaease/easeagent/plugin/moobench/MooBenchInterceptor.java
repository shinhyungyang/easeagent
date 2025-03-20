/*
 * Copyright (c) 2021, MegaEase
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.megaease.easeagent.plugin.moobench;

import com.megaease.easeagent.plugin.annotation.AdviceTo;
import com.megaease.easeagent.plugin.api.Context;
import com.megaease.easeagent.plugin.api.trace.Span;
import com.megaease.easeagent.plugin.enums.Order;
import com.megaease.easeagent.plugin.interceptor.Interceptor;
import com.megaease.easeagent.plugin.interceptor.MethodInfo;
import com.megaease.easeagent.plugin.bridge.EaseAgent;
import com.megaease.easeagent.plugin.api.logging.Logger;

@AdviceTo(value = GameCanvasAdvice.class)
public class MooBenchInterceptor implements Interceptor {
    private static final Object SPAN_KEY = new Object();
    private static final Logger log = EaseAgent.getLogger(MooBenchInterceptor.class);

    @Override
    public void before(MethodInfo methodInfo, Context context) {
        log.info("started ---");
        Object invoker = methodInfo.getInvoker();
        String spanName = invoker.getClass().getSimpleName() + "." + methodInfo.getMethod();
        Span span = context.nextSpan().remoteServiceName("mocha-doom").name(spanName).start();
        log.info("   traceId: " + span.traceIdString());
        log.info("    spanId: " + span.spanIdString());
        log.info("  parentId: " + span.parentIdString());
        context.put(SPAN_KEY, span);
        log.info("ended ---");
    }

    @Override
    public void after(MethodInfo methodInfo, Context context) {
        log.info("started ---");
        try {
            Span span = context.get(SPAN_KEY);
            if (span == null) {
                log.warn("  span is null");
                log.info("ended ---");
                return;
            }
            Throwable throwable = methodInfo.getThrowable();
            if (throwable != null) {
                log.error("  span has error");
                span.error(throwable);
            }
            log.info("   traceId: " + span.traceIdString());
            log.info("    spanId: " + span.spanIdString());
            log.info("  parentId: " + span.parentIdString());
            span.finish();
            context.remove(SPAN_KEY);
        } catch (Exception ignored) {
        }
        log.info("ended ---");
    }

    @Override
    public String getType() {
        return Order.TRACING.getName();
    }

    @Override
    public int order() {
        return Order.TRACING.getOrder();
    }
}
