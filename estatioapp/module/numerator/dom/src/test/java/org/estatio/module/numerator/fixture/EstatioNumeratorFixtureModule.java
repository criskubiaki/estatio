/*
 *
 *  Copyright 2012-2014 Eurocommercial Properties NV
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.module.numerator.fixture;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.incode.module.fixturesupport.dom.scripts.TeardownFixtureAbstract;

import org.estatio.module.base.fixtures.security.apptenancy.enums.ApplicationTenancy_enum;
import org.estatio.module.numerator.dom.Numerator;

public class EstatioNumeratorFixtureModule {


    private EstatioNumeratorFixtureModule() {}



    public abstract static class ActionDomainEvent<S>
            extends org.apache.isis.applib.services.eventbus.ActionDomainEvent<S> { }

    public abstract static class CollectionDomainEvent<S,T>
            extends org.apache.isis.applib.services.eventbus.CollectionDomainEvent<S,T> { }

    public abstract static class PropertyDomainEvent<S,T>
            extends org.apache.isis.applib.services.eventbus.PropertyDomainEvent<S,T> { }


    public static class Setup extends FixtureScript {

        static boolean prereqsRun = false;

        @Override
        protected void execute(final ExecutionContext executionContext) {
            if(!prereqsRun) {
                executionContext.executeChild(this, new ApplicationTenancy_enum.PersistScript());
                prereqsRun = true;
            }
        }
    }

    public static class Teardown extends TeardownFixtureAbstract {
        @Override
        protected void execute(final ExecutionContext executionContext) {
            deleteFrom(Numerator.class);
//        executionContext.executeChild(this, new ApplicationTenancy_enum.DeleteScript());
        }
    }
}

