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

package org.parboiled.matchers;

public final class MatcherUtils
{

    private MatcherUtils()
    {
    }

    // TODO: UGLY, REMOVE! .wrap() should be a Rule/Matcher-level method
    public static Matcher unwrap(final Matcher matcher)
    {
        if (matcher instanceof ProxyMatcher)
            return unwrap(ProxyMatcher.unwrap(matcher));
        if (matcher instanceof VarFramingMatcher)
            return unwrap(VarFramingMatcher.unwrap(matcher));
        if (matcher instanceof MemoMismatchesMatcher)
            return unwrap(MemoMismatchesMatcher.unwrap(matcher));
        return matcher;
    }
}
