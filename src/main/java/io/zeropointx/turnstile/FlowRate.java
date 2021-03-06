/*==============================================================================
 =
 = Copyright 2018: darkware.org
 =
 =    Licensed under the Apache License, Version 2.0 (the "License");
 =    you may not use this file except in compliance with the License.
 =    You may obtain a copy of the License at
 =
 =        http://www.apache.org/licenses/LICENSE-2.0
 =
 =    Unless required by applicable law or agreed to in writing, software
 =    distributed under the License is distributed on an "AS IS" BASIS,
 =    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 =    See the License for the specific language governing permissions and
 =    limitations under the License.
 =
 =============================================================================*/

package io.zeropointx.turnstile;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link FlowRate} measures a rate that a number of discrete events are allowed to occur.
 * <p>
 * Somewhat confoundingly, though we just claimed this is talking about discrete events, the number of these
 * allegedly discrete events is allowed to be an annoyingly non-discrete floating point value. This allows you a
 * bit more flexibility in how you declare rates. For instance, you might
 * <p>
 * Furthering the confusion, the number of time units in the rate &mdash;a value that is actually modeling a
 * fluid, non-discrete phenomena&mdash; is required to be an integer (a {@code long}, technically). This is
 * required in order to use the very helpful JDK 8+ {@link Duration} object. The world is an unfair place.
 *
 * <h3>Parse Format</h3>
 *
 * {@link FlowRate}s can be created by parsing
 *
 * @author jeff@darkware.org
 * @since 2017-04-30
 */
public class FlowRate implements Comparable<FlowRate>
{
    private static final Pattern FLOWRATE_PATTERN = Pattern.compile("(\\d+(\\.\\d+)?)([A-Za-z]*)/(\\d*)([a-z]+)");

    private final double volume;
    private final Duration duration;
    private final double volumePerSecond;

    /**
     * Create a new {@link FlowRate} with the explicit rate.
     *
     * @param volume The number of events to allow per duration.
     * @param duration The {@link Duration} to allow the volume of events to pass.
     */
    public FlowRate(final double volume, final Duration duration)
    {
        super();

        this.volume = volume;
        this.duration = duration;

        this.volumePerSecond = (volume * 1000) / (double)duration.toMillis();
    }

    /**
     * Create a new {@link FlowRate} by parsing some well-formatted text.
     *
     * @param text The text to parse.
     */
    public FlowRate(final CharSequence text)
    {
        super();

        Matcher parser = FlowRate.FLOWRATE_PATTERN.matcher(text);
        if (parser.matches())
        {
            SimpleFactor factor = SimpleFactor.parse(parser.group(3));
            this.volume = factor.apply(Double.parseDouble(parser.group(1)));
            final String unitCountText = parser.group(4);
            final long unitCount = (unitCountText.length() > 0) ? Long.parseLong(unitCountText) : 1L;
            final ChronoUnit unit = Optional.ofNullable(FlowRateUnit.valueOf(parser.group(5).toUpperCase()))
                                            .orElseThrow(() -> new IllegalArgumentException("Could not parse time unit."))
                                            .getChronoUnit();

            Preconditions.checkArgument(unitCount > 0, "Rate time units must be positive.");
            Preconditions.checkArgument(this.volume > 0, "Rate volume must be positive.");

            this.duration = Duration.of(unitCount, unit);

            this.volumePerSecond = (volume * 1000) / (double)duration.toMillis();
        }
        else
        {
            throw new IllegalArgumentException("Could not parse a flow rate from input: " + text);
        }
    }

    /**
     * Fetch the flow volume per duration for this rate.
     *
     * @return The volume as a {@code double}.
     */
    public double getVolume()
    {
        return this.volume;
    }

    /**
     * Fetch the duration unit for this rate.
     *
     * @return A {@link Duration} describing the time component for this rate.
     */
    public Duration getDuration()
    {
        return this.duration;
    }

    /**
     * Fetch the number of events which are allowed to pass per second. This is used as a normalized measure
     * for comparing {@link FlowRate}s.
     *
     * @return The number of events per second, in partial events, as {@code double}.
     */
    protected double getVolumePerSecond()
    {
        return this.volumePerSecond;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;
        if (!(o instanceof FlowRate)) return false;
        final FlowRate flowRate = (FlowRate) o;
        return Double.compare(flowRate.volume, volume) == 0 &&
               Objects.equal(duration, flowRate.duration);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(volume, duration);
    }

    @Override
    public int compareTo(final FlowRate that)
    {
        return Double.compare(this.getVolumePerSecond(), that.getVolumePerSecond());
    }
}
