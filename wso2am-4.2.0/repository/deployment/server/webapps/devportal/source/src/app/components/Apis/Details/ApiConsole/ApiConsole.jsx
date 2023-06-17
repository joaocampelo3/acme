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

import React from 'react';
import { FormattedMessage } from 'react-intl';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Icon from '@material-ui/core/Icon';
import AuthManager from 'AppData/AuthManager';
import Paper from '@material-ui/core/Paper';
import Button from '@material-ui/core/Button';
import postmanIcon from '@iconify/icons-simple-icons/postman';
import { Icon as Icons } from '@iconify/react';
import fileDownload from 'js-file-download';
import openapiToPostman from 'openapi-to-postmanv2';
import swaggerToPostman from 'swagger2-postman2-converter';
import FileCopyIcon from '@material-ui/icons/FileCopy';
import CopyToClipboard from 'react-copy-to-clipboard';
import Tooltip from '@material-ui/core/Tooltip';
import CloudDownloadRounded from '@material-ui/icons/CloudDownloadRounded';
import queryString from 'query-string';
import Settings from 'Settings';
import Utils from 'AppData/Utils';
import { ApiContext } from '../ApiContext';
import Progress from '../../../Shared/Progress';
import Api from '../../../../data/api';
import SwaggerUI from './SwaggerUI';
import TryOutController from './TryOutController';
import Application from '../../../../data/Application';

/**
 * @inheritdoc
 * @param {*} theme theme
 */
const styles = (theme) => ({
    iconify: {
        marginRight: 10,
        font: 24,
    },
    buttonIcon: {
        marginRight: 10,
    },
    paper: {
        margin: theme.spacing(1),
        padding: theme.spacing(1),
    },
    grid: {
        marginTop: theme.spacing(4),
        marginBottom: theme.spacing(4),
        paddingRight: theme.spacing(2),
        justifyContent: 'center',
    },
    userNotificationPaper: {
        padding: theme.spacing(2),
    },
    titleSub: {
        marginLeft: theme.spacing(2),
        paddingTop: theme.spacing(2),
        paddingBottom: theme.spacing(2),
        color: theme.palette.getContrastText(theme.palette.background.default),
    },
    swaggerUIPaper: {
        backgroundColor: theme.custom.apiDetailPages.swaggerUIBackground,
    },
});

/**
 *
 *
 * @class ApiConsole
 * @extends {React.Component}
 */
class ApiConsole extends React.Component {
    /**
     *Creates an instance of ApiConsole.
     * @param {*} props properties
     * @memberof ApiConsole
     */
    constructor(props) {
        super(props);
        this.state = {
            securitySchemeType: 'OAUTH',
            username: '',
            password: '',
            scopes: [],
            selectedKeyType: 'PRODUCTION',
            keys: [],
            productionApiKey: '',
            sandboxApiKey: '',
            selectedKeyManager: 'Resident Key Manager',
            urlCopied: false,
            accessTokenPart: Utils.getCookieWithoutEnvironment('WSO2_AM_TOKEN_1_Default'),
            tenant: '',
            selectedAttribute: null,
            advAuthHeader: 'Authorization',
            advAuthHeaderValue: '',
            selectedEndpoint: 'PRODUCTION',
        };
        this.accessTokenProvider = this.accessTokenProvider.bind(this);
        this.updateSwagger = this.updateSwagger.bind(this);
        this.setSecurityScheme = this.setSecurityScheme.bind(this);
        this.setSelectedEnvironment = this.setSelectedEnvironment.bind(this);
        this.setProductionAccessToken = this.setProductionAccessToken.bind(this);
        this.setSandboxAccessToken = this.setSandboxAccessToken.bind(this);
        this.setUsername = this.setUsername.bind(this);
        this.setPassword = this.setPassword.bind(this);
        this.setSelectedKeyType = this.setSelectedKeyType.bind(this);
        this.setSectedKeyManager = this.setSelectedKeyManager.bind(this);
        this.setKeys = this.setKeys.bind(this);
        this.updateAccessToken = this.updateAccessToken.bind(this);
        this.setProductionApiKey = this.setProductionApiKey.bind(this);
        this.setSandboxApiKey = this.setSandboxApiKey.bind(this);
        this.converttopostman = this.convertToPostman.bind(this);
        this.setAdvAuthHeader = this.setAdvAuthHeader.bind(this);
        this.setAdvAuthHeaderValue = this.setAdvAuthHeaderValue.bind(this);
        this.setSelectedEndpoint = this.setSelectedEndpoint.bind(this);
        this.onCopy = this.onCopy.bind(this);
    }

    /**
     * @memberof ApiConsole
     */
    componentDidMount() {
        const { api } = this.context;
        const apiID = api.id;
        const user = AuthManager.getUser();
        let apiData;
        let environments;
        let selectedEnvironment;
        let swagger;
        let productionAccessToken;
        let sandboxAccessToken;
        const { app: { customUrl: { tenantDomain: customUrlEnabledDomain } } } = Settings;
        let tenantDomain = '';
        if (customUrlEnabledDomain !== 'null') {
            tenantDomain = customUrlEnabledDomain;
        } else {
            const { location } = window;
            if (location) {
                const { tenant } = queryString.parse(location.search);
                if (tenant) {
                    tenantDomain = tenant;
                }
            }
        }
        this.setState({ tenant: tenantDomain });
        this.apiClient = new Api();
        const promiseAPI = this.apiClient.getAPIById(apiID);
        let selectedAttribute = null;

        promiseAPI
            .then((apiResponse) => {
                apiData = apiResponse.obj;
                if (apiData.endpointURLs) {
                    environments = apiData.endpointURLs.map((endpoint) => {
                        return { name: endpoint.environmentName, displayName: endpoint.environmentDisplayName };
                    });
                }
                if (apiData.scopes) {
                    const scopeList = apiData.scopes.map((scope) => { return scope.key; });
                    this.setState({ scopes: scopeList });
                }
                if (environments && environments.length > 0) {
                    selectedEnvironment = environments[0].name;
                    selectedAttribute = 'environmentName';
                    return this.apiClient.getSwaggerByAPIIdAndEnvironment(apiID, selectedEnvironment);
                } else {
                    selectedAttribute = '';
                    return this.apiClient.getSwaggerByAPIId(apiID);
                }
            })
            .then((swaggerResponse) => {
                swagger = swaggerResponse.obj;

                let defaultSecurityScheme = 'OAUTH';
                if (!apiData.securityScheme.includes('oauth2')) {
                    defaultSecurityScheme = apiData.securityScheme.includes('api_key') ? 'API-KEY' : 'BASIC';
                }

                this.setState({
                    api: apiData,
                    swagger,
                    environments,
                    productionAccessToken,
                    sandboxAccessToken,
                    selectedEnvironment,
                    securitySchemeType: defaultSecurityScheme,
                    selectedAttribute,
                });
                if (user != null) {
                    return this.apiClient.getSubscriptions(apiID);
                } else {
                    return null;
                }
            })
            .catch((error) => {
                if (process.env.NODE_ENV !== 'production') {
                    console.error(error);
                }
                const { status } = error;
                if (status === 404) {
                    this.setState({ notFound: true });
                }
            });
    }

    /**
     * Set SecurityScheme value
     * @memberof ApiConsole
     */
    setSecurityScheme(securityScheme) {
        this.setState({ securitySchemeType: securityScheme });
    }

    /**
     * Set Selected Environment
     * @memberof ApiConsole
     */
    setSelectedEnvironment(selectedEnvironment) {
        this.setState({ selectedEnvironment });
    }

    /**
     * Set Production Access Token
     * @memberof ApiConsole
     */
    setProductionAccessToken(productionAccessToken) {
        this.setState({ productionAccessToken });
    }

    /**
     * Set Sandbox Access Token
     * @memberof ApiConsole
     */
    setSandboxAccessToken(sandboxAccessToken) {
        this.setState({ sandboxAccessToken });
    }

    /**
     * Set Production API Key
     * @memberof ApiConsole
     */
    setProductionApiKey(productionApiKey) {
        this.setState({ productionApiKey });
    }

    /**
     * Set Sandbox API Key
     * @memberof ApiConsole
     */
    setSandboxApiKey(sandboxApiKey) {
        this.setState({ sandboxApiKey });
    }

    /**
     * Set Username
     * @memberof ApiConsole
     */
    setUsername(username) {
        this.setState({ username });
    }

    /**
     * Set Password
     * @memberof ApiConsole
     */
    setPassword(password) {
        this.setState({ password });
    }

    /**
     * Set Password
     * @memberof ApiConsole
     */
    setSelectedKeyType(selectedKeyType, isUpdateToken, selectedApplication) {
        if (isUpdateToken) {
            this.setState({ selectedKeyType }, this.updateAccessToken(selectedApplication));
        } else {
            this.setState({ selectedKeyType });
        }
    }

    /**
     * Set Password
     * @memberof ApiConsole
     */
    setSelectedKeyManager(selectedKeyManager, isUpdateToken, selectedApplication) {
        if (isUpdateToken) {
            this.setState({ selectedKeyManager }, this.updateAccessToken(selectedApplication));
        } else {
            this.setState({ selectedKeyManager });
        }
    }

    setKeys(keys) {
        this.setState({ keys });
    }

    /**
     * Set authorization header of third party APIs
     * @param advAuthHeader authorization header
     */
    setAdvAuthHeader(advAuthHeader) {
        this.setState({ advAuthHeader });
    }

    /**
     * Set authorization header value of third party APIs
     * @param advAuthHeaderValue authorization header value
     */
    setAdvAuthHeaderValue(advAuthHeaderValue) {
        this.setState({ advAuthHeaderValue });
    }

    /**
     * Set selected endpoint type of third party APIs
     * @param selectedEndpoint selected endpoint type
     */
    setSelectedEndpoint(selectedEndpoint) {
        this.setState({ selectedEndpoint });
    }

    /**
     * Update swagger for properties for Swagger 2.0 third party APIs
     * @param spec api definition
     * @param serverUrl server url
     * @returns {*}
     */
    setServersSpec(spec, serverUrl) {
        let schemes;
        const [protocol, host] = serverUrl.split('://');
        if (protocol === 'http') {
            schemes = ['http'];
        } else if (protocol === 'https') {
            schemes = ['https'];
        }
        return {
            ...spec,
            schemes,
            host,
        };
    }

    /**
     * Converting an OpenAPI file to a postman collection
     * @memberof ApiConsole
   */
    convertToPostman(fr) {
        openapiToPostman.convert({ type: 'string', data: fr },
            {}, (err, conversionResult) => {
                if (!conversionResult.result) {
                    const collection = swaggerToPostman.convert(fr);
                    if (!collection) {
                        console.log('Could not convert');
                    } else {
                        fileDownload(
                            JSON.stringify(collection),
                            'postman collection',
                        );
                    }
                } else {
                    fileDownload(
                        JSON.stringify(conversionResult.output[0].data),
                        'postman collection',
                    );
                }
            });
    }

    /**
     * Load the access token for given key type
     * @memberof TryOutController
     */
    updateAccessToken(selectedApplication) {
        const {
            selectedKeyType, selectedKeyManager, keys,
        } = this.state;
        let accessToken;
        if (keys.get(selectedKeyManager) && keys.get(selectedKeyManager).keyType === selectedKeyType) {
            ({ accessToken } = keys.get(selectedKeyManager).token);
            if (selectedKeyType === 'PRODUCTION') {
                this.setProductionAccessToken(accessToken);
            } else {
                this.setSandboxAccessToken(accessToken);
            }
        } else {
            Application.get(selectedApplication)
                .then((application) => {
                    return application.getKeys(selectedKeyType);
                })
                .then((appKeys) => {
                    if (appKeys.get(selectedKeyManager)
                    && appKeys.get(selectedKeyManager).keyType === selectedKeyType) {
                        ({ accessToken } = appKeys.get(selectedKeyManager).token);
                    }
                    if (appKeys.get(selectedKeyManager).keyType === 'PRODUCTION') {
                        this.setProductionAccessToken(accessToken);
                    } else {
                        this.setSandboxAccessToken(accessToken);
                    }
                    this.setKeys(appKeys);
                });
        }
    }

    /**
     *
     * Provids the access token to the Swagger UI
     * @returns {*} access token
     * @memberof ApiConsole
     */
    accessTokenProvider() {
        const {
            securitySchemeType, username, password, productionAccessToken, sandboxAccessToken, selectedKeyType,
            productionApiKey, sandboxApiKey, api, advAuthHeaderValue,
        } = this.state;
        if (api.advertiseInfo && api.advertiseInfo.advertised) {
            return advAuthHeaderValue;
        }
        if (securitySchemeType === 'BASIC') {
            const credentials = username + ':' + password;
            return btoa(credentials);
        }
        if (securitySchemeType === 'API-KEY') {
            if (selectedKeyType === 'PRODUCTION') {
                return productionApiKey;
            } else {
                return sandboxApiKey;
            }
        } else if (selectedKeyType === 'PRODUCTION') {
            return productionAccessToken;
        } else {
            return sandboxAccessToken;
        }
    }

    /**
     * Load the swagger file of the given environment
     * @memberof ApiConsole
     */
    updateSwagger(environment) {
        const {
            api, environments,
        } = this.state;
        let promiseSwagger;

        if (environment && environments.find((e) => e.name === environment)) {
            promiseSwagger = this.apiClient.getSwaggerByAPIIdAndEnvironment(api.id, environment);
        } else {
            promiseSwagger = this.apiClient.getSwaggerByAPIId(api.id);
        }
        promiseSwagger.then((swaggerResponse) => {
            this.setState({ swagger: swaggerResponse.obj });
        });
    }

    onCopy = () => {
        this.setState({
            urlCopied: true,
        });
        const caller = function () {
            this.setState({ urlCopied: false });
        };
        setTimeout(caller, 2000);
    }

    /**
     * @inheritdoc
     * @memberof ApiConsole
     */
    render() {
        const { classes } = this.props;
        const {
            api, notFound, swagger, securitySchemeType, selectedEnvironment, environments, scopes,
            username, password, productionAccessToken, sandboxAccessToken, selectedKeyType, accessTokenPart,
            sandboxApiKey, productionApiKey, selectedKeyManager, advAuthHeader, advAuthHeaderValue, selectedEndpoint,
            urlCopied, tenant, selectedAttribute,
        } = this.state;
        const { location } = window;
        const user = AuthManager.getUser();
        const downloadSwagger = JSON.stringify({ ...swagger });
        const downloadLink = 'data:text/json;charset=utf-8, ' + encodeURIComponent(downloadSwagger);
        const fileName = 'swagger.json';

        if (api == null || swagger == null) {
            return <Progress />;
        }
        if (notFound) {
            return 'API Not found !';
        }
        let isApiKeyEnabled = false;
        let authorizationHeader = api.authorizationHeader ? api.authorizationHeader : 'Authorization';
        if (api && api.securityScheme) {
            isApiKeyEnabled = api.securityScheme.includes('api_key');
            if (isApiKeyEnabled && securitySchemeType === 'API-KEY') {
                authorizationHeader = 'apikey';
            }
        }
        let swaggerSpec = swagger;
        if (api.advertiseInfo && api.advertiseInfo.advertised) {
            authorizationHeader = advAuthHeader;
            if (swaggerSpec.openapi) {
                if (selectedEndpoint === 'PRODUCTION') {
                    swaggerSpec = {
                        ...swagger,
                        servers: [
                            { url: api.advertiseInfo.apiExternalProductionEndpoint },
                        ],
                    };
                } else {
                    swaggerSpec = {
                        ...swagger,
                        servers: [
                            { url: api.advertiseInfo.apiExternalSandboxEndpoint },
                        ],
                    };
                }
            } else if (selectedEndpoint === 'PRODUCTION') {
                swaggerSpec = this.setServersSpec(swaggerSpec, api.advertiseInfo.apiExternalProductionEndpoint);
            } else {
                swaggerSpec = this.setServersSpec(swaggerSpec, api.advertiseInfo.apiExternalSandboxEndpoint);
            }
        }
        return (
            <>
                <Paper className={classes.paper}>
                    <Grid container className={classes.grid}>
                        {!user && (!api.advertiseInfo || !api.advertiseInfo.advertised) && (
                            <Grid item md={6}>
                                <Paper className={classes.userNotificationPaper}>
                                    <Typography variant='h5' component='h3'>
                                        <Icon>info</Icon>
                                        {' '}
                                        <FormattedMessage id='notice' defaultMessage='Notice' />
                                    </Typography>
                                    <Typography component='p'>
                                        <FormattedMessage
                                            id='api.console.require.access.token'
                                            defaultMessage={'You need an access token to try the API. Please log '
                                            + 'in and subscribe to the API to generate an access token. If you already '
                                            + 'have an access token, please provide it below.'}
                                        />
                                    </Typography>
                                </Paper>
                            </Grid>
                        )}
                    </Grid>

                    <TryOutController
                        setSecurityScheme={this.setSecurityScheme}
                        securitySchemeType={securitySchemeType}
                        setSelectedEnvironment={this.setSelectedEnvironment}
                        selectedEnvironment={selectedEnvironment}
                        productionAccessToken={productionAccessToken}
                        setProductionAccessToken={this.setProductionAccessToken}
                        sandboxAccessToken={sandboxAccessToken}
                        setSandboxAccessToken={this.setSandboxAccessToken}
                        swagger={swaggerSpec}
                        environments={environments}
                        scopes={scopes}
                        setUsername={this.setUsername}
                        setPassword={this.setPassword}
                        username={username}
                        password={password}
                        setSelectedKeyType={this.setSelectedKeyType}
                        selectedKeyType={selectedKeyType}
                        setSelectedKeyManager={this.setSelectedKeyManager}
                        selectedKeyManager={selectedKeyManager}
                        updateSwagger={this.updateSwagger}
                        setKeys={this.setKeys}
                        setProductionApiKey={this.setProductionApiKey}
                        setSandboxApiKey={this.setSandboxApiKey}
                        productionApiKey={productionApiKey}
                        sandboxApiKey={sandboxApiKey}
                        setAdvAuthHeader={this.setAdvAuthHeader}
                        setAdvAuthHeaderValue={this.setAdvAuthHeaderValue}
                        advAuthHeader={advAuthHeader}
                        advAuthHeaderValue={advAuthHeaderValue}
                        setSelectedEndpoint={this.setSelectedEndpoint}
                        selectedEndpoint={selectedEndpoint}
                        api={this.state.api}
                        URLs={null}
                    />

                    {api.type !== 'SOAP' && (
                        <Grid container>
                            <Grid xs={7} item />
                            <Grid xs={2} item>
                                <Button size='small' onClick={() => this.convertToPostman(downloadSwagger)}>
                                    <Icons icon={postmanIcon} width={30} height={30} className={classes.buttonIcon} />
                                    <FormattedMessage
                                        id='Apis.Details.APIConsole.APIConsole.download.postman'
                                        defaultMessage='Postman collection'
                                    />
                                </Button>
                            </Grid>
                            <Grid xs={3} item>
                                <a href={downloadLink} download={fileName}>
                                    <Button size='small'>
                                        <CloudDownloadRounded className={classes.buttonIcon} />
                                        <FormattedMessage
                                            id='Apis.Details.APIConsole.APIConsole.download.swagger'
                                            defaultMessage='Swagger ( /swagger.json )'
                                        />
                                    </Button>
                                </a>
                                <Tooltip
                                    title={urlCopied
                                        ? (
                                            <FormattedMessage
                                                id='Apis.Details.Swagger.URL.copied'
                                                defaultMessage='Copied'
                                            />
                                        )
                                        : (
                                            <FormattedMessage
                                                id='Apis.Details.Swagger.URL.copy.to.clipboard'
                                                defaultMessage='Copy to clipboard'
                                            />
                                        )}
                                    placement='top'
                                >
                                    <CopyToClipboard
                                        text={location.origin + '/api/am/devportal/v3/apis/' + api.id + '/swagger?accessToken='
                                        + accessTokenPart + '&X-WSO2-Tenant-Q=' + tenant + '&' + selectedAttribute + '='
                                        + selectedEnvironment}
                                        onCopy={this.onCopy}
                                        size='small'
                                    >
                                        <Button aria-label='Copy to clipboard' className={classes.button}>
                                            <FileCopyIcon className={classes.buttonIcon} />
                                        </Button>
                                    </CopyToClipboard>
                                </Tooltip>
                            </Grid>
                        </Grid>
                    )}
                </Paper>
                <Paper className={classes.swaggerUIPaper}>
                    <SwaggerUI
                        api={this.state.api}
                        accessTokenProvider={this.accessTokenProvider}
                        spec={swaggerSpec}
                        authorizationHeader={authorizationHeader}
                        securitySchemeType={securitySchemeType}
                    />
                </Paper>
            </>
        );
    }
}

ApiConsole.propTypes = {
    classes: PropTypes.shape({
        paper: PropTypes.string.isRequired,
        titleSub: PropTypes.string.isRequired,
        grid: PropTypes.string.isRequired,
        userNotificationPaper: PropTypes.string.isRequired,
        buttonIcon: PropTypes.string.isRequired,
        iconify: PropTypes.string.isRequired,
    }).isRequired,
};

ApiConsole.contextType = ApiContext;

export default withStyles(styles)(ApiConsole);
