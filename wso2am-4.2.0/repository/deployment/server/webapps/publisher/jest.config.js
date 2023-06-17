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

/**
 * Refer https://jestjs.io/docs/en/configuration for more information about jest configs
 * Added monaco-editor mapping because of this issue
 *      https://github.com/react-monaco-editor/react-monaco-editor/issues/133#issuecomment-403960502
 * - per-invocation config (globalSetup, globalTeardown)
*  - per-worker (not existent, see Per-worker setup/teardown #8708)
*  - per-suite (setupFiles, setupFilesAfterEnv, beforeAll, afterAll)
*  - per-test (beforeEach, afterEach).
 */
module.exports = {
    setupFilesAfterEnv: ['<rootDir>/source/Tests/setupFilesAfterEnv.ts'],
    setupFiles: ['<rootDir>/source/Tests/setupTests.ts'],
    globalSetup: '<rootDir>/source/Tests/jestGlobalSetup.ts',
    moduleNameMapper: {
        'AppComponents(.*)$': '<rootDir>/source/src/app/components/$1',
        'AppData(.*)$': '<rootDir>/source/src/app/data/$1',
        'AppTests(.*)$': '<rootDir>/source/Tests/$1',
        '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$':
            '<rootDir>/source/Tests/Unit/__mocks__/fileMock.js',
        '\\.(css|less)$': '<rootDir>/source/Tests/Unit/__mocks__/styleMock.js',
        userCustomThemes: '<rootDir>/site/public/conf/userThemes.js',
        '^Config$': '<rootDir>/site/public/conf/settings.json',
        '^MaterialIcons$': '<rootDir>/site/public/fonts/iconfont/MaterialIcons.js',
        '^monaco-editor$': '<rootDir>/node_modules/react-monaco-editor',
        '^nimma/legacy$': '<rootDir>/node_modules/nimma/dist/legacy/cjs/index.js',
        '^nimma/fallbacks$': '<rootDir>/node_modules/nimma/dist/legacy/cjs/fallbacks/index.js',
    },
    testPathIgnorePatterns: [
        '<rootDir>/node_modules/', 
        '<rootDir>/source/Tests/Integration/',
        '<rootDir>/source/src/app/components/Apis/Details/NewOverview/Overview.test.jsx',
        '<rootDir>/source/src/app/components/Apis/Create/GraphQL/ApiCreateGraphQL.test.tsx'],
    transformIgnorePatterns: ['<rootDir>/node_modules/'],

    // Automatically clear mock calls and instances between every test
    clearMocks: true,

    // Indicates whether the coverage information should be collected while executing the test
    collectCoverage: false,

    // An array of glob patterns indicating a set of files for which coverage information should be collected
    collectCoverageFrom: [
        'source/**/*.{js,jsx,ts,tsx}',
        '!source/**/*.d.ts',
        '!**/node_modules/**',
    ],

    // The directory where Jest should output its coverage files
    coverageDirectory: 'coverage',

    // A list of reporter names that Jest uses when writing coverage reports
    coverageReporters: ['json', 'text', 'lcov', 'clover', 'html'],

    // The test environment that will be used for testing,
    // Default is node https://github.com/jest-community/vscode-jest/issues/165
    testEnvironment: 'jsdom',
};
