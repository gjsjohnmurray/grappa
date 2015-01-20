/*
 * Copyright (C) 2009-2011 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fge.grappa.matchers.predicates;

import com.github.fge.grappa.matchers.MatcherType;
import com.github.fge.grappa.matchers.base.CustomDefaultLabelMatcher;
import com.github.fge.grappa.matchers.base.Matcher;
import com.google.common.base.Preconditions;
import org.parboiled.MatcherContext;
import org.parboiled.Rule;

/**
 * A special {@link Matcher} not actually matching any input but rather trying its submatcher against the current input
 * position. Succeeds if the submatcher would fail.
 */
public final class TestNotMatcher
    extends CustomDefaultLabelMatcher<TestNotMatcher>
{
    private final Matcher subMatcher;

    public TestNotMatcher(final Rule subRule)
    {
        super(Preconditions.checkNotNull(subRule, "subRule"), "testNot");
        subMatcher = getChildren().get(0);
    }

    public Matcher getSubMatcher()
    {
        return subMatcher;
    }

    @Override
    public MatcherType getType()
    {
        return MatcherType.PREDICATE;
    }

    @Override
    public <V> boolean match(final MatcherContext<V> context)
    {
        final int lastIndex = context.getCurrentIndex();
        final Object valueStackSnapshot
            = context.getValueStack().takeSnapshot();

        if (subMatcher.getSubContext(context).runMatcher())
            return false;

        // reset location, Test matchers never advance
        context.setCurrentIndex(lastIndex);

        // erase all value stack changes the the submatcher could have made
        context.getValueStack().restoreSnapshot(valueStackSnapshot);
        return true;
    }
}
