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

import com.megaease.easeagent.plugin.Points;
import com.megaease.easeagent.plugin.matcher.IClassMatcher;
import com.megaease.easeagent.plugin.matcher.ClassMatcher;
import com.megaease.easeagent.plugin.matcher.IMethodMatcher;
import com.megaease.easeagent.plugin.matcher.MethodMatcher;
import com.megaease.easeagent.plugin.tools.matcher.MethodMatcherUtils;
import com.megaease.easeagent.plugin.bridge.EaseAgent;
import com.megaease.easeagent.plugin.api.logging.Logger;

import java.util.Set;

import static com.megaease.easeagent.plugin.tools.matcher.ClassMatcherUtils.name;

public class GameCanvasAdvice implements Points {
    private static final Logger log = EaseAgent.getLogger(GameCanvasAdvice.class);

    @Override
    public IClassMatcher getClassMatcher() {
        log.info("getClassMatcher() started");
        return ClassMatcher.builder()
            .hasClassName("awt.EventObserver")
            .build();
    }

    @Override
    public Set<IMethodMatcher> getMethodMatcher() {
        log.info("getMethodMatcher() started");
        return MethodMatcher.multiBuilder()
            .match(MethodMatcher.builder().named("observe")
                .arg(0, "java.awt.AWTEvent")
                .returnType("void")
                .build())
            .match(MethodMatcher.builder().named("sendKeyUps")
                .arg(0, "java.awt.AWTEvent")
                .returnType("void")
                .build())
            .match(MethodMatcher.builder().named("sendKeyDowns")
                .arg(0, "java.awt.AWTEvent")
                .returnType("void")
                .build())
            .build();
    }
}
