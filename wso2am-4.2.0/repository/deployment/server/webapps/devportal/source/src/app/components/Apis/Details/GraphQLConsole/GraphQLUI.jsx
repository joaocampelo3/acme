/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import React, {
    useState, useEffect, useRef, useContext,
} from 'react';
import GraphiQL from 'graphiql';
import 'graphiql/graphiql.css';
import PropTypes from 'prop-types';
import Box from '@material-ui/core/Box';
import GraphiQLExplorer from 'graphiql-explorer';
import Collapse from '@material-ui/core/Collapse';
import { createGraphiQLFetcher } from '@graphiql/toolkit';
import { ApiContext } from '../ApiContext';
import Api from '../../../../data/api';
import QueryComplexityView from './QueryComplexityView';

import Progress from '../../../Shared/Progress';

const { buildSchema } = require('graphql');

/**
 *
 * @param {*} props
 */
export default function GraphQLUI(props) {
    const {
        authorizationHeader,
        URLs,
        securitySchemeType,
        accessTokenProvider,
        additionalHeaders,
    } = props;
    const { api } = useContext(ApiContext);
    const [schema, setSchema] = useState(null);
    const [query, setQuery] = useState('');
    const [isExplorerOpen, setIsExplorerOpen] = useState(false);
    const graphiqlEl = useRef(null);
    const [open, setOpen] = React.useState(true);

    useEffect(() => {
        const apiID = api.id;
        const apiClient = new Api();
        const promiseGraphQL = apiClient.getGraphQLSchemaByAPIId(apiID);
        promiseGraphQL
            .then((res) => {
                const graphqlSchemaObj = buildSchema(res.data);
                setSchema(graphqlSchemaObj);
            });
    }, []);

    const parameters = {};

    const handleClick = () => {
        setOpen(!open);
    };

    const handleToggleExplorer = () => {
        const newExplorerIsOpen = !isExplorerOpen;
        parameters.isExplorerOpen = newExplorerIsOpen;
        setIsExplorerOpen(newExplorerIsOpen);
    };

    /**
     * Get subscription fetcher.
     * @param {string} wsUrl subscription websocket URL
     * @return {string} The subscription fetcher
     */
    function queryFetcher(wsUrl) {
        let token;
        if (api.advertiseInfo && api.advertiseInfo.advertised) {
            token = accessTokenProvider();
        } else if (authorizationHeader === 'apikey') {
            token = accessTokenProvider();
        } else if (securitySchemeType === 'BASIC') {
            token = 'Basic ' + accessTokenProvider();
        } else {
            token = 'Bearer ' + accessTokenProvider();
        }

        const headers = {
            [authorizationHeader]: token,
        };

        additionalHeaders.forEach((header) => {
            headers[header.name] = header.value;
        });

        return createGraphiQLFetcher({
            headers,
            url: URLs ? URLs.https : null,
            subscriptionUrl: wsUrl === null || wsUrl === undefined ? null
                : wsUrl + '?access_token=' + accessTokenProvider(),
        });
    }

    if ({ schema } === null) {
        return <Progress />;
    } else {
        return (
            <>
                <div>
                    <Box display='flex'>
                        <Box display='flex'>
                            <Collapse in={!open} timeout='auto' unmountOnExit>
                                <QueryComplexityView
                                    open={open}
                                    setOpen={setOpen}
                                />
                            </Collapse>
                        </Box>
                        <Box display='flex' width={1}>
                            <Box display='flex'>
                                <GraphiQLExplorer
                                    schema={schema}
                                    query={query}
                                    onEdit={setQuery}
                                    explorerIsOpen={isExplorerOpen}
                                    onToggleExplorer={handleToggleExplorer}
                                />
                            </Box>
                            <Box display='flex' height='800px' flexGrow={1}>
                                <GraphiQL
                                    ref={graphiqlEl}
                                    fetcher={(queryFetcher(URLs && URLs.wss))}
                                    schema={schema}
                                    query={query}
                                    onEditQuery={setQuery}
                                >
                                    <GraphiQL.Toolbar>
                                        <GraphiQL.Button
                                            onClick={() => graphiqlEl.current.handlePrettifyQuery()}
                                            label='Prettify'
                                            title='Prettify Query (Shift-Ctrl-P)'
                                        />
                                        <GraphiQL.Button
                                            onClick={() => graphiqlEl.current.handleToggleHistory()}
                                            label='History'
                                            title='Show History'
                                        />
                                        <GraphiQL.Button
                                            onClick={() => setIsExplorerOpen(!isExplorerOpen)}
                                            label='Explorer'
                                            title='Toggle Explorer'
                                        />
                                        <GraphiQL.Button
                                            onClick={handleClick}
                                            label='Complexity Analysis'
                                            title='View Field`s Complexity Values'
                                        />
                                    </GraphiQL.Toolbar>

                                </GraphiQL>
                            </Box>
                        </Box>
                    </Box>
                </div>
            </>
        );
    }
}

GraphQLUI.propTypes = {
    classes: PropTypes.shape({
        paper: PropTypes.string.isRequired,
    }).isRequired,
    additionalHeaders: PropTypes.shape({
        array: PropTypes.arrayOf(PropTypes.element).isRequired,
    }).isRequired,
};
