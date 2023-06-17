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

import React, { FC, useContext, useEffect, useState } from 'react';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import ExpansionPanelDetails from '@material-ui/core/ExpansionPanelDetails';
import Typography from '@material-ui/core/Typography';
import { makeStyles, Theme } from '@material-ui/core/styles';
import { FormattedMessage } from 'react-intl';
import APIContext from 'AppComponents/Apis/Details/components/ApiContext';
import API from 'AppData/api';
import PolicyDropzone from './PolicyDropzone';
import type { AttachedPolicy, Policy, PolicySpec } from './Types';
import FlowArrow from './components/FlowArrow';
import ApiOperationContext from './ApiOperationContext';

const useStyles = makeStyles((theme: Theme) => ({
    flowSpecificPolicyAttachGrid: {
        marginTop: theme.spacing(1),
        overflowX: 'scroll',
    },
}));

const defaultPolicyForMigration = {
    id: '',
    category: 'Mediation',
    name: '',
    displayName: '',
    version: '',
    description: '',
    applicableFlows: [],
    supportedGateways: ['Synapse'],
    supportedApiTypes: [],
    policyAttributes: [],
    isAPISpecific: true,
};

interface PoliciesExpansionProps {
    target: any;
    verb: string;
    allPolicies: PolicySpec[] | null;
    isChoreoConnectEnabled: boolean;
    policyList: Policy[];
}

const PoliciesExpansion: FC<PoliciesExpansionProps> = ({
    target,
    verb,
    allPolicies,
    isChoreoConnectEnabled,
    policyList,
}) => {
    // Policies attached for each request, response and fault flow
    const [requestFlowPolicyList, setRequestFlowPolicyList] = useState<AttachedPolicy[]>([]);
    const [responseFlowPolicyList, setResponseFlowPolicyList] = useState<AttachedPolicy[]>([]);
    const [faultFlowPolicyList, setFaultFlowPolicyList] = useState<AttachedPolicy[]>([]);

    // Droppable policy identifier list for each request, response and fault flow
    const [requestFlowDroppablePolicyList, setRequestFlowDroppablePolicyList] = useState<string[]>([]);
    const [responseFlowDroppablePolicyList, setResponseFlowDroppablePolicyList] = useState<string[]>([]);
    const [faultFlowDroppablePolicyList, setFaultFlowDroppablePolicyList] = useState<string[]>([]);

    const classes = useStyles();
    const { apiOperations } = useContext<any>(ApiOperationContext);
    const { api } = useContext<any>(APIContext);

    useEffect(() => {
        const requestList = [];
        const responseList = [];
        const faultList = [];
        for (const policy of policyList) {
            if (policy.applicableFlows.includes('request')) {
                requestList.push(`policyCard-${policy.id}`);
            }
            if (policy.applicableFlows.includes('response')) {
                responseList.push(`policyCard-${policy.id}`);
            }
            if (policy.applicableFlows.includes('fault')) {
                faultList.push(`policyCard-${policy.id}`);
            }
        }
        setRequestFlowDroppablePolicyList(requestList);
        setResponseFlowDroppablePolicyList(responseList);
        setFaultFlowDroppablePolicyList(faultList);
    }, [policyList]);

    useEffect(() => {
        (async () => {
            let operationInAction = apiOperations.find(
                (op: any) =>
                    op.target === target &&
                    op.verb.toLowerCase() === verb.toLowerCase(),
            );

            // Populate request flow attached policy list
            const requestFlowList: AttachedPolicy[] = [];
            const requestFlow = operationInAction.operationPolicies.request;
            for (const requestFlowAttachedPolicy of requestFlow) {
                const { policyId, policyName, policyVersion, uuid } =
                    requestFlowAttachedPolicy;
                if (policyId === null) {
                    // Handling migration flow
                    requestFlowList.push({
                        ...defaultPolicyForMigration,
                        name: policyName,
                        displayName: policyName,
                        applicableFlows: ['request'],
                        uniqueKey: uuid,
                    });
                } else {
                    const policyObj = allPolicies?.find(
                        (policy: PolicySpec) => 
                            policy.name === policyName && 
                            policy.version === policyVersion,
                    );
                    if (policyObj) {
                        requestFlowList.push({ ...policyObj, uniqueKey: uuid });
                    } else {
                        try {
                            // eslint-disable-next-line no-await-in-loop
                            const policyResponse = await API.getOperationPolicy(
                                policyId,
                                api.id,
                            );
                            if (policyResponse)
                                requestFlowList.push({
                                    ...policyResponse.body,
                                    uniqueKey: uuid,
                                });
                        } catch (error) {
                            console.error(error);
                        }
                    }
                }
            }
            setRequestFlowPolicyList(requestFlowList);

            // Populate response flow attached policy list
            const responseFlowList: AttachedPolicy[] = [];
            const responseFlow = operationInAction.operationPolicies.response;
            for (const responseFlowAttachedPolicy of responseFlow) {
                const { policyId, policyName, policyVersion, uuid } =
                    responseFlowAttachedPolicy;
                if (policyId === null) {
                    // Handling migration flow
                    responseFlowList.push({
                        ...defaultPolicyForMigration,
                        name: policyName,
                        displayName: policyName,
                        applicableFlows: ['response'],
                        uniqueKey: uuid,
                    });
                } else {
                    const policyObj = allPolicies?.find(
                        (policy: PolicySpec) => 
                            policy.name === policyName && 
                            policy.version === policyVersion,
                    );
                    if (policyObj) {
                        responseFlowList.push({ ...policyObj, uniqueKey: uuid });
                    } else {
                        try {
                            // eslint-disable-next-line no-await-in-loop
                            const policyResponse = await API.getOperationPolicy(
                                policyId,
                                api.id,
                            );
                            if (policyResponse)
                                responseFlowList.push({
                                    ...policyResponse.body,
                                    uniqueKey: uuid,
                                });
                        } catch (error) {
                            console.error(error);
                        }
                    }   
                }
            }
            setResponseFlowPolicyList(responseFlowList);

            if (!isChoreoConnectEnabled) {
                // Populate fault flow attached policy list
                const faultFlowList: AttachedPolicy[] = [];
                const faultFlow = operationInAction.operationPolicies.fault;
                for (const faultFlowAttachedPolicy of faultFlow) {
                    const { policyId, policyName, policyVersion, uuid } =
                        faultFlowAttachedPolicy;
                    if (policyId === null) {
                        // Handling migration flow
                        faultFlowList.push({
                            ...defaultPolicyForMigration,
                            name: policyName,
                            displayName: policyName,
                            applicableFlows: ['fault'],
                            uniqueKey: uuid,
                        });
                    } else {
                        const policyObj = allPolicies?.find(
                            (policy: PolicySpec) => 
                                policy.name === policyName && 
                                policy.version == policyVersion,
                        );
                        if (policyObj) {
                            faultFlowList.push({ ...policyObj, uniqueKey: uuid });
                        } else {
                            try {
                                // eslint-disable-next-line no-await-in-loop
                                const policyResponse = await API.getOperationPolicy(
                                    policyId,
                                    api.id,
                                );
                                if (policyResponse)
                                    faultFlowList.push({
                                        ...policyResponse.body,
                                        uniqueKey: uuid,
                                    });
                            } catch (error) {
                                console.error(error);
                            }
                        }
                    }
                }
                setFaultFlowPolicyList(faultFlowList);
            }
        })();
    }, [apiOperations]);

    return (
        <ExpansionPanelDetails>
            <Grid
                spacing={2}
                container
                direction='row'
                justify='flex-start'
                alignItems='flex-start'
            >
                <Grid item xs={12} md={12}>
                    <Box className={classes.flowSpecificPolicyAttachGrid} data-testid='drop-policy-zone-request'>
                        <Typography variant='subtitle2' align='left'>
                            <FormattedMessage
                                id='Apis.Details.Policies.PoliciesExpansion.request.flow.title'
                                defaultMessage='Request Flow'
                            />
                        </Typography>
                        <FlowArrow arrowDirection='left' />
                        <PolicyDropzone
                            policyDisplayStartDirection='left'
                            currentPolicyList={requestFlowPolicyList}
                            setCurrentPolicyList={setRequestFlowPolicyList}
                            droppablePolicyList={requestFlowDroppablePolicyList}
                            currentFlow='request'
                            target={target}
                            verb={verb}
                            allPolicies={allPolicies}
                        />
                    </Box>
                    <Box className={classes.flowSpecificPolicyAttachGrid} data-testid='drop-policy-zone-response'>
                        <Typography variant='subtitle2' align='left'>
                            <FormattedMessage
                                id='Apis.Details.Policies.PoliciesExpansion.response.flow.title'
                                defaultMessage='Response Flow'
                            />
                        </Typography>
                        <FlowArrow arrowDirection='right' />
                        <PolicyDropzone
                            policyDisplayStartDirection='right'
                            currentPolicyList={responseFlowPolicyList}
                            setCurrentPolicyList={setResponseFlowPolicyList}
                            droppablePolicyList={
                                responseFlowDroppablePolicyList
                            }
                            currentFlow='response'
                            target={target}
                            verb={verb}
                            allPolicies={allPolicies}
                        />
                    </Box>
                    {!isChoreoConnectEnabled && (
                        <Box className={classes.flowSpecificPolicyAttachGrid}>
                            <Typography variant='subtitle2' align='left'>
                                <FormattedMessage
                                    id='Apis.Details.Policies.PoliciesExpansion.fault.flow.title'
                                    defaultMessage='Fault Flow'
                                />
                            </Typography>
                            <FlowArrow arrowDirection='right' />
                            <PolicyDropzone
                                policyDisplayStartDirection='right'
                                currentPolicyList={faultFlowPolicyList}
                                setCurrentPolicyList={setFaultFlowPolicyList}
                                droppablePolicyList={
                                    faultFlowDroppablePolicyList
                                }
                                currentFlow='fault'
                                target={target}
                                verb={verb}
                                allPolicies={allPolicies}
                            />
                        </Box>
                    )}
                </Grid>
            </Grid>
        </ExpansionPanelDetails>
    );
};

export default PoliciesExpansion;
