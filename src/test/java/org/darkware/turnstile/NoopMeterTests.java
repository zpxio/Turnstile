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

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author jeff@darkware.org
 * @since 2017-05-02
 */
public class NoopMeterTests
{
    @Test
    public void basic()
    {
        final NoopMeter meter = new NoopMeter();

        for (int i = 1; i < 1000; i++)
        {
            assertThat(meter.getDelayFor(i)).isEqualTo(0L);
        }
    }
}
