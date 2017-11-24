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
package org.estatio.module.asset.fixtures.person.personas;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.estatio.module.asset.dom.role.FixedAssetRoleTypeEnum;
import org.estatio.module.asset.fixtures.person.builders.PersonAndRolesBuilder;
import org.estatio.module.asset.fixtures.person.enums.Person_enum;
import org.estatio.module.asset.fixtures.property.personas.PropertyAndOwnerAndManagerForMacFr;
import org.estatio.module.party.dom.PersonGenderType;

public class PersonAndRolesForOlivePropertyManagerFr extends FixtureScript {

    public static final Person_enum data = Person_enum.OlivePropertyManagerFr;

    public static final String REF = data.getRef();
    public static final String AT_PATH = data.getApplicationTenancy().getPath();

    @Override
    protected void execute(ExecutionContext executionContext) {

        executionContext.executeChild(this, new PropertyAndOwnerAndManagerForMacFr());

        getContainer().injectServicesInto(new PersonAndRolesBuilder())
                    .setAtPath(AT_PATH)
                    .setReference(REF)
                    .setFirstName("Olive")
                    .setLastName("Beusoleil")
                    .setPersonGenderType(PersonGenderType.FEMALE)
                    .addFixedAssetRole(FixedAssetRoleTypeEnum.PROPERTY_MANAGER, PropertyAndOwnerAndManagerForMacFr.REF)
                    .addPartyRoleType(FixedAssetRoleTypeEnum.PROPERTY_MANAGER) // implied 
                    .setSecurityUsername(REF.toLowerCase())
                .execute(executionContext);
    }

}
