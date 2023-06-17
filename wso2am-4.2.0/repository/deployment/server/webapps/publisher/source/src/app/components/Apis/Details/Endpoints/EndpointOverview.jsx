/**
 * Copyright (c)  WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, {
    useEffect, useState, useCallback,
} from 'react';
import {
    FormControl,
    Grid,
    Paper,
    Typography,
    withStyles,
    Radio,
    FormControlLabel,
    Collapse,
    RadioGroup, Checkbox, Dialog, DialogTitle, DialogContent, IconButton, Button, DialogActions, Icon,
} from '@material-ui/core';
import PropTypes from 'prop-types';
import { FormattedMessage, injectIntl } from 'react-intl';
import { isRestricted } from 'AppData/AuthManager';
import LaunchIcon from '@material-ui/icons/Launch';
import { Progress } from 'AppComponents/Shared';
import CONSTS from 'AppData/Constants';

import cloneDeep from 'lodash.clonedeep';
import InlineMessage from 'AppComponents/Shared/InlineMessage';
import ServiceCatalog from 'AppData/ServiceCatalog';
import Alert from 'AppComponents/Shared/Alert';
import MockImplEndpoints from 'AppComponents/Apis/Details/Endpoints/Prototype/MockImplEndpoints';
import {
    getEndpointTypeProperty,
    createEndpointConfig,
    getEndpointTemplate,
} from './endpointUtils';
import GeneralConfiguration from './GeneralConfiguration';
import LoadbalanceFailoverConfig from './LoadbalanceFailoverConfig';
import GenericEndpoint from './GenericEndpoint';
import AdvanceEndpointConfig from './AdvancedConfig/AdvanceEndpointConfig';
import EndpointSecurity from './GeneralConfiguration/EndpointSecurity';
import Credentials from './AWSLambda/Credentials.jsx';
import ServiceEndpoint from './ServiceEndpoint';

const styles = (theme) => ({
    listing: {
        margin: theme.spacing(1),
        padding: theme.spacing(1),
    },
    endpointContainer: {
        paddingLeft: theme.spacing(2),
        padding: theme.spacing(1),
    },
    endpointName: {
        paddingLeft: theme.spacing(1),
        fontSize: '1rem',
        paddingTop: theme.spacing(1),
        paddingBottom: theme.spacing(1),
    },
    endpointTypesWrapper: {
        padding: theme.spacing(3),
        marginTop: theme.spacing(2),
    },
    sandboxHeading: {
        display: 'flex',
        alignItems: 'center',
    },
    radioGroup: {
        display: 'flex',
        flexDirection: 'row',
    },
    endpointsWrapperLeft: {
        padding: theme.spacing(1),
        borderRight: '#c4c4c4',
        borderRightStyle: 'solid',
        borderRightWidth: 'thin',
    },
    endpointsWrapperRight: {
        padding: theme.spacing(1),
    },
    endpointsTypeSelectWrapper: {
        marginLeft: theme.spacing(2),
        marginRight: theme.spacing(2),
        padding: theme.spacing(1),
        display: 'flex',
        justifyContent: 'space-between',
    },
    endpointTypesSelectWrapper: {
        display: 'flex',
    },
    defaultEndpointWrapper: {
        paddingLeft: theme.spacing(1),
        paddingRight: theme.spacing(1),
        marginRight: theme.spacing(1),
    },
    configDialogHeader: {
        fontWeight: '600',
    },
    addLabel: {
        padding: theme.spacing(2),
    },
    buttonIcon: {
        marginRight: theme.spacing(1),
    },
    button: {
        textTransform: 'none',
    },
});

const endpointTypes = [
    { key: 'http', value: 'HTTP/REST Endpoint' },
    { key: 'default', value: 'Dynamic Endpoints' },
    { key: 'address', value: 'HTTP/SOAP Endpoint' },
    { key: 'prototyped', value: 'Prototype Endpoint' },
    { key: 'INLINE', value: 'Mock Implementation' },
    { key: 'awslambda', value: 'AWS Lambda' },
    { key: 'service', value: 'Service Endpoint' },
    { key: 'MOCKED_OAS', value: 'Mock Implementation' },
];

/**
 * The endpoint overview component. This component holds the views of endpoint creation and configuration.
 * @param {any} props The props that are being passed to the component.
 * @returns {any} HTML view of the endpoints overview.
 */
function EndpointOverview(props) {
    const {
        classes,
        api,
        endpointsDispatcher,
        swaggerDef,
        updateSwagger,
        saveAndRedirect,
    } = props;
    const { endpointConfig } = api;
    const [endpointType, setEndpointType] = useState(endpointTypes[0]);
    const [supportedEnpointTypes, setSupportedEndpointType] = useState([]);

    const [epConfig, setEpConfig] = useState(endpointConfig);
    const [endpointSecurityInfo, setEndpointSecurityInfo] = useState(null);
    const [advanceConfigOptions, setAdvancedConfigOptions] = useState({
        open: false,
        index: 0,
        type: '',
        category: '',
        config: undefined,
    });
    const [endpointSecurityConfig, setEndpointSecurityConfig] = useState({
        open: false,
        type: '',
        category: '',
        config: undefined,
    });
    const [endpointCategory, setEndpointCategory] = useState({ sandbox: false, prod: false });
    const [typeChangeConfirmation, setTypeChangeConfirmation] = useState({ openDialog: false, serviceInfo:
    (api.serviceInfo) });
    const [servicesList, setServicesList] = useState([]);

    const handleToggleEndpointSecurity = () => {
        const tmpSecurityInfo = !endpointSecurityInfo ? {
            production: CONSTS.DEFAULT_ENDPOINT_SECURITY,
            sandbox: CONSTS.DEFAULT_ENDPOINT_SECURITY,
        } : endpointSecurityInfo;
        setEndpointSecurityInfo(tmpSecurityInfo);
    };

    /**
     * Method to get the type of the endpoint. (HTTP/REST or HTTP/SOAP)
     * In failover/ loadbalance cases, the endpoint type is presented in the endpoints list. Therefore that property
     * needs to be extracted separately.
     *
     * @param {Object} apiObject  The representative type of the endpoint.
     * @return {string} The type of the endpoint.
     * */
    const getEndpointType = (apiObject) => {
        const type = apiObject.endpointConfig && apiObject.endpointConfig.endpoint_type;
        if (apiObject.endpointImplementationType === 'INLINE') {
            return endpointTypes[4];
        } else if (apiObject.endpointImplementationType === 'MOCKED_OAS') {
            return endpointTypes[7];
        } else if (apiObject.endpointImplementationType === 'ENDPOINT'
            && apiObject.endpointConfig.implementation_status === 'prototyped') {
            return endpointTypes[3];
        } else if (type === 'http') {
            if (typeChangeConfirmation.serviceInfo) {
                return endpointTypes[6];
            }
            return endpointTypes[0];
        } else if (type === 'default') {
            return endpointTypes[1];
        } else if (type === 'address') {
            return endpointTypes[2];
        } else if (type === 'awslambda') {
            return endpointTypes[5];
        } else if (type === 'service') {
            return endpointTypes[6];
        } else {
            const availableEndpoints = (endpointConfig.production_endpoints && endpointConfig.production_endpoints)
                || (endpointConfig.sandbox_endpoints && endpointConfig.sandbox_endpoints);
            // Handle the all endpoints de-select condition... Rollback to http.
            if (!availableEndpoints) {
                return endpointTypes[0];
            }
            if (Array.isArray(availableEndpoints)) {
                return availableEndpoints[0].endpoint_type !== undefined
                    ? endpointTypes[2] : endpointTypes[0];
            }
            return availableEndpoints.endpoint_type !== undefined
                ? endpointTypes[2] : endpointTypes[0];
        }
    };

    /**
     * Method to get the supported endpoint types by api type.
     *
     * @param {Object} apiObject  The representative type of the endpoint.
     * @return {string} The supported endpoint types.
     * */
    const getSupportedType = (apiObject) => {
        const { type } = apiObject;
        let supportedEndpointTypes = [];
        if (type === 'GRAPHQL') {
            supportedEndpointTypes = [
                { key: 'http', value: 'HTTP/REST Endpoint' },
                { key: 'service', value: 'Service Endpoint' },
                { key: 'default', value: 'Dynamic Endpoints' },
            ];
        } else if (type === 'SOAP' || type === 'SOAPTOREST') {
            supportedEndpointTypes = [
                { key: 'address', value: 'HTTP/SOAP Endpoint' },
                { key: 'default', value: 'Dynamic Endpoints' },
            ];
        } else if (type === 'SSE') {
            supportedEndpointTypes = [
                { key: 'http', value: 'HTTP/REST Endpoint' },
                { key: 'service', value: 'Service Endpoint' },
            ];
        } else {
            supportedEndpointTypes = [
                { key: 'http', value: 'HTTP/REST Endpoint' },
                { key: 'service', value: 'Service Endpoint' },
                { key: 'address', value: 'HTTP/SOAP Endpoint' },
                { key: 'default', value: 'Dynamic Endpoints' },
                { key: 'INLINE', value: 'Mock Implementation' },
                { key: 'awslambda', value: 'AWS Lambda' },
            ];
        }
        return supportedEndpointTypes;
    };

    /**
     * Retrieve the service list from the service catalog
     */
    function getServices() {
        const promisedServices = ServiceCatalog.getServiceList();
        promisedServices.then((response) => {
            setServicesList(response.list);

        }).catch((error) => {
            if (error.response) {
                Alert.error(error.response.body.description);
            } else {
                Alert.error(
                    <FormattedMessage
                        id='Apis.Details.APIDefinition.Addservice.service.retrieve.error'
                        defaultMessage='Something went wrong while retrieving the services'
                    />,
                );
            }
            console.error(error);
        });
    }

    useEffect(() => {
        const supportedTypeLists = getSupportedType(api);
        const epType = getEndpointType(api);
        if (epType.key === 'service') {
            getServices();
        }
        if (epType.key !== 'INLINE' || epType.key !== 'MOCKED_OAS') {
            setEndpointCategory({
                prod: !!endpointConfig.production_endpoints,
                sandbox: !!endpointConfig.sandbox_endpoints,
            });
        }
        setSupportedEndpointType(supportedTypeLists);
        setEpConfig(endpointConfig);
        setEndpointType(epType);
        setEndpointSecurityInfo(endpointConfig.endpoint_security);
    }, [props]);


    const getEndpoints = (type) => {
        if (epConfig[type]) {
            return epConfig[type].length > 0
                ? epConfig[type][0].url : epConfig[type].url;
        }
        return '';
    };

    const getService = () => {
        if (epConfig.serviceInfo) {
            return epConfig.serviceInfo
        }
        return '';
    };

    const handleOnChangeEndpointCategoryChange = (category) => {
        let endpointConfigCopy = cloneDeep(endpointConfig);
        if (category === 'prod') {
            const endpointProp = 'production_endpoints';
            if (endpointCategory[category]) {
                delete endpointConfigCopy[endpointProp];
                if (endpointConfigCopy.endpointType === 'failover') {
                    delete endpointConfigCopy.production_failovers;
                }
            } else if (endpointConfigCopy.endpointType === 'load_balance') {
                endpointConfigCopy[endpointProp] = [getEndpointTemplate(endpointType.key)];
            } else if (endpointConfigCopy.endpointType === 'failover') {
                endpointConfigCopy[endpointProp] = getEndpointTemplate(endpointType.key);
                endpointConfigCopy.production_failovers = [];
            } else {
                endpointConfigCopy[endpointProp] = getEndpointTemplate(endpointType.key);
            }
        } else {
            const endpointProp = 'sandbox_endpoints';
            if (endpointCategory[category]) {
                delete endpointConfigCopy[endpointProp];
                if (endpointConfigCopy.endpointType === 'failover') {
                    delete endpointConfigCopy.sandbox_failovers;
                }
            } else if (endpointConfigCopy.endpointType === 'load_balance') {
                endpointConfigCopy[endpointProp] = [getEndpointTemplate(endpointType.key)];
            } else if (endpointConfigCopy.endpointType === 'failover') {
                endpointConfigCopy[endpointProp] = getEndpointTemplate(endpointType.key);
                endpointConfigCopy.sandbox_failovers = [];
            } else {
                endpointConfigCopy[endpointProp] = getEndpointTemplate(endpointType.key);
            }
        }
        // Check whether, config has either prod/ sandbox endpoints. If not, reSet the endpoint type to http
        if (!endpointConfigCopy.production_endpoints && !endpointConfigCopy.sandbox_endpoints) {
            endpointConfigCopy = createEndpointConfig('http');
        }
        endpointsDispatcher({ action: 'select_endpoint_category', value: endpointConfigCopy });
    };

    /**
     * Method to modify the endpoint represented by the given parameters.
     *
     * If url is null, remove the endpoint from the endpoint config.
     *
     * @param {number} index The index of the endpoint in the listing.
     * @param {string} category The endpoint category. (production/ sand box)
     * @param {string} url The new endpoint url.
     * */
    const editEndpoint = (index, category, url) => {
        let modifiedEndpoint = null;
        // Make a copy of the endpoint config.
        const endpointConfigCopy = cloneDeep(epConfig);
        /*
        * If the index > 0, it means that the endpoint is load balance or fail over.
        * Otherwise it is the default endpoint. (index = 0)
        * */
        if (index > 0) {
            const endpointTypeProperty = getEndpointTypeProperty(endpointConfigCopy.endpoint_type, category);
            modifiedEndpoint = endpointConfigCopy[endpointTypeProperty];
            /*
            * In failover case, the failover endpoints are a separate object. But in endpoint listing, since we
            *  consider all the endpoints as a single list, to get the real index of the failover endpoint we use
            *  index - 1.
            * */
            if (endpointConfigCopy.endpoint_type === 'failover') {
                modifiedEndpoint[index - 1].url = url.trim();
            } else {
                modifiedEndpoint[index].url = url.trim();
            }
            endpointConfigCopy[endpointTypeProperty] = modifiedEndpoint;
        } else if (url !== '') {
            modifiedEndpoint = endpointConfigCopy[category];

            /*
            * In this case, we are editing the default endpoint.
            * If the endpoint type is load balance, the production_endpoints or the sandbox_endpoint object is an
            *  array. Otherwise, in failover mode, the default endpoint is an object.
            *
            * So, we check whether the endpoints is an array or an object.
            *
            * If This is the first time a user creating an endpoint endpoint config object does not have
            *  production_endpoints or sandbox_endpoints object.
            * Therefore create new object and add to the endpoint config.
            * */
            if (!modifiedEndpoint) {
                modifiedEndpoint = getEndpointTemplate(endpointConfigCopy.endpoint_type);
                modifiedEndpoint.url = url.trim();
            } else if (Array.isArray(modifiedEndpoint)) {
                if (url === '') {
                    modifiedEndpoint.splice(0, 1);
                } else {
                    modifiedEndpoint[0].url = url.trim();
                }
            } else {
                modifiedEndpoint.url = url.trim();
            }
            endpointConfigCopy[category] = modifiedEndpoint;
        } else {
            /*
            * If the url is empty, delete the respective endpoint object.
            * */
            delete endpointConfigCopy[category];
        }
        endpointsDispatcher({ action: category, value: modifiedEndpoint });

       
        // Remove service from the API if any other endpoint type is selected
        const epType = getEndpointType(api);
        if (epType.key !== 'service') {
            endpointsDispatcher({ action: 'set_service',
                value:  { 
                    key: "_",
                    name: "_",
                    outdated: false,
                    version: "_"
                }} );
        }
         
    };

    const editService = (value) => {

        const endpointConfigCopy = cloneDeep(epConfig);
        const newService = { 
            key: value.serviceKey,
            name: value.name,
            outdated: false,
            version: value.version
        }
        if (value && value.serviceKey) {
            endpointConfigCopy.service = newService;
            endpointsDispatcher({ action: 'set_service',
                value: newService } ); 
        }
    }

    const handleEndpointCategorySelect = (event) => {
        endpointsDispatcher({
            action: 'endpoint_type',
            value: { category: event.target.value, endpointType: endpointType.key },
        });
    };

    /**
     * Handles the endpoint type change functionality.
     *
     * @param {string} value The selected endpoint type.
     * */
    const changeEndpointType = (value) => {
        setTypeChangeConfirmation({ openDialog: false, serviceInfo: false });
        const selectedKey = typeChangeConfirmation.type || value;
        if (selectedKey === 'INLINE' || selectedKey === 'MOCKED_OAS') {
            const tmpConfig = createEndpointConfig('prototyped');
            endpointsDispatcher({
                action: 'set_inline_or_mocked_oas',
                value: {
                    endpointConfig: tmpConfig,
                    endpointImplementationType: selectedKey,
                },
            });
        } else if (selectedKey === 'prototyped') {
            const tmpConfig = createEndpointConfig(selectedKey);
            endpointsDispatcher({
                action: 'set_prototyped',
                value: {
                    endpointImplementationType: 'ENDPOINT',
                    endpointConfig: tmpConfig,
                },
            });
        } else if (selectedKey === 'awslambda') {
            const generatedEndpointConfig = createEndpointConfig(selectedKey);
            endpointsDispatcher({
                action: 'select_endpoint_type',
                value: {
                    endpointImplementationType: 'ENDPOINT',
                    endpointConfig: { ...generatedEndpointConfig },
                },
            });
        } else {
            const generatedEndpointConfig = createEndpointConfig(selectedKey);
            endpointsDispatcher({
                action: 'select_endpoint_type',
                value: {
                    endpointImplementationType: 'ENDPOINT',
                    endpointConfig: { ...generatedEndpointConfig },
                },
            });
        }
    };

    /**
     * Handles the endpoint type select event. If endpoint config has existing values, show confirmation dialog.
     * @param {any} event The select event.
     * */
    const handleEndpointTypeSelect = (event) => {
        // Check whether the endpoint Config has values.
        if (epConfig.production_endpoints || epConfig.sandbox_endpoints) {
            // Show confirmation dialog
            setTypeChangeConfirmation({ type: event.target.value, openDialog: true });
        } else {
            changeEndpointType(event.target.value);
        }
    };

    /**
     * Method to get the advance configuration from the selected endpoint.
     *
     * @param {number} index The selected endpoint index
     * @param {string} epType The type of the endpoint. (loadbalance/ failover)
     * @param {string} category The endpoint category (Production/ sandbox)
     * @return {object} The advance config object of the endpoint.
     * */
    const getAdvanceConfig = (index, epType, category) => {
        const endpointTypeProperty = getEndpointTypeProperty(epType, category);
        let advanceConfig = {};
        if (index > 0) {
            if (epConfig.endpoint_type === 'failover') {
                advanceConfig = epConfig[endpointTypeProperty][index - 1].config;
            } else {
                advanceConfig = epConfig[endpointTypeProperty][index].config;
            }
        } else {
            const endpointInfo = epConfig[endpointTypeProperty];
            if (Array.isArray(endpointInfo)) {
                advanceConfig = endpointInfo[0].config;
            } else {
                advanceConfig = endpointInfo.config;
            }
        }
        return advanceConfig;
    };

    /**
     * Method to open/ close the advance configuration dialog. This method also sets some information about the
     * seleted endpoint type/ category and index.
     *
     * @param {number} index The index of the selected endpoint.
     * @param {string} type The endpoint type
     * @param {string} category The endpoint category.
     * */
    const toggleAdvanceConfig = (index, type, category) => {
        const advanceEPConfig = getAdvanceConfig(index, type, category);
        setAdvancedConfigOptions(() => {
            return ({
                open: !advanceConfigOptions.open,
                index,
                type,
                category,
                config: advanceEPConfig === undefined ? {} : advanceEPConfig,
            });
        });
    };

    const toggleEndpointSecurityConfig = (type, category) => {
        handleToggleEndpointSecurity();
        setEndpointSecurityConfig(() => {
            return ({
                open: !endpointSecurityConfig.open,
                type,
                category,
                config: endpointSecurityInfo === undefined ? {} : endpointSecurityInfo,
            });
        });
    };

    /**
     * Method to handle the endpoint security changes.
     * @param {string} value The value
     * @param {string} type The security property that is being modified.
     * */
    const handleEndpointSecurityChange = (value, type) => {
        endpointsDispatcher({
            action: 'endpointSecurity',
            value: { ...endpointSecurityInfo, [type]: value },
        });
    };

    const saveEndpointSecurityConfig = (endpointSecurityObj, enType) => {
        const { type } = endpointSecurityObj;
        let newEndpointSecurityObj = endpointSecurityObj;
        if (type === 'NONE') {
            newEndpointSecurityObj = { ...CONSTS.DEFAULT_ENDPOINT_SECURITY, type };
        } else {
            newEndpointSecurityObj.enabled = true;
        }
        endpointsDispatcher({
            action: 'endpointSecurity',
            value: {
                ...endpointSecurityInfo,
                [enType]: newEndpointSecurityObj,
            },
        });
        setEndpointSecurityConfig({ open: false });
    };

    const closeEndpointSecurityConfig = () => {
        setEndpointSecurityConfig({ open: false });
    };

    /**
     * Method to save the advance configurations.
     *
     * @param {object} advanceConfig The advance configuration object.
     * */
    const saveAdvanceConfig = (advanceConfig) => {
        const config = cloneDeep(epConfig);
        const endpointConfigProperty = getEndpointTypeProperty(
            advanceConfigOptions.type, advanceConfigOptions.category,
        );
        const selectedEndpoints = config[endpointConfigProperty];
        if (Array.isArray(selectedEndpoints)) {
            if (advanceConfigOptions.type === 'failover') {
                selectedEndpoints[advanceConfigOptions.index - 1].config = advanceConfig;
            } else {
                selectedEndpoints[advanceConfigOptions.index].config = advanceConfig;
            }
        } else {
            selectedEndpoints.config = advanceConfig;
        }
        setAdvancedConfigOptions({ open: false });
        endpointsDispatcher({
            action: 'set_advance_config',
            value: { ...config, [endpointConfigProperty]: selectedEndpoints },
        });
    };

    /**
     * Method to close the advance configuration dialog box.
     * */
    const closeAdvanceConfig = () => {
        setAdvancedConfigOptions({ open: false });
    };

    /**
     * Method to update the resource paths object in the swagger.
     * @param {any} paths The updated paths object.
     * */
    const updatePaths = useCallback(
        (paths) => {
            updateSwagger({ ...swaggerDef, paths });
        },
        [swaggerDef],
    );

    const iff = (condition, then, otherwise) => (condition ? then : otherwise);

    /**
     *
     *
     * @param {*} event
     */
    function epCategoryOnChangeHandler() {
        handleOnChangeEndpointCategoryChange('prod');
    }

    return (
        <div className={classes.overviewWrapper}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    {api.type === 'WS' ? <div /> : (
                        <FormControl component='fieldset' className={classes.formControl}>
                            <RadioGroup
                                aria-label='EndpointType'
                                name='endpointType'
                                className={classes.radioGroup}
                                value={endpointType.key === 'MOCKED_OAS' ? 'INLINE' : endpointType.key}
                                onChange={handleEndpointTypeSelect}
                            >
                                {supportedEnpointTypes.map((endpoint) => {
                                    return (
                                        <FormControlLabel
                                            value={endpoint.key}
                                            control={(
                                                <Radio
                                                    disabled={(isRestricted(['apim:api_create'], api))}
                                                    color='primary'
                                                    id={endpoint.key}
                                                />
                                            )}
                                            label={endpoint.value}
                                        />
                                    );
                                })}
                            </RadioGroup>
                        </FormControl>
                    )}
                </Grid>
                <Grid item xs={12}>
                    {(endpointType.key === 'INLINE' || endpointType.key === 'MOCKED_OAS') ? 
                        iff(Object.keys(swaggerDef.paths).length !== 0, 
                            <MockImplEndpoints 
                                key={endpointType.key}
                                paths={swaggerDef.paths} 
                                swagger={swaggerDef} 
                                updatePaths={updatePaths} 
                                endpointType={endpointType.key} 
                                endpointConfig={endpointConfig}
                                endpointsDispatcher={endpointsDispatcher}
                            />, 
                            <Progress />)
                        : (
                            <Paper className={classes.endpointContainer}>

                                {endpointType.key === 'service'
                                    ? (
                                        <>
                                            <FormControlLabel
                                                control={(
                                                    <Checkbox
                                                        disabled={isRestricted(
                                                            ['apim:api_create'], api)}
                                                        checked={endpointCategory.prod}
                                                        value='prod'
                                                        color='primary'
                                                        onChange={epCategoryOnChangeHandler}
                                                    />
                                                )}
                                                label={(
                                                    <Typography>
                                                        <FormattedMessage
                                                            id={'Apis.Details.'
                                                                + 'Endpoints.EndpointOverview'
                                                                + '.production.endpoint'
                                                                + '.production.label'}
                                                            defaultMessage='Production Endpoint'
                                                        />
                                                    </Typography>
                                                )}
                                            />
                                            <Collapse in={endpointCategory.prod}>
                                                <ServiceEndpoint
                                                    classes={classes}
                                                    api={api}
                                                    services={servicesList}
                                                    category='production_endpoints'
                                                    type=''
                                                    setAdvancedConfigOpen={toggleAdvanceConfig}
                                                    esCategory='production'
                                                    setESConfigOpen={toggleEndpointSecurityConfig}
                                                    name={<FormattedMessage
                                                        id={'Apis.Details.Endpoints.'
                                                            + 'EndpointOverview.production'
                                                            + '.endpoint.production.header'}
                                                        defaultMessage='Production Endpoint'
                                                    />}
                                                    editEndpoint={editEndpoint}
                                                    endpointURL={getEndpoints
                                                    (
                                                        'production_endpoints'
                                                    )}
                                                    existingService={getService}
                                                    editService={editService}
                                                    index={0} />
                                            </Collapse>
                                            <FormControlLabel
                                                control={(
                                                    <Checkbox
                                                        disabled={isRestricted(
                                                            ['apim:api_create'], api)}
                                                        checked={endpointCategory.sandbox}
                                                        value='sandbox'
                                                        color='primary'
                                                        onChange={() => (
                                                            handleOnChangeEndpointCategoryChange
                                                            (
                                                                'sandbox',
                                                            ))}
                                                    />
                                                )}
                                                label={(
                                                    <FormattedMessage
                                                        id={'Apis.Details.Endpoints.'
                                                            + 'EndpointOverview.sandbox'
                                                            + '.endpoint'}
                                                        defaultMessage='Sandbox Endpoint'
                                                    />
                                                )}
                                            />
                                            <Collapse in={endpointCategory.sandbox}>
                                                <ServiceEndpoint
                                                    classes={classes}
                                                    api={api}
                                                    services={servicesList}
                                                    category='sandbox_endpoints'
                                                    type=''
                                                    setAdvancedConfigOpen={toggleAdvanceConfig}
                                                    esCategory='sandbox'
                                                    setESConfigOpen={toggleEndpointSecurityConfig}
                                                    name={ <FormattedMessage
                                                        id={'Apis.Details.Endpoints.'
                                                            + 'EndpointOverview.sandbox'
                                                            + '.endpoint'}
                                                        defaultMessage='Sandbox Endpoint'
                                                    />}
                                                    editEndpoint={editEndpoint}
                                                    endpointURL={getEndpoints
                                                    (
                                                        'sandbox_endpoints'
                                                    )}
                                                    index={0} />
                                            </Collapse>

                                        </>

                                    )
                                    : (
                                        <>
                                            {endpointType.key === 'awslambda'
                                                ? (
                                                    <Credentials
                                                        apiId={api.id}
                                                        endpointConfig={endpointConfig}
                                                        endpointsDispatcher={endpointsDispatcher}
                                                    />
                                                )
                                                : (
                                                    <>
                                                        {endpointType.key === 'prototyped'
                                                            ? (
                                                                <Typography>
                                                                    <FormattedMessage
                                                                        id={'Apis.Details.Endpoints.'
                                                                            + 'EndpointOverview.prototype.endpoint'
                                                                            + '.prototype.label'}
                                                                        defaultMessage='Prototype Endpoint'
                                                                    />
                                                                </Typography>
                                                            )
                                                            : (
                                                                <FormControlLabel
                                                                    control={(
                                                                        <Checkbox
                                                                            id='production-endpoint-checkbox'
                                                                            disabled={isRestricted(
                                                                                ['apim:api_create'], api)}
                                                                            checked={endpointCategory.prod}
                                                                            value='prod'
                                                                            color='primary'
                                                                            onChange={epCategoryOnChangeHandler}
                                                                        />
                                                                    )}
                                                                    label={(
                                                                        <Typography>
                                                                            <FormattedMessage
                                                                                id={'Apis.Details.'
                                                                                    + 'Endpoints.EndpointOverview'
                                                                                    + '.production.endpoint'
                                                                                    + '.production.label'}
                                                                                defaultMessage='Production Endpoint'
                                                                            />
                                                                        </Typography>
                                                                    )}
                                                                />
                                                            )}
                                                        <Collapse in={endpointCategory.prod}>
                                                            {endpointType.key === 'default'
                                                                ? (

                                                                    <InlineMessage>
                                                                        <div className={classes.contentWrapper}>
                                                                            <Typography component='p'
                                                                                className={classes.content}>
                                                                                <FormattedMessage
                                                                                    id={'Apis.Details.Endpoints'
                                                                                        + '.EndpointOverview'
                                                                                        + '.upload.mediation.message'}
                                                                                    defaultMessage={
                                                                                        'Please upload a mediation'
                                                                                        + ' sequence file to'
                                                                                        + ' Message Mediation Policies,'
                                                                                        + ' which sets the endpoints.'
                                                                                    }
                                                                                />
                                                                                <IconButton
                                                                                    onClick={saveAndRedirect}
                                                                                >
                                                                                    <LaunchIcon
                                                                                        style={{ marginLeft: '2px' }}
                                                                                        fontSize='small'
                                                                                        color='primary'
                                                                                    />
                                                                                </IconButton>
                                                                            </Typography>
                                                                        </div>
                                                                        <Button
                                                                            className={classes.button}
                                                                            aria-label='Settings'
                                                                            onClick={() => toggleAdvanceConfig(
                                                                                0, '', 'production_endpoints',
                                                                            )}
                                                                            disabled={
                                                                                (isRestricted(
                                                                                    ['apim:api_create'], api,
                                                                                )
                                                                                )
                                                                            }
                                                                            variant='outlined'
                                                                        >
                                                                            <Icon
                                                                                className={classes.buttonIcon}
                                                                            >
                                                                                settings
                                                                            </Icon>
                                                                            <Typography>
                                                                                <FormattedMessage
                                                                                    id={'Apis.Details.Endpoints'
                                                                                        + '.EndpointOverview.advance'
                                                                                        + '.endpoint.configuration'}
                                                                                    defaultMessage='Advanced 
                                                                                    Configurations'
                                                                                />
                                                                            </Typography>
                                                                        </Button>
                                                                        <Button
                                                                            className={classes.button}
                                                                            aria-label='Settings'
                                                                            onClick={() => toggleEndpointSecurityConfig(
                                                                                '', 'production',
                                                                            )}
                                                                            disabled={
                                                                                (isRestricted(
                                                                                    ['apim:api_create'], api,
                                                                                )
                                                                                )
                                                                            }
                                                                            variant='outlined'
                                                                        >
                                                                            <Icon
                                                                                className={classes.buttonIcon}
                                                                            >
                                                                                security
                                                                            </Icon>
                                                                            <Typography>
                                                                                <FormattedMessage
                                                                                    id={'Apis.Details.Endpoints'
                                                                                        + '.EndpointOverview.endpoint'
                                                                                        + '.security.configuration'}
                                                                                    defaultMessage={'Endpoint '
                                                                                        + 'Security Configurations'}
                                                                                />
                                                                            </Typography>
                                                                        </Button>
                                                                    </InlineMessage>
                                                                )
                                                                : (
                                                                    <GenericEndpoint
                                                                        autoFocus
                                                                        name={endpointType.key === 'prototyped'
                                                                            ? (
                                                                                <FormattedMessage
                                                                                    id={'Apis.Details.Endpoints.'
                                                                                        + 'EndpointOverview.prototype'
                                                                                        + '.endpoint.prototype.header'}
                                                                                    defaultMessage='Prototype Endpoint'
                                                                                />
                                                                            ) : (
                                                                                <FormattedMessage
                                                                                    id={'Apis.Details.Endpoints.'
                                                                                        + 'EndpointOverview.production'
                                                                                        + '.endpoint.production.header'}
                                                                                    defaultMessage='Production Endpoint'
                                                                                />
                                                                            )}
                                                                        className={classes.defaultEndpointWrapper}
                                                                        endpointURL={getEndpoints
                                                                        (
                                                                            'production_endpoints'
                                                                        )}
                                                                        type=''
                                                                        index={0}
                                                                        category='production_endpoints'
                                                                        editEndpoint={editEndpoint}
                                                                        setAdvancedConfigOpen={toggleAdvanceConfig}
                                                                        esCategory='production'
                                                                        setESConfigOpen={toggleEndpointSecurityConfig}
                                                                        apiId={api.id}
                                                                    />
                                                                )}
                                                        </Collapse>
                                                        {endpointType.key === 'prototyped' ? <div />
                                                            : (
                                                                <div>
                                                                    <FormControlLabel
                                                                        control={(
                                                                            <Checkbox
                                                                                id='sandbox-endpoint-checkbox'
                                                                                disabled={isRestricted(
                                                                                    ['apim:api_create'], api)}
                                                                                checked={endpointCategory.sandbox}
                                                                                value='sandbox'
                                                                                color='primary'
                                                                                onChange={() => (
                                                                                    handleOnChangeEndpointCategoryChange
                                                                                    (
                                                                                        'sandbox',
                                                                                    ))}
                                                                            />
                                                                        )}
                                                                        label={(
                                                                            <FormattedMessage
                                                                                id={'Apis.Details.Endpoints.'
                                                                                    + 'EndpointOverview.sandbox'
                                                                                    + '.endpoint'}
                                                                                defaultMessage='Sandbox Endpoint'
                                                                            />
                                                                        )}
                                                                    />
                                                                    <Collapse in={endpointCategory.sandbox}>
                                                                        {endpointType.key === 'default'
                                                                            ? (
                                                                                <InlineMessage>
                                                                                    <div className={classes.
                                                                                        contentWrapper}>
                                                                                        <Typography
                                                                                            component='p'
                                                                                            className={classes.content}
                                                                                        >
                                                                                            <FormattedMessage
                                                                                                id={'Apis.Details'
                                                                                                    + '.Endpoints'
                                                                                                    + '.Endpoint'
                                                                                                    + 'Overview'
                                                                                                    + '.upload'
                                                                                                    + '.mediation'
                                                                                                    + '.message'}
                                                                                                defaultMessage={
                                                                                                    'Please upload '
                                                                                                    + ' a mediation'
                                                                                                    + ' sequence '
                                                                                                    + 'file to'
                                                                                                    + ' Message '
                                                                                                    + '  Mediation'
                                                                                                    + ' Policies,'
                                                                                                    + ' which sets the'
                                                                                                    + ' endpoints.'
                                                                                                }
                                                                                            />
                                                                                            <IconButton
                                                                                                onClick={
                                                                                                    saveAndRedirect
                                                                                                }
                                                                                            >
                                                                                                <LaunchIcon
                                                                                                    style={{
                                                                                                        marginLeft:
                                                                                                            '2px'
                                                                                                    }}
                                                                                                    fontSize='small'
                                                                                                    color='primary'
                                                                                                />
                                                                                            </IconButton>
                                                                                        </Typography>
                                                                                    </div>
                                                                                    <Button
                                                                                        className={classes.button}
                                                                                        aria-label='Settings'
                                                                                        onClick={() =>
                                                                                            toggleAdvanceConfig(
                                                                                                0, '',
                                                                                                'sandbox_endpoints',
                                                                                            )}
                                                                                        disabled={
                                                                                            (isRestricted(
                                                                                                ['apim:api_create'],
                                                                                                api,
                                                                                            )
                                                                                            )
                                                                                        }
                                                                                        variant='outlined'
                                                                                    >
                                                                                        <Icon
                                                                                            className={
                                                                                                classes.buttonIcon}
                                                                                        >
                                                                                            settings
                                                                                        </Icon>
                                                                                        <Typography>
                                                                                            <FormattedMessage
                                                                                                id={'Apis.Details.'
                                                                                                + 'Endpoints'
                                                                                                + '.EndpointOverview.'
                                                                                                + 'advance'
                                                                                                + '.endpoint.'
                                                                                                + 'configuration'}
                                                                                                defaultMessage={
                                                                                                    'Advanced '
                                                                                                    + 'Configurations'}
                                                                                            />
                                                                                        </Typography>
                                                                                    </Button>
                                                                                </InlineMessage>
                                                                            )
                                                                            : (
                                                                                <GenericEndpoint
                                                                                    autoFocus
                                                                                    name='Sandbox Endpoint'
                                                                                    className={classes.
                                                                                        defaultEndpointWrapper}
                                                                                    endpointURL={getEndpoints
                                                                                    (
                                                                                        'sandbox_endpoints'
                                                                                    )}
                                                                                    type=''
                                                                                    index={0}
                                                                                    category='sandbox_endpoints'
                                                                                    editEndpoint={editEndpoint}
                                                                                    esCategory='sandbox'
                                                                                    setAdvancedConfigOpen=
                                                                                        {toggleAdvanceConfig}
                                                                                    setESConfigOpen=
                                                                                        {toggleEndpointSecurityConfig}
                                                                                    apiId={api.id}
                                                                                />
                                                                            )}

                                                                    </Collapse>
                                                                </div>
                                                            )}
                                                    </>
                                                )}
                                        </>
                                    )}
                            </Paper>
                        )}
                </Grid>
                {endpointType.key === 'INLINE' || endpointType.key === 'MOCKED_OAS' || 
                    endpointType.key === 'prototyped' || endpointType.key === 'awslambda' || api.type === 'WS'
                    ? <div />
                    : (
                        <Grid item xs={12}>
                            <Typography variant='h4' align='left' className={classes.titleWrapper} gutterBottom>
                                <FormattedMessage
                                    id='Apis.Details.Endpoints.EndpointOverview.general.config.header'
                                    defaultMessage='General Endpoint Configurations'
                                />
                            </Typography>
                            <GeneralConfiguration
                                epConfig={(cloneDeep(epConfig))}
                                endpointType={endpointType}
                                endpointsDispatcher={endpointsDispatcher}
                            />
                        </Grid>
                    )}
                {
                    endpointType.key === 'INLINE'
                        || endpointType.key === 'MOCKED_OAS'
                        || endpointType.key === 'default'
                        || endpointType.key === 'prototyped'
                        || api.type === 'WS'
                        || endpointType.key === 'awslambda'
                        || endpointType.key === 'service'
                        ? <div />
                        : (
                            <Grid item xs={12}>
                                <Typography
                                    variant='h4'
                                    align='left'
                                    className={classes.titleWrapper}
                                    gutterBottom
                                >
                                    <FormattedMessage
                                        id={'Apis.Details.Endpoints.'
                                            + 'EndpointOverview.lb.failover.endpoints.header'}
                                        defaultMessage='Load balance and Failover Configurations'
                                    />
                                </Typography>
                                <LoadbalanceFailoverConfig
                                    handleEndpointCategorySelect={handleEndpointCategorySelect}
                                    toggleAdvanceConfig={toggleAdvanceConfig}
                                    toggleESConfig={toggleEndpointSecurityConfig}
                                    endpointsDispatcher={endpointsDispatcher}
                                    epConfig={(cloneDeep(epConfig))}
                                    endpointSecurityInfo={endpointSecurityInfo}
                                    handleEndpointTypeSelect={handleEndpointTypeSelect}
                                    globalEpType={endpointType}
                                    apiType={api.type}
                                />
                            </Grid>
                        )
                }
            </Grid>
            <Dialog open={advanceConfigOptions.open}>
                <DialogTitle>
                    <Typography className={classes.configDialogHeader}>
                        <FormattedMessage
                            id='Apis.Details.Endpoints.EndpointOverview.advance.endpoint.configuration'
                            defaultMessage='Advanced Configurations'
                        />
                    </Typography>
                </DialogTitle>
                <DialogContent>
                    <AdvanceEndpointConfig
                        isSOAPEndpoint={endpointType.key === 'address'}
                        advanceConfig={advanceConfigOptions.config}
                        onSaveAdvanceConfig={saveAdvanceConfig}
                        onCancel={closeAdvanceConfig}
                    />
                </DialogContent>
            </Dialog>
            <Dialog open={endpointSecurityConfig.open}>
                <DialogTitle>
                    <Typography className={classes.configDialogHeader}>
                        <FormattedMessage
                            id='Apis.Details.Endpoints.EndpointOverview.endpoint.security.configuration'
                            defaultMessage='Endpoint Security Configurations'
                        />
                    </Typography>
                </DialogTitle>
                <DialogContent>
                    {endpointSecurityConfig.category === 'production' ? (
                        <EndpointSecurity
                            securityInfo={endpointSecurityInfo
                                && (endpointSecurityInfo.production
                                    ? endpointSecurityInfo.production : endpointSecurityInfo)}
                            onChangeEndpointAuth={handleEndpointSecurityChange}
                            saveEndpointSecurityConfig={saveEndpointSecurityConfig}
                            closeEndpointSecurityConfig={closeEndpointSecurityConfig}
                            isProduction
                        />
                    ) : (
                        <EndpointSecurity
                            securityInfo={endpointSecurityInfo
                                && (endpointSecurityInfo.sandbox
                                    ? endpointSecurityInfo.sandbox : endpointSecurityInfo)}
                            onChangeEndpointAuth={handleEndpointSecurityChange}
                            saveEndpointSecurityConfig={saveEndpointSecurityConfig}
                            closeEndpointSecurityConfig={closeEndpointSecurityConfig}
                        />
                    )}
                </DialogContent>
            </Dialog>
            <Dialog open={typeChangeConfirmation.openDialog}>
                <DialogTitle>
                    <Typography className={classes.configDialogHeader}>
                        <FormattedMessage
                            id='Apis.Details.Endpoints.EndpointOverview.endpoint.type.change.confirmation'
                            defaultMessage='Change Endpoint Type'
                        />
                    </Typography>
                </DialogTitle>
                <DialogContent>
                    <Typography>
                        <FormattedMessage
                            id='Apis.Details.Endpoints.EndpointOverview.endpoint.type.change.confirmation.message'
                            defaultMessage='Your current endpoint configuration will be lost.'
                        />
                    </Typography>
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() => { setTypeChangeConfirmation({ openDialog: false, serviceInfo: false }); }}
                        color='primary'
                    >
                        <FormattedMessage
                            id='Apis.Details.Endpoints.EndpointOverview.change.type.cancel'
                            defaultMessage='Cancel'
                        />
                    </Button>
                    <Button
                        onClick={() => { changeEndpointType(typeChangeConfirmation.type); }}
                        color='primary'
                        id='change-endpoint-type-btn'
                    >
                        <FormattedMessage
                            id='Apis.Details.Endpoints..EndpointOverview.change.type.proceed'
                            defaultMessage='Proceed'
                        />
                    </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

EndpointOverview.propTypes = {
    classes: PropTypes.shape({
        overviewWrapper: PropTypes.shape({}),
        endpointTypesWrapper: PropTypes.shape({}),
        endpointName: PropTypes.shape({}),
    }).isRequired,
    api: PropTypes.shape({}).isRequired,
    endpointsDispatcher: PropTypes.func.isRequired,
    swaggerDef: PropTypes.shape({}).isRequired,
    updateSwagger: PropTypes.func.isRequired,
    saveAndRedirect: PropTypes.func.isRequired,
};

export default injectIntl(withStyles(styles)(EndpointOverview));
