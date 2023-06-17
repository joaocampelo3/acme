/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import React from 'react';
import { Route, Switch } from 'react-router-dom';
import ResourceNotFound from 'AppComponents/Base/Errors/ResourceNotFound';
import Listing from './Listing/Listing';
import ScopesCreate from './Create/CreateScope';
import ScopesEdit from './EditScope';

/**
 * `Route` elements for shared scopes UI.
 * @returns {JSX} Shared scope routes.
 */
const Scopes = () => {
    return (
        <Switch>
            <Route
                exact
                path='/scopes'
                component={Listing}
            />
            <Route exact path='/scopes/create' component={ScopesCreate} />
            <Route exact path='/scopes/edit' component={ScopesEdit} />
            <Route component={ResourceNotFound} />
        </Switch>
    );
};

export default Scopes;
