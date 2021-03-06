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

import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * @author jeff@darkware.org
 * @since 2017-04-30
 */
public class SimpleFactorTests
{
    @Test
    public void multiplier()
    {
        assertThat(SimpleFactor.K.getMultiplier()).isEqualTo(1000L);
    }

    @Test
    public void apply_long()
    {
        assertThat(SimpleFactor.K.apply(4L)).isEqualTo(4L * SimpleFactor.K.getMultiplier());
    }

    @Test
    public void apply_double()
    {
        assertThat(SimpleFactor.K.apply(3.1137)).isEqualTo(3.1137 * SimpleFactor.K.getMultiplier(), Offset.offset(0.02));
    }

}
