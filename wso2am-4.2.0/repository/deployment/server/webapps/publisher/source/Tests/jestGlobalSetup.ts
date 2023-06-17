/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.com). All Rights Reserved.
 *
 * This software is the property of WSO2 Inc. and its suppliers, if any.
 * Dissemination of any information or reproduction of any material contained
 * herein is strictly forbidden, unless permitted by WSO2 in accordance with
 * the WSO2 Commercial License available at http://wso2.com/licenses.
 * For specific language governing the permissions and limitations under
 * this license, please see the license as well as any agreement you’ve
 * entered into with WSO2 governing the purchase of this software and any
 * associated services.
 */

import {
  OASConfigs,
  APIConfig,
  downloadOASDefinition,
} from "./Utils/setupUtils";

export default async () => {
  console.log("\nConfiguring OpenAPI mocks");
  for (const [, apiConfig] of OASConfigs()) {
    const { inputSpec } = apiConfig as APIConfig;
    await downloadOASDefinition(inputSpec);
  }
};
