package com.github.parboiled1.grappa.matchers.join;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import org.parboiled.BaseParser;
import org.parboiled.JoinParser;
import org.parboiled.Rule;

import javax.annotation.Nonnull;

/**
 * Bootstrapping a {@link JoinMatcher}
 *
 * <p>An instance of this class is created by {@link JoinParser#join(Object)}.
 * </p>
 *
 * <p>Its two arguments are a {@link BaseParser} instance and the object to use
 * as a rule which will become the {@link JoinMatcher}'s "joining" rule; this
 * object is converted to a {@link Rule} using {@link
 * BaseParser#toRule(Object)}.</p>
 *
 * <p>Its only method, {@link #using(Object)}, produces a {@link
 * JoinMatcherBuilder}; the argument to this method (converted to a rule in a
 * similar fashion as the above) will become the "joined" rule of the produced
 * matcher.</p>
 *
 * @param <V> value type produced by the parser
 * @param <P> parser class
 *
 * @see JoinMatcherBuilder
 */
@Beta
public final class JoinMatcherBootstrap<V, P extends BaseParser<V>>
{
    private final P parser;
    private final Rule joined;

    @VisibleForTesting
    static <T, E extends BaseParser<T>> JoinMatcherBootstrap<T, E> create(
        final E parser, final Object joined)
    {
        return new JoinMatcherBootstrap<T, E>(parser, joined);
    }

    public JoinMatcherBootstrap(@Nonnull final P parser,
        @Nonnull final Object joined)
    {
        this.parser = Preconditions.checkNotNull(parser);
        this.joined = parser.toRule(joined);
    }

    public JoinMatcherBuilder using(@Nonnull final Object joining)
    {
        return new JoinMatcherBuilder(joined, parser.toRule(joining));
    }
}