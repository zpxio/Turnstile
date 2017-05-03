/*==============================================================================
 =
 = Copyright 2017: darkware.org
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

package org.darkware.turnstile;

import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author jeff@darkware.org
 * @since 2017-05-02
 */
public class PausableMeterTests
{
    @Test
    public void pause() throws InterruptedException
    {
        final PausableMeter meter = new RateControlledMeter("100/s");

        meter.start();
        Thread.sleep(200);
        meter.pause();

        assertThat(meter.getElapsedTime()).isZero();
        assertThat(meter.getPreviousElapsedTime()).isCloseTo(200L, Offset.offset(10L));
    }
}