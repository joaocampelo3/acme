/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import React, { useState, useEffect } from 'react';
import {
    Button,
    Grid,
    IconButton,
    Tooltip,
    Typography,
    useTheme,
    makeStyles,
} from '@material-ui/core';
import API from 'AppData/api';
import { Progress } from 'AppComponents/Shared';
import { FormattedMessage, useIntl } from 'react-intl';
import { Link } from 'react-router-dom';
import AddCircle from '@material-ui/icons/AddCircle';
import MUIDataTable from 'mui-datatables';
import Icon from '@material-ui/core/Icon';
import { isRestricted } from 'AppData/AuthManager';
import Box from '@material-ui/core/Box';
import OnboardingMenuCard from 'AppComponents/Shared/Onboarding/OnboardingMenuCard';
import Onboarding from 'AppComponents/Shared/Onboarding/Onboarding';
import HelpOutlineIcon from '@material-ui/icons/HelpOutline';
import Chip from '@material-ui/core/Chip';
import ArrowForward from '@material-ui/icons/ArrowForward';
import ArrowBack from '@material-ui/icons/ArrowBack';
import TrendingDown from '@material-ui/icons/TrendingDown';
import ResourceNotFoundError from 'AppComponents/Base/Errors/ResourceNotFoundError';
import CONSTS from 'AppData/Constants';
import Delete from './DeletePolicy';
import CommonPolicyGatewaySelector from './CommonPolicyGatewaySelector';

const useStyles = makeStyles((theme) => ({
    table: {
        marginLeft: 'auto',
        marginRight: 'auto',
        '& > td[class^=MUIDataTableBodyCell-cellHide-]': {
            display: 'none',
        },
        '& .MUIDataTableBodyCell-cellHide-793': {
            display: 'none',
        },
        '& td': {
            wordBreak: 'break-word',
        },
        '& th': {
            minWidth: '150px',
        },
    },
    heading: {
        flexGrow: 1,
        marginTop: 10,
    },
    titleWrapper: {
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        marginBottom: theme.spacing(2),
        marginLeft: 'auto',
        marginRight: 'auto',
    },
    mainTitle: {
        paddingLeft: 0,
    },
    buttonIcon: {
        marginRight: theme.spacing(1),
    },
    icon: {
        marginRight: theme.spacing(0.5),
    },
}));

/**
 * Renders the common policy management UI.
 * @returns {JSX} Policy management page to render.
 */
const Listing = () => {
    const intl = useIntl();
    const theme = useTheme();
    const classes = useStyles();
    const { commonPolicyAddIcon } = theme.custom.landingPage.icons;
    const [policies, setPolicies] = useState(null);
    const [loading, setLoading] = useState(false);
    const [notFound, setnotFound] = useState(false);
    const [isAllowedToFilterCCPolicies, setIsAllowedToFilterCCPolicies] = useState(false);

    /**
     * 
     * @param {boolean} isCCEnabled : Indicates whether Choreo Connect is selected or not.
     */
    const handleGatewayTypeSelection = (isCCEnabled) => {
        setIsAllowedToFilterCCPolicies(isCCEnabled);
    } 

    const fetchCommonPolicies = () => {
        setLoading(true);
        const promisedPolicies = API.getCommonOperationPolicies();
        promisedPolicies
            .then((response) => {
                setPolicies(response.body.list);
            })
            .catch((error) => {
                console.error(error);
                setnotFound(true);
            })
            .finally(() => {
                setLoading(false);
            });
    };

    // Provides the gateway specific policies list.
    const getPoliciesList = () => {
        let gatewayType = CONSTS.GATEWAY_TYPE.synapse;
        if (isAllowedToFilterCCPolicies) {
            gatewayType = CONSTS.GATEWAY_TYPE.choreoConnect;
        }
        // removes irrelevant policies for the selected gateway type
        return policies?.filter((policy) => {
            return policy.supportedGateways.includes(gatewayType)
        }).map((policyObj) => {
            const policy = [];
            policyObj.supportedGateways.forEach((policyGateway) => {
                if (gatewayType === policyGateway) {
                    policy.push(policyObj.id);
                    policy.push(policyObj.displayName);
                    policy.push(policyObj.version);
                    policy.push(policyObj.description);
                    policy.push(policyObj.applicableFlows);
                }
            });
            return policy;
        });
    }

    useEffect(() => {
        fetchCommonPolicies();
    }, []);

    const getViewUrl = (policyId) => {
        return `/policies/${policyId}/view`;
    };

    policies?.sort((a, b) => a.displayName.localeCompare(b.displayName));

    const policiesList = getPoliciesList();

    const columns = [
        {
            name: 'id',
            options: {
                display: 'excluded',
                filter: false,
            },
        },
        intl.formatMessage({
            id: 'CommonPolicies.Listing.table.header.policy.name',
            defaultMessage: 'Policy Name',
        }),
        intl.formatMessage({
            id: 'CommonPolicies.Listing.table.header.policy.version',
            defaultMessage: 'Policy Version',
        }),
        intl.formatMessage({
            id: 'CommonPolicies.Listing.table.header.description',
            defaultMessage: 'Description',
        }),
        {
            options: {
                customBodyRender: (value, tableMeta) => {
                    if (tableMeta.rowData) {
                        const flows = value || [];
                        return flows.map((flow) => {
                            let chipColor = theme.custom.policyFlowChipColor
                                ? theme.custom.policyFlowChipColor[
                                    flow.toLowerCase()
                                ]
                                : null;
                            let chipTextColor = '#000000';
                            if (!chipColor) {
                                // The policyFlowChipColor is not populated properly
                                chipColor = '#cccccc';
                            } else {
                                chipTextColor = theme.palette.getContrastText(
                                    theme.custom.policyFlowChipColor[
                                        flow.toLowerCase()
                                    ],
                                );
                            }
                            let flowIcon = null;
                            if (flow === 'request') {
                                flowIcon = <ArrowForward />;
                            } else if (flow === 'response') {
                                flowIcon = <ArrowBack />;
                            } else if (flow === 'fault') {
                                flowIcon = <TrendingDown />;
                            }
                            return (
                                <Chip
                                    key={flow}
                                    label={flow.toUpperCase()}
                                    style={{
                                        backgroundColor: chipColor,
                                        color: chipTextColor,
                                        height: 20,
                                        fontSize: 9,
                                        margin: theme.spacing(0.3),
                                    }}
                                    icon={flowIcon}
                                />
                            );
                        });
                    }
                    return false;
                },
                filter: false,
                sort: false,
                label: (
                    <FormattedMessage
                        id='CommonPolicies.Listing.table.header.applicable.flows'
                        defaultMessage='Applicable Flows'
                    />
                ),
            },
        },
        {
            name: 'Actions',
            options: {
                customBodyRender: (value, tableMeta) => {
                    if (tableMeta.rowData) {
                        const policyId = tableMeta.rowData[0];
                        const policyName = tableMeta.rowData[1];
                        return (
                            <Box display='flex' flexDirection='row'>
                                <Button
                                    disabled={isRestricted([
                                        'apim:api_view',
                                        'apim:api_manage',
                                        'apim:mediation_policy_view',
                                        'apim:mediation_policy_manage',
                                        'apim:api_mediation_policy_manage',
                                    ])}
                                    aria-label={'View ' + policyName}
                                    component={Link}
                                    to={
                                        !isRestricted([
                                            'apim:api_view',
                                            'apim:api_manage',
                                            'apim:mediation_policy_view',
                                            'apim:mediation_policy_manage',
                                            'apim:api_mediation_policy_manage',
                                        ]) && {
                                            pathname: getViewUrl(policyId),
                                            state: {
                                                policyName,
                                                policyId,
                                            },
                                        }
                                    }
                                >
                                    <Icon className={classes.icon}>
                                        visibility
                                    </Icon>
                                    <FormattedMessage
                                        id='CommonPolicies.Listing.table.header.actions.view'
                                        defaultMessage='View'
                                    />
                                </Button>
                                <Delete
                                    policyId={policyId}
                                    policyName={policyName}
                                    fetchCommonPolicies={fetchCommonPolicies}
                                />
                            </Box>
                        );
                    }
                    return false;
                },
                filter: false,
                sort: false,
                label: (
                    <FormattedMessage
                        id='CommonPolicies.Listing.table.header.actions.title'
                        defaultMessage='Actions'
                    />
                ),
            },
        },
    ];

    const options = {
        filterType: 'multiselect',
        selectableRows: 'none',
        title: false,
        filter: false,
        sort: false,
        print: false,
        download: false,
        viewColumns: false,
        customToolbar: false,
        rowsPerPageOptions: [5, 10, 25, 50, 100],
    };

    if (policies && policies.length === 0) {
        return (
            <Onboarding
                title={
                    <FormattedMessage
                        id='CommonPolicies.Listing.onboarding.create.new'
                        defaultMessage='Let’s get started !'
                    />
                }
                subTitle={
                    <FormattedMessage
                        id='CommonPolicies.Listing.onboarding.policies.tooltip'
                        defaultMessage={
                            'Policies provide the capability to alter the behavior ' +
                            'of API resources'
                        }
                    />
                }
            >
                <OnboardingMenuCard
                    to={CONSTS.PATH_TEMPLATES.COMMON_POLICY_CREATE}
                    name='Policies'
                    iconName={commonPolicyAddIcon}
                    disabled={isRestricted([
                        'apim:api_create',
                        'apim:api_manage',
                        'apim:mediation_policy_create',
                        'apim:mediation_policy_manage',
                        'apim:api_mediation_policy_manage',
                    ])}
                />
            </Onboarding>
        );
    }

    if (loading) {
        return <Progress per={90} message='Loading Policies ...' />;
    }

    if (notFound || !policies) {
        return <ResourceNotFoundError />;
    }

    return (
        <div className={classes.heading}>
            <Grid
                className={classes.titleWrapper}
                xs={12}
                sm={12}
                md={11}
                lg={11}
                item
            >
                <Typography
                    variant='h4'
                    align='left'
                    component='h1'
                    className={classes.mainTitle}
                >
                    <FormattedMessage
                        id='CommonPolicies.Listing.policies.title.name'
                        defaultMessage='Policies'
                    />
                </Typography>
                <Tooltip
                    title={
                        <FormattedMessage
                            id='CommonPolicies.Listing.policies.title.tooltip'
                            defaultMessage={
                                'You can utilize these policies at the operation level' +
                                ' by navigating to the Policies tab under any desired API'
                            }
                        />
                    }
                    placement='bottom-start'
                >
                    <IconButton size='small' aria-label='Policy-helper-text'>
                        <HelpOutlineIcon fontSize='small' />
                    </IconButton>
                </Tooltip>
                <Box pl={1}>
                    <Button
                        color='primary'
                        variant='outlined'
                        size='small'
                        data-testid='add-new-common-policy'
                        disabled={isRestricted([
                            'apim:api_create',
                            'apim:api_manage',
                            'apim:mediation_policy_create',
                            'apim:mediation_policy_manage',
                            'apim:api_mediation_policy_manage',
                        ])}
                        component={Link}
                        to={
                            !isRestricted([
                                'apim:api_create',
                                'apim:api_manage',
                                'apim:mediation_policy_create',
                                'apim:mediation_policy_manage',
                                'apim:api_mediation_policy_manage',
                            ]) && CONSTS.PATH_TEMPLATES.COMMON_POLICY_CREATE
                        }
                    >
                        <AddCircle className={classes.buttonIcon} />
                        <FormattedMessage
                            id='CommonPolicies.Listing.policies.title.add.new.policy'
                            defaultMessage='Add New Policy'
                        />
                    </Button>
                </Box>
                {isRestricted([
                    'apim:api_create',
                    'apim:api_manage',
                    'apim:mediation_policy_create',
                    'apim:mediation_policy_manage',
                    'apim:api_mediation_policy_manage',
                ]) && (
                    <Grid item>
                        <Typography variant='body2' color='primary'>
                            <FormattedMessage
                                id='CommonPolicies.Listing.policies.title.update.not.allowed'
                                defaultMessage={
                                    '*You are not authorized to manage policies ' +
                                    'due to insufficient permissions'
                                }
                            />
                        </Typography>
                    </Grid>
                )}
            </Grid>
            <Grid
                className={classes.table}
                xs={12}
                sm={12}
                md={11}
                lg={11}
                item
            >
                <Box>
                    <CommonPolicyGatewaySelector
                        handleGatewayTypeSelection={handleGatewayTypeSelection}
                        isAllowedToFilterCCPolicies={isAllowedToFilterCCPolicies}
                    />
                </Box>
                <MUIDataTable
                    title={false}
                    data={policiesList}
                    columns={columns}
                    options={options}
                />
            </Grid>
        </div>
    );
};

export default Listing;
